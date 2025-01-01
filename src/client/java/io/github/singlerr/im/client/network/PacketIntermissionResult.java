package io.github.singlerr.im.client.network;

import io.github.singlerr.im.Intermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Data
@AllArgsConstructor
public final class PacketIntermissionResult implements FabricPacket {

  public static final PacketType<PacketIntermissionResult> TYPE =
      PacketType.create(new ResourceLocation(
          Intermission.ID, "intermission_result"), PacketIntermissionResult::new);

  private boolean success;

  public PacketIntermissionResult(FriendlyByteBuf buf) {
    read(buf);
  }

  public void read(FriendlyByteBuf buf) {
    this.success = buf.readBoolean();
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBoolean(this.success);
  }

  @Override
  public PacketType<?> getType() {
    return TYPE;
  }
}
