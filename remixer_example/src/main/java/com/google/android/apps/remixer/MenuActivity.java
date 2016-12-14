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

package com.google.android.apps.remixer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

  private RecyclerView list;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    list = (RecyclerView) findViewById(R.id.exampleList);
    list.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
            LayoutInflater.from(MenuActivity.this).inflate(R.layout.list_text, null));
      }

      @Override
      public void onBindViewHolder(ViewHolder holder, int position) {
        int textId = position == 0 ? R.string.mainDemoName : R.string.boxDemoName;
        Class activityClass = position == 0 ? MainActivity.class : BoxActivity.class;
        holder.setContent(textId, activityClass);
      }

      @Override
      public int getItemCount() {
        return 2;
      }
    });
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Class activityClass;

    ViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
    }

    void setContent(@StringRes int string, Class activityClass) {
      ((TextView) itemView).setText(string);
      this.activityClass = activityClass;
    }

    @Override
    public void onClick(View view) {
      Intent intent = new Intent();
      intent.setClass(MenuActivity.this, activityClass);
      startActivity(intent);
    }
  }
}
