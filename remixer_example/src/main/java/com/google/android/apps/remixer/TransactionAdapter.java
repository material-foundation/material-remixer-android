/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 *
 */

package com.google.android.apps.remixer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.Calendar;
import java.util.Date;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

  private final TransactionItem[] items = TransactionItem.generateTransactions(30);
  private boolean iconsVisible = true;

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    TransactionItemView view =
        (TransactionItemView) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.transaction_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setData(items[position]);
  }

  @Override
  public int getItemCount() {
    return items.length;
  }

  public void setIconsVisible(boolean iconsVisible) {
    this.iconsVisible = iconsVisible;
  }
  class ViewHolder extends RecyclerView.ViewHolder {

    TransactionItemView itemView;

    public ViewHolder(TransactionItemView itemView) {
      super(itemView);
      this.itemView = itemView;
    }

    void setData(TransactionItem item) {
      itemView.setData(item, iconsVisible);
    }
  }

  public float getTotalSince(Date date) {
    float total = 0;
    for (TransactionItem transaction : items) {
      if (transaction.getDate().after(date)) {
        total += transaction.getAmount();
      }
    }
    return total;
  }

  public float getThisMonthTotal() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return getTotalSince(calendar.getTime());
  }

  public float getLastMonthTotal() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, -1);
    return getTotalSince(calendar.getTime());
  }

}
