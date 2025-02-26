package io.github.singlerr.im.client;

import com.mojang.blaze3d.platform.InputConstants;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.text.Typeface;
import io.github.singlerr.im.Intermission;
import io.github.singlerr.im.client.network.PacketDalgonaRequest;
import io.github.singlerr.im.client.network.PacketIntermissionRequest;
import io.github.singlerr.im.client.network.PacketRequestSync;
import io.github.singlerr.im.client.network.handler.PacketDalgonaRequestHandler;
import io.github.singlerr.im.client.network.handler.PacketIntermissionRequestHandler;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.io.InputStream;

@Slf4j
public final class IntermissionClient implements ClientModInitializer {
    public static final KeyMapping OPEN_MENU =
            new KeyMapping("Open Menu", InputConstants.KEY_B, "Menu");
    public static final ResourceLocation SCRATCH_SOUND =
            new ResourceLocation(Intermission.ID, "scratch");
    public static final SoundEvent SCRATCH_SOUND_EVENT =
            SoundEvent.createVariableRangeEvent(SCRATCH_SOUND);
    public static Typeface NANUM_FONT;

    public IntermissionClient() {
        KeyBindingHelper.registerKeyBinding(OPEN_MENU);
    }

    @Override
    public void onInitializeClient() {

        ResourceManagerHelperImpl.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public ResourceLocation getFabricId() {
                        return new ResourceLocation("sgadminui", "client_resources");
                    }

                    @Override
                    public void onResourceManagerReload(ResourceManager resourceManager) {
                        try (InputStream in = Minecraft.getInstance().getResourceManager()
                                .open(new ResourceLocation("intermission", "font/nanum_square_neo.ttf"))) {
                            FontFamily font = FontFamily.createFamily(in, false);
                            NANUM_FONT = Typeface.createTypeface(font);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        ClientEntityEvents.ENTITY_LOAD.register(this::requestNameSync);
        Registry.register(BuiltInRegistries.SOUND_EVENT, SCRATCH_SOUND, SCRATCH_SOUND_EVENT);
        ClientPlayNetworking.registerGlobalReceiver(PacketDalgonaRequest.TYPE,
                new PacketDalgonaRequestHandler());
        ClientPlayNetworking.registerGlobalReceiver(PacketIntermissionRequest.TYPE,
                new PacketIntermissionRequestHandler());
    }

    private void requestNameSync(Entity entity, ClientLevel level) {
        if (!(entity instanceof Player player)) {
            return;
        }

        ClientPlayNetworking.send(new PacketRequestSync(player.getUUID()));
    }

}
