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

import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.ui.R;
import com.google.android.libraries.remixer.ui.gesture.Direction;
import com.google.android.libraries.remixer.ui.gesture.GestureListener;
import com.google.android.libraries.remixer.ui.gesture.ShakeListener;

/**
 * A fragment that shows all Remixes for the current activity. It's very easy to use:
 *
 * <pre><code>
 * class MyActivity extends FragmentActivity {
 *   // ...
 *
 *   protected void onCreate(Bundle savedInstanceState) {
 *     // ...
 *     RemixerFragment remixerFragment = RemixerFragment.newInstance();
 *     // Attach it to a button.
 *     remixerFragment.attachToButton(this, button);
 *     // Have remixer show up on 3 finger swipe up.
 *     remixerFragment.attachToGesture(this, Direction.UP, 3);
 *   }
 * }
 * </code></pre>
 */
public class RemixerFragment extends BottomSheetDialogFragment {

  public static final String REMIXER_TAG = "Remixer";

  private Remixer remixer;
  private ShakeListener shakeListener;

  public RemixerFragment() {
    remixer = Remixer.getInstance();
  }

  public static RemixerFragment newInstance() {
    return new RemixerFragment();
  }

  private boolean isAddingFragment = false;
  private final Object syncLock = new Object();

  private SensorEventListener sensorEventListener;

  /**
   * Attach this instance to {@code button}'s OnClick, so that clicking the button shows this
   * fragment.
   *
   * <p><b>Notice this will replace the button's OnClickListener</b>
   */
  public void attachToButton(final FragmentActivity activity, Button button) {
    button.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        showRemixer(activity.getSupportFragmentManager(), REMIXER_TAG);
      }
    });
  }

  /**
   *
   * @param manager
   * @param tag
   * @return whether the fragment was shown or not.
   */

  public void showRemixer(FragmentManager manager, String tag) {
    synchronized(syncLock) {
      if (!isAddingFragment && !isAdded()) {
        isAddingFragment = true;
        show(manager, tag);
      }
    }
  }

  // TODO(nicksahler): Generalize to attaching to any SensorEventListener
  /**
   * Attach this instance to a shake gesture and show fragment when magnitude exceeds {@code threshold}
   */
  public void attachToShake(final FragmentActivity activity, final double threshold) {
    shakeListener = new ShakeListener(activity, threshold, this);
    shakeListener.attach();
  }

  /**
   * Detach from a shake gesture
   */
  public void detachFromShake() {
    shakeListener.detach();
    shakeListener = null;
  }

  /**
   * Attach this instance to a swipe gesture with {@code numberOfFingers} numbers in direction
   * {@code direction} on the {@code activity}, so that performing the gesture will show this
   * fragment.
   *
   * <p><b>Notice this will replace the activity's root view's OnTouchListener</b>
   */
  public void attachToGesture(
      FragmentActivity activity, Direction direction, int numberOfFingers) {
    GestureListener.attach(activity, direction, numberOfFingers, this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_remixer_list, container, false);
    ImageView closeButton = (ImageView) view.findViewById(R.id.closeButton);
    closeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(RemixerFragment.this);
        transaction.commit();
      }
    });
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.remixerList);
    recyclerView.setAdapter(
        new RemixerAdapter(remixer.getVariablesWithContext(getActivity())));
    return view;
  }

  @Override
  public void onResume() {
    isAddingFragment = false;
    super.onResume();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }
}
