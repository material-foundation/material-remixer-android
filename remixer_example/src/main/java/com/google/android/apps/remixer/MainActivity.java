package com.google.android.apps.remixer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.libraries.remixer.BooleanRemix;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.StringRemix;
import com.google.android.libraries.remixer.ui.view.RemixerActivity;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

import java.util.Arrays;

// Implementing RemixerActivity is necessary to use RemixerFragment
public class MainActivity extends AppCompatActivity implements RemixerActivity {

  // A text view whose text is updated by an ItemListRemix<String> and font size by a RangeRemix
  private TextView boundedText;
  // A text view whose text is updated by a StringRemix and is visible depending on a BoleanRemix
  private TextView freeformText;
  // The remixer instance
  private Remixer remixer;
  // A fragment that is displayed with the Remixes.
  private RemixerFragment remixerFragment;

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

    // Create a RangeRemix that updates boundedText's size between 10 and 48 sp.
    RangeRemix fontSizeRangeRemix = new RangeRemix(
        "Font size in sp",
        "font_size",
        16 /* defaultValue */,
        10 /* minValue */,
        48 /* maxValue */,
        new RemixCallback<Integer>() {
          @Override
          public void onValueSet(Remix<Integer> remix) {
            boundedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, remix.getSelectedValue());
          }
        },
        R.layout.seekbar_range_remix_widget);
    remixer.addRemix(fontSizeRangeRemix);

    // Create an ItemListRemix<String> that updates boundedText's contents from a list of options
    ItemListRemix<String> itemListRemix = new ItemListRemix<String>(
        "Text",
        "text",
        "Hello World!",
        Arrays.asList("Hello World!", "Foo", "Bar", "Foobar"),
        new RemixCallback<String>() {
          @Override
          public void onValueSet(Remix<String> remix) {
            boundedText.setText(remix.getSelectedValue());
          }
        },
        R.layout.item_list_remix_widget
    );
    remixer.addRemix(itemListRemix);

    // Create a BooleanRemix that controls whether freeformText is visible or not.
    BooleanRemix booleanRemix = new BooleanRemix(
        "Show Freeform Text",
        "freeformTextDisplay",
        false,
        new RemixCallback<Boolean>() {
          @Override
          public void onValueSet(Remix<Boolean> remix) {
            freeformText.setVisibility(remix.getSelectedValue() ? View.VISIBLE : View.GONE);
          }
        },
        R.layout.boolean_remix_widget);
    remixer.addRemix(booleanRemix);

    // Create a StringRemix that lets you set freeformText's content freely.
    StringRemix freeformStringRemix = new StringRemix(
        "Free form text",
        "freeformText",
        "You can change this string",
        new RemixCallback<String>() {

          @Override
          public void onValueSet(Remix<String> remix) {
            freeformText.setText(remix.getSelectedValue());
          }
        },
        R.layout.string_remix_widget);
    remixer.addRemix(freeformStringRemix);

    // Add a callback to open the Remixer UI when the button is clicked.
    remixerButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        getRemixerFragment().show(getSupportFragmentManager(), "Remixer");
      }
    });
  }

  @Override
  public Remixer getRemixer() {
    return remixer;
  }

  @NonNull
  private RemixerFragment getRemixerFragment() {
    if (remixerFragment == null) {
      remixerFragment = RemixerFragment.newInstance();
    }
    return remixerFragment;
  }
}

