package io.github.singlerr.im.client.menu;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import io.github.singlerr.im.client.menu.views.RadialView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class IntermissionMenu extends Fragment {

  private final long duration;
  private final float startAngle;
  private final float angleAmount;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           DataSet savedInstanceState) {
    LinearLayout base = new LinearLayout(requireContext());
    base.setOrientation(LinearLayout.VERTICAL);
    base.setLayoutTransition(new LayoutTransition());
    RadialView radialView = new RadialView(requireContext(), duration, startAngle, angleAmount);
    {
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);
      params.setMargins(base.dp(8), base.dp(10), base.dp(8), base.dp(10));
      base.addView(radialView, params);
    }
    return base;
  }

}
