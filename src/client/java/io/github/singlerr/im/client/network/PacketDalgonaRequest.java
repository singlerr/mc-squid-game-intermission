package io.github.singlerr.im.client.network;

import io.github.singlerr.im.Intermission;
import lombok.Data;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Data
public final class PacketDalgonaRequest implements FabricPacket {

  public static final PacketType<PacketDalgonaRequest> TYPE =
      PacketType.create(new ResourceLocation(
          Intermission.ID, "dalgona_request"), PacketDalgonaRequest::new);

  private String dalgonaImagePath;

  public PacketDalgonaRequest(FriendlyByteBuf buf) {
    read(buf);
  }

  private void read(FriendlyByteBuf buf) {
    dalgonaImagePath = buf.readUtf();
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeUtf(dalgonaImagePath);
  }

  @Override
  public PacketType<?> getType() {
    return TYPE;
  }
}
