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
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.annotation.RemixerInstance;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * A class that has members annotated with any of the
 * {@link com.google.android.libraries.remixer.annotation} annotations.
 *
 * <p>Every instance of this object keeps track of all methods annotated with @*Method annotations
 * and the name of the {@link Remixer} instance annotated with {@link RemixerInstance}. It assumes
 * that there is only one such field in this class.
 *
 * <p>While adding methods, this also makes sure that their remix keys are not duplicated.
 */
public class AnnotatedClass {

  /**
   * The {@link RemixerBinder} class' name. this is useful for inheritance.
   */
  public static final ClassName REMIXER_BINDER_CLASS_NAME =
      ClassName.get(RemixerBinder.Binder.class);
  /**
   * The class being compiled that has Remixer annotations.
   */
  private final TypeElement sourceClass;
  /**
   * Class name for the source class.
   */
  private final TypeName sourceClassName;
  /**
   * The name of the class that to generate to contain all the code related to Remixer annotations.
   */
  private final String generatedClassName;
  /**
   * The package name to use for the generated class. This *must* match the package for
   * {@code sourceClass}.
   */
  private final String packageName;
  /**
   * The name of the {@code Remixer} instance that is annotated {@code @RemixerInstance}.
   */
  private final Name remixerName;
  /**
   * A set of all the used Remix Keys. Used for guaranteeing that there is no duplication of keys.
   */
  private final Set<String> remixKeys;
  /**
   * A mapping from method to annotation, this is used later for sorting and to make sure one method
   * is only annotated once.
   */
  private HashMap<ExecutableElement, MethodAnnotation> methodMap;

  /**
   * Constructor
   * @param sourceClass The TypeElement that represent the class that contains members annotated by
   *     Remixer annotations.
   * @param remixerInstance The field that is annotated with @RemixerInstance.
   */
  public AnnotatedClass(TypeElement sourceClass, VariableElement remixerInstance) {
    this.sourceClass = sourceClass;
    this.sourceClassName = ClassName.get(sourceClass.asType());
    generatedClassName = sourceClass.getSimpleName() + "_RemixerBinder";
    remixerName = remixerInstance.getSimpleName();
    packageName =
        ((PackageElement) sourceClass.getEnclosingElement()).getQualifiedName().toString();
    methodMap = new HashMap<>();
    remixKeys = new HashSet<>();
  }

  /**
   * Adds a method annotated by a Remixer annotation
   * @throws RemixerAnnotationException when a key is repeated among Remixer annotations in the same
   *     class.
   */
  public void addMethod(MethodAnnotation method) throws RemixerAnnotationException {
    if (!method.getSourceClass().equals(sourceClass)) {
      throw new RemixerAnnotationException(method.getSourceMethod(),
          "Adding a method annotation to the wrong class, shouldn't happen. File a bug.");
    }
    if (remixKeys.contains(method.getKey())) {
      throw new RemixerAnnotationException(method.getSourceMethod(),
          "Repeated Remix key, there can only be one with the same name in the same class");
    }
    if (methodMap.containsKey(method.getSourceMethod())) {
      throw new RemixerAnnotationException(method.getSourceMethod(),
          "Method can only be annotated once.");
    }
    remixKeys.add(method.getKey());
    // Put in a map, these will be sorted at the time of generating the source code
    methodMap.put(method.getSourceMethod(), method);
  }

  /**
   * Generates a Java file with the code corresponding to all Remixer annotations in this class.
   */
  public JavaFile generateJavaFile() throws RemixerAnnotationException {
    Collection<MethodAnnotation> annotatedMethods = sortMethods();
    TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName);
    ParameterizedTypeName superclass =
        ParameterizedTypeName.get(REMIXER_BINDER_CLASS_NAME, sourceClassName);
    classBuilder
        .superclass(superclass)
        .addJavadoc("This class was generated by RemixerAnnotationProcessor");

    // Create bind method signature
    MethodSpec.Builder bindMethodBuilder = MethodSpec
        .methodBuilder("bindInstance")
        .addParameter(ClassName.get(sourceClass), "activity")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC);

    // Initialize "remixer" object, reuse the activity's object if it is not null.
    bindMethodBuilder
        .addStatement("$T remixer", Remixer.class)
        .beginControlFlow("if (activity.$N == null)", remixerName)
        .addStatement("remixer = new $T()",  Remixer.class)
        .nextControlFlow("else")
        .addStatement("remixer = activity.$N", remixerName)
        .endControlFlow();
    for (MethodAnnotation method : annotatedMethods) {
      // Create all of the internal callback classes
      classBuilder.addType(method.generateCallbackClass());
      // Add them to the bind method.
      method.addSetupStatements(bindMethodBuilder);
    }
    // Copy remixer into the activity's remixer instance.
    bindMethodBuilder.addStatement("activity.$N = remixer", remixerName);
    classBuilder.addMethod(bindMethodBuilder.build());

    return JavaFile.builder(packageName, classBuilder.build()).build();
  }

  /**
   * Sorts all the methods by appearance in the source code file.
   *
   * <p>Not only this generates predictable output for tests, but this is necessary for the user to
   * have any control over the order of remixes being added.
   */
  private Collection<MethodAnnotation> sortMethods() throws RemixerAnnotationException {
    List<MethodAnnotation> annotatedMethods = new ArrayList<>();
    List<? extends Element> enclosedElements = sourceClass.getEnclosedElements();
    for (Element element : enclosedElements) {
      if (element instanceof ExecutableElement) {
        ExecutableElement currentMethod = (ExecutableElement) element;
        if (methodMap.containsKey(currentMethod)) {
          annotatedMethods.add(methodMap.get(currentMethod));
        }
      }
    }
    return annotatedMethods;
  }
}
