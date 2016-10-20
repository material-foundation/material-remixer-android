package com.google.android.apps.remixer;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * This activity reuses one of the values from variables in MainActivity and it also uses the more
 * explicit (not-annotation-based) API for Remixer.
 *
 * <p>While this is supported we think most people would prefer to use the clearer, less verbose
 * annotation-based API.
 */
public class BoxActivity extends AppCompatActivity {

  // A title text whose text size is set by a variable.
  private TextView titleText;

  private ImageView box;
  // The remixer instance
  private Remixer remixer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_box);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Find all UI widgets
    titleText = (TextView) findViewById(R.id.titleText);
    box = (ImageView) findViewById(R.id.box);
    // Initialize the remixer instance
    remixer = Remixer.getInstance();

    // Create a RangeVariable that updates titleText's size between 16 and 72 sp. This reuses the
    // same value as the one in the MainActivity...
    RangeVariable.Builder fontSizeRangeVariable = new RangeVariable.Builder()
        .setKey("setTextSize")
        .setTitle("(Shared) title text size")
        .setParentObject(this)
        .setMinValue(16)
        .setMaxValue(72)
        .setIncrement(4)
        .setCallback(new Callback<Integer>() {
          @Override
          public void onValueSet(Variable<Integer> variable) {
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, variable.getSelectedValue());
          }
        });
    remixer.addItem(fontSizeRangeVariable.buildAndInit());

    // Create a BooleanVariable that controls whether the box is shown.
    Variable.Builder<Boolean> booleanVariable = new BooleanVariableBuilder()
        .setKey("showBox")
        .setTitle("Show Box")
        .setParentObject(this)
        .setCallback(new Callback<Boolean>() {
          @Override
          public void onValueSet(Variable<Boolean> variable) {
            box.setVisibility(variable.getSelectedValue() ? View.VISIBLE : View.GONE);
          }
        });
    remixer.addItem(booleanVariable.buildAndInit());

    // Create a list of colors to set for the box
    ItemListVariable.Builder<Integer> colorVariable = new ItemListVariable.Builder<Integer>()
        .setKey("setBoxColor")
        .setTitle("Box color")
        .setParentObject(this)
        .setPossibleValues(new Integer[] {Color.DKGRAY, Color.LTGRAY, Color.MAGENTA, Color.CYAN} )
        .setCallback(new Callback<Integer>() {
          @Override
          public void onValueSet(Variable<Integer> variable) {
            box.setBackgroundColor(variable.getSelectedValue());
          }
        })
        .setLayoutId(R.layout.color_list_variable_widget);
    remixer.addItem(colorVariable.buildAndInit());

    RemixerFragment remixerFragment = RemixerFragment.newInstance();
    remixerFragment.attachToGesture(this, Direction.UP, 3);
  }
}

