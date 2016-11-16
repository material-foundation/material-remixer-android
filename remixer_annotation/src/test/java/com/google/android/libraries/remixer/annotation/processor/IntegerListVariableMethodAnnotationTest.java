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

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import java.util.ArrayList;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class IntegerListVariableMethodAnnotationTest {

  private ArrayList<Processor> allProcessors;

  @Before
  public void setUpClass() {
    allProcessors = new ArrayList<>();
    allProcessors.add(new RemixerAnnotationProcessor());
  }

  @Test
  public void failsMethodWithWrongParameter() {
    JavaFileObject file = JavaFileObjects
        .forResource(
            "inputs/IntegerListVariableMethodAnnotationTest/MethodWithWrongParameter.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("Integer")
        .in(file);
  }

  @Test
  public void failsExplicitWrongDefault() {
    JavaFileObject file = JavaFileObjects
        .forResource(
            "inputs/IntegerListVariableMethodAnnotationTest/ExplicitWrongDefault.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("Default value explicitly set to unknown value")
        .in(file);
  }

  @Test
  public void failsEmptyList() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/IntegerListVariableMethodAnnotationTest/EmptyList.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("List of possible values cannot be empty")
        .in(file);
  }

  @Test
  public void buildsCorrectColorList() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/IntegerListVariableMethodAnnotationTest/CorrectColorList.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .compilesWithoutError()
        .and()
        .generatesSources(JavaFileObjects
            .forResource("outputs/IntegerListVariableMethodAnnotationTest/CorrectColorList.java"));
  }

  @Test
  public void buildsAndFixesDefaultValue() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/IntegerListVariableMethodAnnotationTest/FixesDefaultValue.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .compilesWithoutError()
        .and()
        .generatesSources(JavaFileObjects
            .forResource("outputs/IntegerListVariableMethodAnnotationTest/FixesDefaultValue.java"));
  }
}
