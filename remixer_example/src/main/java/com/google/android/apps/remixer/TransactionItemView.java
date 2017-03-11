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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.NumberFormat;

public class TransactionItemView extends RelativeLayout {

  private TextView amount;
  private ImageView businessTypeIcon;
  private TextView businessName;
  private TextView date;

  public TransactionItemView(Context context) {
    super(context);
  }

  public TransactionItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TransactionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    businessTypeIcon = (ImageView) findViewById(R.id.businessTypeIcon);
    businessName = (TextView) findViewById(R.id.businessName);
    date = (TextView) findViewById(R.id.date);
    amount = (TextView) findViewById(R.id.amount);
  }

  public void setData(TransactionItem data, boolean iconVisible) {
    businessTypeIcon.setImageResource(data.getBusinessTypeIconResource());
    businessTypeIcon.setVisibility(iconVisible ? View.VISIBLE : View.GONE);
    businessName.setText(data.getBusinessName());
    DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
    date.setText(dateFormat.format(data.getDate()));
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    amount.setText(numberFormat.format(data.getAmount()));
  }
}
