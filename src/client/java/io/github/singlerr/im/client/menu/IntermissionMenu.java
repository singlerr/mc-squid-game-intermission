package io.github.singlerr.im.client.menu;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import io.github.singlerr.im.client.IntermissionClient;
import io.github.singlerr.im.client.menu.views.RadialView;
import io.github.singlerr.im.client.network.PacketIntermissionResult;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Consumer;

@RequiredArgsConstructor
public final class IntermissionMenu extends Fragment {

    private final long duration;
    private final float startAngle;
    private final float angleAmount;
    private RadialView radialView;

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
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(
                        SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f));
            } else {
                msg.setText("실패!");
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(
                        SoundEvents.TURTLE_EGG_CRACK, 1.0f, 1.0f));
            }
            base.removeView(radialView);
            base.addView(msg);
            base.postDelayed(() -> {
                Minecraft.getInstance().execute(() -> {
                    Minecraft.getInstance().setScreen(null);
                    ClientPlayNetworking.send(new PacketIntermissionResult(result));
                });
            }, 500L);
        };

        radialView = new RadialView(requireContext(), duration, startAngle, angleAmount, resultHandler);
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
