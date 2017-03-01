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

package com.google.android.libraries.remixer.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.storage.FirebaseRemoteControllerSyncer;
import com.google.android.libraries.remixer.ui.R;

/**
 * This drawer is displayed only when Remixer is using a {@link FirebaseRemoteControllerSyncer}
 * as a Synchronization Mechanism
 */
public class RemixerShareDrawer
    extends LinearLayout implements FirebaseRemoteControllerSyncer.SharingStatusListener {

  private Button shareLinkButton;
  private Switch sharingSwitch;
  private TextView sharingDetailText;
  private FirebaseRemoteControllerSyncer syncer;

  public RemixerShareDrawer(Context context) {
    super(context);
  }

  public RemixerShareDrawer(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RemixerShareDrawer(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void init() {
    sharingDetailText = (TextView) findViewById(R.id.sharingDetailText);
    sharingSwitch = (Switch) findViewById(R.id.sharingSwitch);
    shareLinkButton = (Button)findViewById(R.id.shareLinkButton);
    syncer = ((FirebaseRemoteControllerSyncer) Remixer.getInstance().getSynchronizationMechanism());
    syncer.addSharingStatusListener(this);
    sharingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
          syncer.startSharing();
        } else {
          syncer.stopSharing();
        }
      }
    });
    shareLinkButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        getContext().startActivity(syncer.getShareLinkIntent());
      }
    });
  }

  @Override
  public void updateSharingStatus(boolean sharing) {
    sharingSwitch.setChecked(sharing);
    if (sharing) {
      sharingSwitch.setText(R.string.sharingStatusOnText);
      sharingDetailText.setVisibility(View.VISIBLE);
      shareLinkButton.setVisibility(View.VISIBLE);
    } else {
      sharingSwitch.setText(R.string.sharingStatusOffText);
      sharingDetailText.setVisibility(View.GONE);
      shareLinkButton.setVisibility(View.GONE);
    }
  }
}
