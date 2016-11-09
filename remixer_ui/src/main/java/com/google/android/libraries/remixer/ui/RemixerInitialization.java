package com.google.android.libraries.remixer.ui;

import android.app.Application;
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;

/**
 * Remixer initialization takes care of registering data types and registering activity lifecycle
 * callbacks for Remixer.
 */
public class RemixerInitialization {

  /**
   * Initializes a Remixer instance (usually {@link Remixer#getInstance()}) by registering data
   * types for it, and registers for ActivityLifecycleCallbacks with the Application {@code app}.
   *
   * <p>{@code app} can be null in case this is called from tests.
   */
  public static void initRemixer(Remixer remixer, Application app) {
    if (app != null) {
      app.registerActivityLifecycleCallbacks(RemixerCallbacks.getInstance());
    }

    // Boolean values only make sense in Variables, not in ItemListVariables or Range Variables.
    DataType.BOOLEAN.setLayoutIdForRemixerItemType(
        Variable.class, R.layout.boolean_variable_widget);
    remixer.registerDataType(DataType.BOOLEAN);

    // Color values are currently only supported in ItemListVariable. Support should be coming for
    // Variables. RangeVariable doesn't make sense for Color.
    DataType.COLOR.setLayoutIdForRemixerItemType(
        ItemListVariable.class, R.layout.boolean_variable_widget);
    remixer.registerDataType(DataType.COLOR);

    // Number values are only supported in ItemListVariable or RangeVariable
    DataType.NUMBER.setLayoutIdForRemixerItemType(
        ItemListVariable.class, R.layout.item_list_variable_widget);
    DataType.NUMBER.setLayoutIdForRemixerItemType(
        RangeVariable.class, R.layout.seekbar_range_variable_widget);
    remixer.registerDataType(DataType.NUMBER);

    // String values are supported in Variable and ItemListVariable. Range Variable doesn't quite
    // make sens
    DataType.STRING.setLayoutIdForRemixerItemType(
        ItemListVariable.class, R.layout.item_list_variable_widget);
    DataType.STRING.setLayoutIdForRemixerItemType(
        RangeVariable.class, R.layout.string_variable_widget);
    remixer.registerDataType(DataType.STRING);

    // Triggers are only supported in Trigger objects.
    DataType.TRIGGER.setLayoutIdForRemixerItemType(
        Trigger.class, R.layout.trigger_widget);
    remixer.registerDataType(DataType.TRIGGER);
  }
}
