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

import android.app.ActionBar;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.storage.FirebaseRemoteControllerSyncer;
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
public class RemixerFragment
    extends BottomSheetDialogFragment
    implements FirebaseRemoteControllerSyncer.SharingStatusListener {

  public static final String REMIXER_TAG = "Remixer";

  private Remixer remixer;
  private ShakeListener shakeListener;
  private boolean isNetworkBasedSync;
  private ImageView expandSharingOptionsButton;
  private Button sharedStatusButton;
  private RemixerAdapter adapter;
  private boolean isShowingDrawer = false;
  private RemixerShareDrawer shareDrawer;

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
    shareDrawer = (RemixerShareDrawer) view.findViewById(R.id.shareDrawer);
    isShowingDrawer = false;
    ShareDrawerOnClickListener listener = new ShareDrawerOnClickListener();
    expandSharingOptionsButton = (ImageView) view.findViewById(R.id.expandSharingOptionsButton);
    sharedStatusButton = (Button) view.findViewById(R.id.sharedStatusButton);
    expandSharingOptionsButton.setOnClickListener(listener);
    sharedStatusButton.setOnClickListener(listener);
    closeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        RemixerFragment.this.getFragmentManager().beginTransaction().remove(RemixerFragment.this).commit();
      }
    });
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.remixerList);
    adapter = new RemixerAdapter(remixer.getVariablesWithContext(getActivity()));
    recyclerView.setAdapter(adapter);
    return view;
  }

  @Override
  public void onResume() {
    isAddingFragment = false;
    isNetworkBasedSync =
        Remixer.getInstance().getSynchronizationMechanism()
            instanceof FirebaseRemoteControllerSyncer;
    if (isNetworkBasedSync) {
      expandSharingOptionsButton.setVisibility(View.VISIBLE);
      ((FirebaseRemoteControllerSyncer) Remixer.getInstance().getSynchronizationMechanism())
          .addSharingStatusListener(this);
      shareDrawer.init();
    }
    super.onResume();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void updateSharingStatus(boolean sharing) {
    sharedStatusButton.setVisibility(sharing ? View.VISIBLE : View.GONE);
  }

  private class ShareDrawerOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
      isShowingDrawer = !isShowingDrawer;
      if (isShowingDrawer) {
        expandSharingOptionsButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
        expandSharingOptionsButton.setContentDescription(
            getResources().getString(R.string.collapse_sharing_options_drawer));
        expandShareDrawer();
      } else {
        expandSharingOptionsButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
        expandSharingOptionsButton.setContentDescription(
            getResources().getString(R.string.expand_sharing_options_drawer));
        collapseShareDrawer();
      }
    }
  }

  private void collapseShareDrawer() {
    final int initialHeight = shareDrawer.getMeasuredHeight();
    shareDrawer.setVisibility(View.VISIBLE);
    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == 1) {
          shareDrawer.setVisibility(View.GONE);
        } else {
          shareDrawer.getLayoutParams().height =
              initialHeight - (int) (initialHeight * interpolatedTime);
          shareDrawer.requestLayout();
        }
      }

      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };
    // 225 ms is a good time for elements entering the screen.
    // https://material.io/guidelines/motion/duration-easing.html#duration-easing-common-durations
    a.setDuration(225);
    shareDrawer.startAnimation(a);
  }


  private void expandShareDrawer() {
    shareDrawer.measure(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    final int targetHeight = shareDrawer.getMeasuredHeight();
    shareDrawer.setVisibility(View.VISIBLE);
    // Workaround for API < 21
    shareDrawer.getLayoutParams().height = 1;
    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        shareDrawer.getLayoutParams().height =
            interpolatedTime == 1
                ? LinearLayout.LayoutParams.WRAP_CONTENT
                : (int) (targetHeight * interpolatedTime);
        shareDrawer.requestLayout();
      }

      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };
    // 195ms is a good time for elements leaving the screen.
    // https://material.io/guidelines/motion/duration-easing.html#duration-easing-common-durations
    a.setDuration(195);
    shareDrawer.startAnimation(a);
  }
}
