package io.github.singlerr.im.client.network;

import io.github.singlerr.im.Intermission;
import lombok.Data;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Data
public final class PacketIntermissionRequest implements FabricPacket {

    public static final PacketType<PacketIntermissionRequest> TYPE =
            PacketType.create(new ResourceLocation(
                    Intermission.ID, "request_intermission"), PacketIntermissionRequest::new);

    private long duration;
    private float startAngle;
    private float sweepAngle;

    public PacketIntermissionRequest(FriendlyByteBuf buf) {
        read(buf);
    }

    private void read(FriendlyByteBuf buf) {
        this.duration = buf.readLong();
        this.startAngle = buf.readFloat();
        this.sweepAngle = buf.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLong(duration);
        buf.writeFloat(startAngle);
        buf.writeFloat(sweepAngle);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
