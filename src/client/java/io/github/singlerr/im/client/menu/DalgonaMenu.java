package io.github.singlerr.im.client.menu;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.BitmapFactory;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import io.github.singlerr.im.Intermission;
import io.github.singlerr.im.client.IntermissionClient;
import io.github.singlerr.im.client.menu.views.DalgonaView;
import io.github.singlerr.im.client.network.PacketDalgonaResult;
import java.io.IOException;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@RequiredArgsConstructor
public final class DalgonaMenu extends Fragment {

  private final String dalgonaImagePath;
  private final int threshold;

  private DalgonaView dalgonaView;
  private Bitmap dalgonaImage;
  private Bitmap bufferImage;

  @Override
  public void onCreate(DataSet savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      BitmapFactory.Options opt = new BitmapFactory.Options();
      opt.inImmutable = false;
      dalgonaImage = BitmapFactory.decodeStream(
          Minecraft.getInstance().getResourceManager().open(new ResourceLocation(
              Intermission.ID, dalgonaImagePath)), opt);
      bufferImage = BitmapFactory.decodeStream(
          Minecraft.getInstance().getResourceManager().open(new ResourceLocation(
              Intermission.ID, dalgonaImagePath)), opt);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           DataSet savedInstanceState) {
    LinearLayout base = new LinearLayout(requireContext());
    base.setOrientation(LinearLayout.VERTICAL);
    base.setLayoutTransition(new LayoutTransition());
    TextView msg = new TextView(requireContext());
    msg.setTextSize(base.dp(80));
    msg.setGravity(Gravity.CENTER);
    msg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    msg.setTypeface(IntermissionClient.NANUM_FONT);
    Consumer<Boolean> resultHandler = (result) -> {
      if (result) {
        msg.setText("성공!");
      } else {
        msg.setText("실패!");
      }
      base.removeView(dalgonaView);
      base.addView(msg);
      base.postDelayed(() -> {
        Minecraft.getInstance().execute(() -> {
          Minecraft.getInstance().setScreen(null);
          ClientPlayNetworking.send(new PacketDalgonaResult(result));
        });
      }, 1500L);
    };
    dalgonaView = dalgonaView == null ?
        new DalgonaView(requireContext(), IntermissionClient.SCRATCH_SOUND_EVENT, dalgonaImage,
            bufferImage,
            new float[] {160, 85, 10, 255}, new float[] {255, 255, 255, 255},
            threshold, 10, resultHandler) : dalgonaView;
    {
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);
      params.setMargins(base.dp(8), base.dp(8), base.dp(8), base.dp(10));
      base.addView(dalgonaView, params);
    }
    return base;
  }

}
