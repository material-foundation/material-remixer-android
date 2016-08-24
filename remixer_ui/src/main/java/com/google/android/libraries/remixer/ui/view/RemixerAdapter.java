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

package com.google.android.libraries.remixer.ui.view;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.ui.widget.RemixWidget;

import java.util.List;

/**
 * An adapter that takes care of displaying a list of {@link Remix}es using their corresponding
 * {@link RemixWidget}.
 */
class RemixerAdapter extends RecyclerView.Adapter<RemixerAdapter.ViewHolder> {

  private final List<Remix<?>> remixes;

  public RemixerAdapter(List<Remix<?>> items) {
    remixes = items;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(viewType, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.setRemix(remixes.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    return RemixWidgetHelper.getLayoutId(remixes.get(position));
  }

  @Override
  public int getItemCount() {
    return remixes.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private final RemixWidget view;

    public ViewHolder(View view) {
      super(view);

      this.view = (RemixWidget) view;
    }

    @SuppressWarnings("unchecked")
    public void setRemix(Remix remix) {
      view.bindRemix(remix);
    }
  }
}
