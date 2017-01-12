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
public class RangeVariableMethodAnnotationTest {

  private ArrayList<Processor> allProcessors;

  @Before
  public void setUpClass() {
    allProcessors = new ArrayList<>();
    allProcessors.add(new RemixerAnnotationProcessor());
  }

  @Test
  public void failsMethodWithWrongParameter() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RangeVariableMethodAnnotationTest/MethodWithWrongParameter.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("Float")
        .in(file);
  }

  @Test
  public void failsWrongRange() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RangeVariableMethodAnnotationTest/WrongRange.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("minValue cannot be greater than maxValue")
        .in(file);
  }

  @Test
  public void failsExplicitWrongDefault() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RangeVariableMethodAnnotationTest/ExplicitWrongDefault.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("initialValue was explicitly set out of bounds")
        .in(file);
  }

  @Test
  public void buildsAndFixesInitialValue() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RangeVariableMethodAnnotationTest/FixesInitialValue.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .compilesWithoutError()
        .and()
        .generatesSources(JavaFileObjects
            .forResource("outputs/RangeVariableMethodAnnotationTest/FixesInitialValue.java"));
  }

  @Test
  public void correct() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RangeVariableMethodAnnotationTest/Correct.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .compilesWithoutError()
        .and()
        .generatesSources(JavaFileObjects
            .forResource("outputs/RangeVariableMethodAnnotationTest/Correct.java"));
  }
}
