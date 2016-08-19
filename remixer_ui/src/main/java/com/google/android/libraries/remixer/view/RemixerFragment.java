package com.google.android.libraries.remixer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.Remixer;

/**
 * A fragment that shows all Remixes for the current activity. MUST only be instantiated from an
 * activity that implements {@link RemixerActivity}. It's very easy to use:
 *
 * <pre><code>
 * import com.google.android.libraries.remixer.view.RemixerActivity;
 * import com.google.android.libraries.remixer.view.RemixerFragment;
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
      recyclerView.setAdapter(new RemixerAdapter(remixer.getRemixList()));
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
