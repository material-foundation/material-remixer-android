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
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RemixerAnnotationProcessor extends AbstractProcessor {

  private Elements elementUtils;
  private ErrorReporter errorReporter;
  private Filer filer;
  private Types typeUtils;

  private Set<String> alreadyProcessedClasses = new HashSet<>();

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    elementUtils = processingEnv.getElementUtils();
    errorReporter = new ErrorReporter(processingEnv);
    filer = processingEnv.getFiler();
    typeUtils = processingEnv.getTypeUtils();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> set = new HashSet<>();
    for (SupportedMethodAnnotation annotation : SupportedMethodAnnotation.values()) {
      set.add(annotation.getAnnotationType().getCanonicalName());
    }
    return set;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      Map<String, AnnotatedClass> annotatedClasses = new HashMap<>();
      findMethodAnnotations(roundEnv, annotatedClasses);
      for (Map.Entry<String, AnnotatedClass> classEntry : annotatedClasses.entrySet()) {
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
   * Finds all *Method annotations in the code.
   *
   * <p>Makes sure that they are applied to the right method (non-constructor, non-static,
   * public/default access, and that only take one parameter of the right type).
   */
  private void findMethodAnnotations(
      RoundEnvironment roundEnv, Map<String, AnnotatedClass> annotatedClasses)
      throws RemixerAnnotationException {
    for (SupportedMethodAnnotation annotationType : SupportedMethodAnnotation.values()) {
      for (Element element :
          roundEnv.getElementsAnnotatedWith(annotationType.getAnnotationType())) {
        // We know these are Executable elements since RemixerInstances only apply to those.
        ExecutableElement method = (ExecutableElement) element;
        checkPublicOrDefault(method);
        checkMethod(method);
        checkParameter(method, annotationType.getParameterClass());
        TypeElement clazz = (TypeElement) method.getEnclosingElement();
        String className = clazz.getQualifiedName().toString();
        if (!annotatedClasses.containsKey(className)) {
          annotatedClasses.put(className, new AnnotatedClass(clazz));
        }
        Annotation annotation = method.getAnnotation(annotationType.getAnnotationType());
        annotatedClasses.get(className)
            .addMethod(annotationType.getMethodAnnotation(clazz, method, annotation));
      }
    }
  }

  /**
   * Checks that {@code method} contains the right parameters.
   *
   * @param clazz Defines what the right parameters are for this method, if it's null then
   *     {@code method} must have no parameters, if it's not null then {@code method} must have
   *     one parameter of type {@code clazz}.
   */
  private void checkParameter(ExecutableElement method, Class<?> clazz)
      throws RemixerAnnotationException {
    int correctNumberOfParameters = clazz == null ? 0 : 1;
    if (method.getParameters().size() != correctNumberOfParameters) {
      throw new RemixerAnnotationException(
          method,
          String.format(
              Locale.getDefault(),
              "This method must have exactly %d parameter(s)",
              correctNumberOfParameters));
    }
    if (correctNumberOfParameters == 1) {
      VariableElement parameter = method.getParameters().get(0);
      try {
        checkElementType(parameter, clazz.getCanonicalName());
      } catch (RemixerAnnotationException ex) {
        // Throw the same exception with better message.
        throw new RemixerAnnotationException(
            parameter,
            String.format(
                Locale.getDefault(),
                "Trying to use Variable annotations on wrong parameter, must be of type %s",
                clazz));
      }
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
          "Variable annotations can only be used on non-abstract, non-static, regular methods");
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
          "Variable annotations can only be used on public/default elements");
    }
  }
}
