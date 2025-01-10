package io.github.singlerr.im.client.mixins;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

  @Inject(method = "use", at = @At("HEAD"))
  private void ignoreArrowItem(Level level, Player player, InteractionHand interactionHand,
                               CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                               @Share("instaBuild")
                               LocalBooleanRef ref) {
    if (!player.getAbilities().instabuild) {
      player.getAbilities().instabuild = true;
      ref.set(true);
    } else {
      ref.set(false);
    }
  }

  @Inject(method = "use", at = @At("RETURN"))
  private void resetState(Level level, Player player, InteractionHand interactionHand,
                          CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                          @Share("instabuild") LocalBooleanRef ref) {
    if (ref.get()) {
      player.getAbilities().instabuild = false;
    }
  }
}
