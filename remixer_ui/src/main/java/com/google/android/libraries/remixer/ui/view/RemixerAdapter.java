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
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.ui.widget.RemixerWidget;
import java.util.List;

/**
 * An adapter that takes care of displaying a list of {@link Variable}s using their corresponding
 * {@link RemixerWidget}.
 */
class RemixerAdapter extends RecyclerView.Adapter<RemixerAdapter.ViewHolder> {

  private final List<Variable> variables;

  public RemixerAdapter(List<Variable> variables) {
    this.variables = variables;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(viewType, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.setVariable(variables.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    return RemixerWidgetHelper.getLayoutId(variables.get(position));
  }

  @Override
  public int getItemCount() {
    return variables.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private final RemixerWidget view;

    public ViewHolder(View view) {
      super(view);

      this.view = (RemixerWidget) view;
    }

    @SuppressWarnings("unchecked")
    public void setVariable(Variable variable) {
      view.bindVariable(variable);
    }
  }
}
