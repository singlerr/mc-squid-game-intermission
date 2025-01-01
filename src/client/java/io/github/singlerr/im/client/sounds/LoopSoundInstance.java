package io.github.singlerr.im.client.sounds;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class LoopSoundInstance extends AbstractSoundInstance {
  public LoopSoundInstance(SoundEvent soundEvent) {
    super(soundEvent, SoundSource.MASTER, SoundInstance.createUnseededRandom());
    looping = true;
  }
}
