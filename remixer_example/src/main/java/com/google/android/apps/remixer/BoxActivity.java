package com.google.android.apps.remixer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.ColorListVariableMethod;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.ui.gesture.Direction;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * This activity reuses one of the values from variables in MainActivity.
 */
public class BoxActivity extends AppCompatActivity {

  // A title text whose text size is set by a variable.
  private TextView titleText;
  // An ImageView that does nothing but draw a box with its background color.
  private ImageView box;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_box);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Find all UI widgets
    titleText = (TextView) findViewById(R.id.titleText);
    box = (ImageView) findViewById(R.id.box);
    // Initialize the remixer instance
    RemixerBinder.bind(this);
    RemixerFragment remixerFragment = RemixerFragment.newInstance();
    remixerFragment.attachToGesture(this, Direction.UP, 3);
  }

  @RangeVariableMethod(
      key = "titleTextSize", title = "(Shared) title text size",
      minValue = 16, maxValue = 72, increment = 4
  )
  public void setTitleTextSize(Float size) {
    titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
  }

  @BooleanVariableMethod(key = "showBox", title = "Show box")
  void showBox(Boolean show) {
    box.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @ColorListVariableMethod(
      key = "boxColor", title = "Box color",
      possibleValues = {Color.DKGRAY, Color.LTGRAY, Color.MAGENTA, Color.CYAN},
      layoutId = R.layout.color_list_variable_widget
  )
  void setBoxColor(Integer color) {
    box.setBackgroundColor(color);
  }
}

