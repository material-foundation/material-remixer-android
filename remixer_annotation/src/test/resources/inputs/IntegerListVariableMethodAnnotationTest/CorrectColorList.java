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

package somepackage;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.annotation.IntegerListVariableMethod;

public class CorrectColorList {

  /**
   * Default value is implicitly 0, this has to move to 1
   */
  @IntegerListVariableMethod(possibleValues = {1, 2}, defaultValue = 1, isColor = true)
  public void setColor(Integer i){}
}
