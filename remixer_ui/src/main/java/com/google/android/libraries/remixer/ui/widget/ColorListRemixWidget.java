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

package com.google.android.libraries.remixer.ui.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.ui.R;
import com.google.android.libraries.remixer.ui.widget.color.ColorItem;
import com.google.android.libraries.remixer.ui.widget.color.SingleColorDrawable;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays an ItemListRemix&lt;Integer&gt; as a list of colors represented by the integer values.
 *
 * <p>After clicking the color, the corresponding value is set on the Remix, and selection is
 * indicated with a contrasting checkmark.
 */
public class ColorListRemixWidget extends LinearLayout
    implements RemixerItemWidget<ItemListRemix<Integer>> {

  private final Adapter adapter = new Adapter();
  private TextView nameText;

  public ColorListRemixWidget(Context context) {
    super(context);
  }

  public ColorListRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ColorListRemixWidget(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.colorList);
    recyclerView.setLayoutManager(
        new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
    nameText = (TextView) findViewById(R.id.remixName);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void bindRemixerItem(@NonNull final ItemListRemix<Integer> remix) {
    adapter.setRemix(remix);
    nameText.setText(remix.getTitle());
  }

  private static class ViewHolder extends RecyclerView.ViewHolder {

    private final ImageView view;
    private SingleColorDrawable drawable;

    ViewHolder(ImageView view, SingleColorDrawable drawable) {
      super(view);
      this.view = view;
      this.drawable = drawable;
    }

    void setColorItem(final ColorItem colorItem) {
      drawable.setColorItem(colorItem);
      view.invalidate();
    }
  }

  @VisibleForTesting
  static class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<ColorItem> values = new ArrayList<>();
    private ItemListRemix<Integer> remix;

    @VisibleForTesting
    List<ColorItem> getValues() {
      return values;
    }

    public void setRemix(ItemListRemix<Integer> remix) {
      this.remix = remix;
      values.clear();
      for (Integer color : remix.getValueList()) {
        ColorItem colorItem = new ColorItem(color, color.equals(remix.getSelectedValue()));
        values.add(colorItem);
      }
      notifyDataSetChanged();
    }

    @VisibleForTesting
    void selectColor(@ColorInt int color) {
      for (ColorItem item : values) {
        item.setSelected(item.getColor() == color);
      }
      remix.setValue(color);
      notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final ImageView imageView =
          (ImageView) LayoutInflater.from(parent.getContext())
              .inflate(R.layout.color_list_remix_item, parent, false);
      final SingleColorDrawable drawable = new SingleColorDrawable(parent.getContext());
      imageView.setImageDrawable(drawable);
      imageView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          selectColor(drawable.getColorItem().getColor());
        }
      });
      return new ViewHolder(imageView, drawable);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      holder.setColorItem(values.get(position));
    }

    @Override
    public int getItemCount() {
      return values.size();
    }
  }
}
