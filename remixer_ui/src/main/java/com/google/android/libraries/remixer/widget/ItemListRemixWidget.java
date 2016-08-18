package com.google.android.libraries.remixer.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.widget.RemixWidget;

import java.util.List;

/**
 * Displays an ItemListRemix as a Spinner. The list uses item.toString() to display it as text.
 */
public class ItemListRemixWidget extends RelativeLayout
    implements RemixWidget<ItemListRemix<?>> {

  private TextView nameText;
  private Spinner spinner;
  private List<?> values;

  public ItemListRemixWidget(Context context) {
    super(context);
  }

  public ItemListRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ItemListRemixWidget(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    spinner = (Spinner) findViewById(R.id.itemListRemixSpinner);
    nameText = (TextView) findViewById(R.id.itemListRemixName);
  }

  @Override
  public void bindRemix(@NonNull final ItemListRemix remix) {
    values = remix.getValueList();
    int position = values.indexOf(remix.getSelectedValue());
    nameText.setText(remix.getTitle());
    spinner.setAdapter(
        new ArrayAdapter<>(getContext(), R.layout.item_list_spinner_view, values));
    spinner.setSelection(position);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      @Override
      @SuppressWarnings("unchecked")
      public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        remix.setValue(values.get(pos));
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
  }
}
