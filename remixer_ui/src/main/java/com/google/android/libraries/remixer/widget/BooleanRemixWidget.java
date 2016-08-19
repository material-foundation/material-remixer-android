package com.google.android.libraries.remixer.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.libraries.remixer.BooleanRemix;
import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.widget.RemixWidget;

/**
 * Displays a {@link BooleanRemix} as a switch.
 */
public class BooleanRemixWidget extends RelativeLayout implements RemixWidget<BooleanRemix> {

  private TextView nameText;
  private Switch remixSwitch;

  public BooleanRemixWidget(Context context) {
    super(context);
  }

  public BooleanRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BooleanRemixWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    remixSwitch = (Switch) findViewById(R.id.booleanRemixSwitch);
    nameText = (TextView) findViewById(R.id.booleanRemixName);
  }

  @Override
  public void bindRemix(@NonNull final BooleanRemix remix) {
    nameText.setText(remix.getTitle());
    remixSwitch.setChecked(remix.getSelectedValue());
    remixSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        remix.setValue(b);
      }
    });
  }
}
