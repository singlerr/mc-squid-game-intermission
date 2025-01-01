package io.github.singlerr.im.client.network.handler;

import icyllis.modernui.mc.fabric.MuiFabricApi;
import io.github.singlerr.im.client.menu.DalgonaMenu;
import io.github.singlerr.im.client.network.PacketDalgonaRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

public final class PacketDalgonaRequestHandler
    implements ClientPlayNetworking.PlayPacketHandler<PacketDalgonaRequest> {
  @Override
  public void receive(PacketDalgonaRequest packet, LocalPlayer player,
                      PacketSender responseSender) {
    MuiFabricApi.openScreen(new DalgonaMenu(packet.getDalgonaImagePath()));
  }
}
