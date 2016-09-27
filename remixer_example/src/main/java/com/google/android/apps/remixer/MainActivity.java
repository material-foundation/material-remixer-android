package com.google.android.apps.remixer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.Callback;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.ui.gesture.Direction;
import com.google.android.libraries.remixer.ui.view.RemixerActivity;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * Main activity with explicit instantiation of Remixer Objects.
 *
 * <p>Notice implementing RemixerActivity is necessary to use RemixerFragment.
 */
public class MainActivity extends AppCompatActivity implements RemixerActivity {

  // A text view whose text is updated by an ItemListVariable<String> and font size by a RangeVariable
  private TextView boundedText;
  // A text view whose text is updated by a StringVariable and is visible depending on a BooleanVariable
  private TextView freeformText;
  // The remixer instance
  private Remixer remixer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Find all UI widgets
    Button remixerButton = (Button) findViewById(R.id.button);
    boundedText = (TextView) findViewById(R.id.boundedText);
    freeformText = (TextView) findViewById(R.id.freeformText);
    // Initialize the remixer instance
    remixer = new Remixer();

    ItemListVariable.Builder<Integer> colorVariable = new ItemListVariable.Builder<Integer>()
        .setKey("color")
        .setPossibleValues(new Integer[] {Color.DKGRAY, Color.LTGRAY, Color.MAGENTA, Color.CYAN} )
        .setCallback(new Callback<Integer>() {
          @Override
          public void onValueSet(Variable<Integer> variable) {
            boundedText.setTextColor(variable.getSelectedValue());
            freeformText.setTextColor(variable.getSelectedValue());
          }
        })
        .setLayoutId(R.layout.color_list_variable_widget);
    remixer.addItem(colorVariable.buildAndInit());

    // Create a RangeVariable that updates boundedText's size between 10 and 48 sp.
    RangeVariable.Builder fontSizeRangeVariable = new RangeVariable.Builder()
        .setKey("font_size")
        .setMinValue(16)
        .setMaxValue(72)
        .setIncrement(4)
        .setCallback(new Callback<Integer>() {
          @Override
          public void onValueSet(Variable<Integer> variable) {
            boundedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, variable.getSelectedValue());
          }
        });
    remixer.addItem(fontSizeRangeVariable.buildAndInit());

    // Create an ItemListVariable<String> that updates boundedText's contents from a list of options
    ItemListVariable.Builder<String> itemListVariable = new ItemListVariable.Builder<String>()
        .setKey("boundedText")
        .setPossibleValues(new String[] {"Hello world", "Foo", "Bar", "May the force be with you"})
        .setCallback(
            new Callback<String>() {
              @Override
              public void onValueSet(Variable<String> variable) {
                boundedText.setText(variable.getSelectedValue());
              }
            });
    remixer.addItem(itemListVariable.buildAndInit());

    // Create a BooleanVariable that controls whether freeformText is visible or not.
    Variable.Builder<Boolean> booleanVariable = new BooleanVariableBuilder()
        .setKey("freeformTextDisplay")
        .setCallback(new Callback<Boolean>() {
          @Override
          public void onValueSet(Variable<Boolean> variable) {
            freeformText.setVisibility(variable.getSelectedValue() ? View.VISIBLE : View.GONE);
          }
        });
    remixer.addItem(booleanVariable.buildAndInit());

    // Create a StringVariable that lets you set freeformText's content freely.
    Variable.Builder<String> freeformStringVariable = new StringVariableBuilder()
        .setKey("freeformText")
        .setDefaultValue("Change me!")
        .setCallback(new Callback<String>() {
          @Override
          public void onValueSet(Variable<String> variable) {
            freeformText.setText(variable.getSelectedValue());
          }
        });
    remixer.addItem(freeformStringVariable.buildAndInit());

    Trigger trigger = new Trigger("Toast", "toast", new Runnable() {
      @Override
      public void run() {
        String value = freeformText.getText().toString();
        Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
      }
    });
    remixer.addItem(trigger);

    RemixerFragment remixerFragment = RemixerFragment.newInstance();
    remixerFragment.attachToButton(this, remixerButton);
    remixerFragment.attachToGesture(this, Direction.UP, 3);
  }

  @Override
  public Remixer getRemixer() {
    return remixer;
  }
}

