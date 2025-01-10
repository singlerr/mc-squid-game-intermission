package io.github.singlerr.im.client.network;

import io.github.singlerr.im.Intermission;
import java.util.UUID;
import lombok.AllArgsConstructor;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@AllArgsConstructor
public final class PacketRequestSync implements FabricPacket {

  public static final PacketType<PacketRequestSync> TYPE = PacketType.create(new ResourceLocation(
      Intermission.ID, "request_sync"), PacketRequestSync::new);

  private UUID playerId;

  public PacketRequestSync(FriendlyByteBuf buf) {
    read(buf);
  }

  private void read(FriendlyByteBuf buf) {
    this.playerId = buf.readUUID();
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeUUID(playerId);
  }

  @Override
  public PacketType<?> getType() {
    return TYPE;
  }
}
