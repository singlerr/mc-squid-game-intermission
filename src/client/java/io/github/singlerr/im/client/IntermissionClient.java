package io.github.singlerr.im.client;

import com.mojang.blaze3d.platform.InputConstants;
import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.im.Intermission;
import io.github.singlerr.im.client.menu.DalgonaMenu;
import io.github.singlerr.im.client.network.PacketDalgonaRequest;
import io.github.singlerr.im.client.network.PacketIntermissionRequest;
import io.github.singlerr.im.client.network.handler.PacketDalgonaRequestHandler;
import io.github.singlerr.im.client.network.handler.PacketIntermissionRequestHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class IntermissionClient implements ClientModInitializer {

  public static final KeyMapping OPEN_MENU =
      new KeyMapping("Open Menu", InputConstants.KEY_B, "Menu");
  public static final ResourceLocation SCRATCH_SOUND =
      new ResourceLocation(Intermission.ID, "scratch");
  public static final SoundEvent SCRATCH_SOUND_EVENT =
      SoundEvent.createVariableRangeEvent(SCRATCH_SOUND);

  public IntermissionClient() {
    KeyBindingHelper.registerKeyBinding(OPEN_MENU);
  }

  @Override
  public void onInitializeClient() {
    Registry.register(BuiltInRegistries.SOUND_EVENT, SCRATCH_SOUND, SCRATCH_SOUND_EVENT);
    ClientPlayNetworking.registerGlobalReceiver(PacketDalgonaRequest.TYPE,
        new PacketDalgonaRequestHandler());
    ClientPlayNetworking.registerGlobalReceiver(PacketIntermissionRequest.TYPE,
        new PacketIntermissionRequestHandler());
    ClientTickEvents.START_CLIENT_TICK.register(new ClientTickEvents.StartTick() {
      @Override
      public void onStartTick(Minecraft client) {
        if (OPEN_MENU.isDown()) {
          MuiFabricApi.openScreen(new DalgonaMenu("images/star.png"));
        }
      }
    });
  }
}
