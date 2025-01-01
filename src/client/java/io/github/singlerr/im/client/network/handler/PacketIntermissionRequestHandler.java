package io.github.singlerr.im.client.network.handler;

import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.im.client.menu.IntermissionMenu;
import io.github.singlerr.im.client.network.PacketIntermissionRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

public final class PacketIntermissionRequestHandler
    implements ClientPlayNetworking.PlayPacketHandler<PacketIntermissionRequest> {
  @Override
  public void receive(PacketIntermissionRequest packet, LocalPlayer player,
                      PacketSender responseSender) {
    MuiFabricApi.openScreen(
        new IntermissionMenu(packet.getDuration(), packet.getStartAngle(), packet.getSweepAngle()));
  }
}
