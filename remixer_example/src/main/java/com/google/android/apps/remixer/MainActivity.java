package com.google.android.apps.remixer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.libraries.remixer.BooleanRemixBuilder;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.StringRemixBuilder;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.ui.gesture.Direction;
import com.google.android.libraries.remixer.ui.view.RemixerActivity;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * Main activity with explicit instantiation or Remixer Objects.
 *
 * <p>Notice implementing RemixerActivity is necessary to use RemixerFragment.
 */
public class MainActivity extends AppCompatActivity implements RemixerActivity {

  // A text view whose text is updated by an ItemListRemix<String> and font size by a RangeRemix
  private TextView boundedText;
  // A text view whose text is updated by a StringRemix and is visible depending on a BooleanRemix
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
    remixer = Remixer.getInstance();

    ItemListRemix<Integer> colorRemix = new ItemListRemix.Builder<Integer>()
        .setKey("color")
        .setPossibleValues(Color.DKGRAY, Color.LTGRAY, Color.MAGENTA, Color.CYAN)
        .setCallback(new RemixCallback<Integer>() {
          @Override
          public void onValueSet(Remix<Integer> remix) {
            boundedText.setTextColor(remix.getSelectedValue());
            freeformText.setTextColor(remix.getSelectedValue());
          }
        })
        .setLayoutId(R.layout.color_list_remix_widget)
        .buildAndInit();
    remixer.addItem(colorRemix);

    // Create a RangeRemix that updates boundedText's size between 10 and 48 sp.
    RangeRemix.Builder fontSizeRangeRemix = new RangeRemix.Builder()
        .setKey("font_size")
        .setMinValue(16)
        .setMaxValue(72)
        .setIncrement(4)
        .setCallback(new RemixCallback<Integer>() {
          @Override
          public void onValueSet(Remix<Integer> remix) {
            boundedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, remix.getSelectedValue());
          }
        });
    remixer.addItem(fontSizeRangeRemix.buildAndInit());

    // Create an ItemListRemix<String> that updates boundedText's contents from a list of options
    ItemListRemix.Builder<String> itemListRemix = new ItemListRemix.Builder<String>()
        .setKey("boundedText")
        .setPossibleValues("Hello world", "Foo", "Bar", "May the force be with you")
        .setCallback(
            new RemixCallback<String>() {
              @Override
              public void onValueSet(Remix<String> remix) {
                boundedText.setText(remix.getSelectedValue());
              }
            });
    remixer.addItem(itemListRemix.buildAndInit());

    // Create a BooleanRemix that controls whether freeformText is visible or not.
    Remix.Builder<Boolean> booleanRemix = new BooleanRemixBuilder()
        .setKey("freeformTextDisplay")
        .setCallback(new RemixCallback<Boolean>() {
          @Override
          public void onValueSet(Remix<Boolean> remix) {
            freeformText.setVisibility(remix.getSelectedValue() ? View.VISIBLE : View.GONE);
          }
        });
    remixer.addItem(booleanRemix.buildAndInit());

    // Create a StringRemix that lets you set freeformText's content freely.
    Remix.Builder<String> freeformStringRemix = new StringRemixBuilder()
        .setKey("freeformText")
        .setDefaultValue("Change me!")
        .setCallback(new RemixCallback<String>() {
          @Override
          public void onValueSet(Remix<String> remix) {
            freeformText.setText(remix.getSelectedValue());
          }
        });
    remixer.addItem(freeformStringRemix.buildAndInit());

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

