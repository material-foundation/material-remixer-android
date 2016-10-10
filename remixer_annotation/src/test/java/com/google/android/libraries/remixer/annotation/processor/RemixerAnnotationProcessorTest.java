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
public class RemixerAnnotationProcessorTest {

  private ArrayList<Processor> allProcessors;

  @Before
  public void setUpClass() {
    allProcessors = new ArrayList<>();
    allProcessors.add(new RemixerAnnotationProcessor());
  }

  @Test
  public void failsOnNonPublicMethod() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RemixerAnnotationProcessorTest/NonPublicMethod.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("public/default")
        .in(file);
  }

  @Test
  public void failsOnMethodWithMultipleParameters() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RemixerAnnotationProcessorTest/MethodWithMultipleParameters.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("This method must have exactly 1 parameter(s)")
        .in(file);
  }

  @Test
  public void failsOnDuplicateKey() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RemixerAnnotationProcessorTest/DuplicateKey.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("Repeated Variable key")
        .in(file);
  }

  @Test
  public void failsOnInvalidIdentifierKey() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RemixerAnnotationProcessorTest/InvalidIdentifierKey.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .failsToCompile()
        .withErrorContaining("Invalid key used")
        .in(file);
  }

  @Test
  public void buildsCorrectFile() {
    JavaFileObject file = JavaFileObjects
        .forResource("inputs/RemixerAnnotationProcessorTest/Correct.java");
    Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
        .that(file)
        .processedWith(allProcessors)
        .compilesWithoutError()
        .and()
        .generatesSources(JavaFileObjects
            .forResource("outputs/RemixerAnnotationProcessorTest/Correct.java"));
  }
}
