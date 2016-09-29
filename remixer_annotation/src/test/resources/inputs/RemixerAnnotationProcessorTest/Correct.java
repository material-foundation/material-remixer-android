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
import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.IntegerListVariableMethod;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerInstance;
import com.google.android.libraries.remixer.annotation.StringListVariableMethod;
import com.google.android.libraries.remixer.annotation.StringVariableMethod;
import com.google.android.libraries.remixer.annotation.TriggerMethod;

/**
 * This ends up testing defaults for all remixes, and that sorting works.
 */
public class Correct {

  @RemixerInstance
  Remixer remixer;

  @BooleanVariableMethod
  public void setBoolean(Boolean i) {}

  @IntegerListVariableMethod(possibleValues = {1, 2, 3})
  public void setIntegerList(Integer i) {}

  @RangeVariableMethod
  public void setInt(Integer i) {}

  @StringVariableMethod
  public void setString(String i) {}

  @StringListVariableMethod(possibleValues = {"hello", "world"})
  public void setStringList(String i) {}

  @TriggerMethod
  public void pullTrigger() {}
}
