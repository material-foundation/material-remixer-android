/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.annotation.processor;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.annotation.BooleanRemixMethod;
import com.google.android.libraries.remixer.annotation.RangeRemixMethod;
import com.google.android.libraries.remixer.annotation.RemixerInstance;
import com.google.android.libraries.remixer.annotation.StringListRemixMethod;
import com.google.android.libraries.remixer.annotation.StringRemixMethod;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * The Annotation processor for Remixer. It checks that the annotations in {@code
 * com.google.android.libraries.remixer.annotation} are applied correctly and generates code to
 * support them.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({
    "com.google.android.libraries.remixer.annotation.BooleanRemixMethod",
    "com.google.android.libraries.remixer.annotation.RangeRemixMethod",
    "com.google.android.libraries.remixer.annotation.StringListRemixMethod",
    "com.google.android.libraries.remixer.annotation.StringRemixMethod",
    "com.google.android.libraries.remixer.annotation.RemixerInstance"
    })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RemixerAnnotationProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private ErrorReporter errorReporter;
  private Filer filer;
  private Types typeUtils;

  private Map<String, AnnotatedClass> annotatedClasses;
  private Map<Class<? extends Annotation>, Class<?>> annotationToParameter;
  private Set<String> alreadyProcessedClasses = new HashSet<>();

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    annotationToParameter = new HashMap<>();
    annotationToParameter.put(BooleanRemixMethod.class, Boolean.class);
    annotationToParameter.put(RangeRemixMethod.class, Integer.class);
    annotationToParameter.put(StringListRemixMethod.class, String.class);
    annotationToParameter.put(StringRemixMethod.class, String.class);

    elementUtils = processingEnv.getElementUtils();
    errorReporter = new ErrorReporter(processingEnv);
    filer = processingEnv.getFiler();
    typeUtils = processingEnv.getTypeUtils();
    annotatedClasses = new HashMap<>();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      // First process RemixerInstance annotations.
      findRemixerInstances(roundEnv);
      findRemixAnnotations(roundEnv);
      for (Map.Entry<String, AnnotatedClass> classEntry: annotatedClasses.entrySet()) {
        if (!alreadyProcessedClasses.contains(classEntry.getKey())) {
          JavaFile file = classEntry.getValue().generateJavaFile();
          file.writeTo(filer);
          alreadyProcessedClasses.add(classEntry.getKey());
        }
      }
      return true;
    } catch (RemixerAnnotationException ex) {
      errorReporter.reportError(ex.getElement(), ex.getMessage());
      return false;
    } catch (IOException ex) {
      errorReporter.reportError(null, "Couldn't write file: " + ex.getMessage());
      return false;
    }
  }

  /**
   * Finds all {@link RemixerInstance} annotations.
   * Makes sure that there is only one of those per class and that they are applied to the right
   * field (public/default access of type {@link Remixer}.
   */
  private void findRemixerInstances(RoundEnvironment roundEnv) throws RemixerAnnotationException {
    for (Element instance : roundEnv.getElementsAnnotatedWith(RemixerInstance.class)) {
      // We know these are field elements since RemixerInstances only apply to those.
      checkPublicOrDefault(instance);
      checkElementType(instance, Remixer.class.getCanonicalName());
      TypeElement clazz = (TypeElement) instance.getEnclosingElement();
      String className = clazz.getQualifiedName().toString();
      if (annotatedClasses.containsKey(className)) {
        throw new RemixerAnnotationException(
            instance, "There can only be one @RemixerInstance per class");
      }
      annotatedClasses.put(
          className,
          new AnnotatedClass(clazz, (VariableElement) instance));
    }
  }

  /**
   * Finds all *RemixMethod annotations in the code.
   *
   * <p>Makes sure that they are applied to the right method (non-constructor, non-static,
   * public/default access, and that only take one parameter of the right type).
   */
  private void findRemixAnnotations(RoundEnvironment roundEnv) throws RemixerAnnotationException {
    for (Map.Entry<Class<? extends Annotation>, Class<?>> mapping :
        annotationToParameter.entrySet()) {
      for (Element element : roundEnv.getElementsAnnotatedWith(mapping.getKey())) {
        // We know these are Executable elements since RemixerInstances only apply to those.
        ExecutableElement method = (ExecutableElement) element;
        checkPublicOrDefault(method);
        checkMethod(method);
        checkParameter(method, mapping.getValue());
        TypeElement clazz = (TypeElement) method.getEnclosingElement();
        String className = clazz.getQualifiedName().toString();
        if (!annotatedClasses.containsKey(className)) {
          throw new RemixerAnnotationException(
              element,
              "Remix annotations REQUIRE a @RemixerInstance annotated field in the same class");
        }
        Annotation annotation = method.getAnnotation(mapping.getKey());
        annotatedClasses.get(className)
            .addMethod(getMethodAnnotation(clazz, method, annotation));
      }
    }
  }

  /**
   * Returns a {@link MethodAnnotation} object that represents one single MethodAnnotation found in
   * the code. This is used later to generate the code.
   * @throws RemixerAnnotationException Any semantic error or usage of an unimplemented annotation.
   */
  private MethodAnnotation getMethodAnnotation(
      TypeElement clazz, ExecutableElement method, Annotation annotation)
      throws RemixerAnnotationException {
    if (annotation instanceof BooleanRemixMethod) {
      return new BooleanRemixMethodAnnotation(clazz, method, (BooleanRemixMethod) annotation);
    }
    if (annotation instanceof RangeRemixMethod) {
      return new RangeRemixMethodAnnotation(clazz, method, (RangeRemixMethod) annotation);
    }
    if (annotation instanceof StringListRemixMethod) {
      return new StringListRemixMethodAnnotation(clazz, method, (StringListRemixMethod) annotation);
    }
    if (annotation instanceof StringRemixMethod) {
      return new StringRemixMethodAnnotation(clazz, method, (StringRemixMethod) annotation);
    }
    throw new RemixerAnnotationException(method,
        "Using an unimplemented Method annotation. Should never happen!");
  }

  /**
   * Checks that {@code method} has only one parameter of type {@code clazz}.
   */
  private void checkParameter(ExecutableElement method, Class<?> clazz)
      throws RemixerAnnotationException {
    if (method.getParameters().size() != 1) {
      throw new RemixerAnnotationException(
          method, "Remix annotations can only be used on methods with only one parameter");
    }
    VariableElement parameter = method.getParameters().get(0);
    try {
      checkElementType(parameter, clazz.getCanonicalName());
    } catch (RemixerAnnotationException ex) {
      // Throw the same exception with better message.
      throw new RemixerAnnotationException(
          parameter,
          String.format("Trying to use Remix annotations on wrong parameter, must be of type %s",
              clazz));
    }
  }

  /**
   * Checks that {@code element} is a method (not a constructor) that isn't abstract or static.
   */
  private void checkMethod(ExecutableElement element) throws RemixerAnnotationException {
    if (element.getKind() != ElementKind.METHOD
        || element.getModifiers().contains(Modifier.ABSTRACT)
        || element.getModifiers().contains(Modifier.STATIC)) {
      throw new RemixerAnnotationException(
          element,
          "Remix annotations can only be used on non-abstract, non-static, regular methods");
    }
  }

  /**
   * Checks that {@code element} is a variable with type {@code clazz}.
   */
  private void checkElementType(Element element, String clazz)
      throws RemixerAnnotationException {
    TypeMirror typeToCompare = elementUtils.getTypeElement(clazz).asType();
    TypeMirror elementType = element.asType();
    if (!typeUtils.isSubtype(elementType, typeToCompare)) {
      throw new RemixerAnnotationException(element,
          String.format(Locale.getDefault(),
              "Element must be of type %s to use Remixer Annotations", clazz));
    }
  }

  /**
   * Checks that the access to {@code element} is either public or default so it can be accessed
   * from another class in the same package.
   */
  private void checkPublicOrDefault(Element element) throws RemixerAnnotationException {
    Set<Modifier> modifiers = element.getModifiers();
    if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.PROTECTED)) {
      throw new RemixerAnnotationException(element,
          "Remix annotations can only be used on public/default elements");
    }
  }
}
