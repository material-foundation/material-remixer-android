package com.google.android.libraries.remixer.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.StringRemix;
import com.google.android.libraries.remixer.widget.RemixWidget;

/**
 * Displays a {@link StringRemix} in an EditText and lets you edit its value. The StringRemix must
 * not have a list of possible values to use this View.
 */
public class StringRemixWidget extends RelativeLayout implements RemixWidget<StringRemix> {

  private TextView nameText;
  private EditText text;

  public StringRemixWidget(Context context) {
    super(context);
  }

  public StringRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StringRemixWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    text = (EditText) findViewById(R.id.stringRemixText);
    nameText = (TextView) findViewById(R.id.stringRemixName);
  }

  @Override
  public void bindRemix(@NonNull final StringRemix remix) {
    nameText.setText(remix.getTitle());
    text.setText(remix.getSelectedValue());
    text.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        remix.setValue(s.toString());
      }
    });
  }
}
