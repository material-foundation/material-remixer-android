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
import com.google.android.libraries.remixer.ui.R;
import com.google.android.libraries.remixer.ui.widget.RemixerWidget;
import java.util.List;

/**
 * An adapter that takes care of displaying a list of {@link Variable}es using their corresponding
 * {@link RemixerWidget}.
 */
class RemixerAdapter extends RecyclerView.Adapter<RemixerAdapter.ViewHolder> {

  private final List<Variable> variables;
  private boolean isShowingShareDrawer = false;

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
    if (!isShowingShareDrawer) {
      holder.setVariable(variables.get(position));
    } else if (position > 0) {
      holder.setVariable(variables.get(position - 1));
    } else {
      holder.initShareDrawer();
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (isShowingShareDrawer && position == 0) {
      return R.layout.remixer_share_drawer;
    } else if (isShowingShareDrawer) {
      return RemixerWidgetHelper.getLayoutId(variables.get(position - 1));
    } else {
      return RemixerWidgetHelper.getLayoutId(variables.get(position));
    }
  }

  @Override
  public int getItemCount() {
    if (isShowingShareDrawer) {
      return variables.size() + 1;
    } else {
      return variables.size();
    }
  }

  boolean toggleShareDrawer() {
    isShowingShareDrawer = !isShowingShareDrawer;
    // Notifying single item changes makes RecyclerView animate correctly.
    if (isShowingShareDrawer) {
      notifyItemInserted(0);
    } else {
      notifyItemRemoved(0);
    }
    return isShowingShareDrawer;
  }

  /**
   * A view holder that can contain either
   */
  class ViewHolder extends RecyclerView.ViewHolder {
    private RemixerWidget remixerWidget = null;
    private RemixerShareDrawer shareDrawer = null;

    ViewHolder(View view) {
      super(view);
      if (view instanceof RemixerWidget) {
        this.remixerWidget = (RemixerWidget) view;
      } else {
        this.shareDrawer = (RemixerShareDrawer) view;
      }
    }

    @SuppressWarnings("unchecked")
    void setVariable(Variable variable) {
      // Assume its a RemixerWidget if we're setting a Variable
      remixerWidget.bindVariable(variable);
    }

    void initShareDrawer() {
      shareDrawer.init();
    }
  }
}
