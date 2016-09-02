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

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.ui.R;

/**
 * A fragment that shows all Remixes for the current activity. MUST only be instantiated from an
 * activity that implements {@link RemixerActivity}. It's very easy to use:
 *
 * <pre><code>
 * import RemixerActivity;
 * import RemixerFragment;
 * import com.google.android.remixer.Remixer;
 *
 * class MyActivity implements RemixerActivity {
 *   Remixer remixer;
 *   RemixerFragment remixerFragment;
 *
 *   &#064;Override
 *   public Remixer getRemixer() {
 *     return remixer;
 *   }
 *
 *   // ...
 *
 *   void showRemixer() {
 *     if (remixerFragment == null) {
 *       remixerFragment = RemixerFragment.newInstance();
 *     }
 *     remixerFragment.show(getSupportFragmentManager(), "FragmentKey");
 *   }
 * }
 *
 * </code></pre>
 */
public class RemixerFragment extends BottomSheetDialogFragment {

  private Remixer remixer;

  public RemixerFragment() {
  }

  public static RemixerFragment newInstance() {
    return new RemixerFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    remixer = ((RemixerActivity) getActivity()).getRemixer();
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_remix_list, container, false);

    // Set the adapter
    if (view instanceof RecyclerView) {
      Context context = view.getContext();
      RecyclerView recyclerView = (RecyclerView) view;
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
      recyclerView.setAdapter(new RemixerAdapter(remixer.getRemixerItems()));
    }
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }
}
