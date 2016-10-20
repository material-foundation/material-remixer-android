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

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MenuActivity extends AppCompatActivity {

  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    listView = (ListView) findViewById(R.id.exampleList);
    final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_text);
    adapter.add(getResources().getString(R.string.mainDemoName));
    adapter.add(getResources().getString(R.string.boxDemoName));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        switch (i) {
          case 0:
            intent.setClass(getApplicationContext(), MainActivity.class);
            break;
          default:
            intent.setClass(getApplicationContext(), BoxActivity.class);
            break;
        }
        startActivity(intent);
      }
    });
    listView.setAdapter(adapter);
  }
}
