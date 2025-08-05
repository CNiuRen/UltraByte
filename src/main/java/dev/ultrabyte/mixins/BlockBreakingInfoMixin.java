package dev.ultrabyte.mixins;

import dev.ultrabyte.UltraByte;
import dev.ultrabyte.events.impl.PlayerMineEvent;
import net.minecraft.entity.player.BlockBreakingInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBreakingInfo.class)
public class BlockBreakingInfoMixin {
    @Inject(method = "compareTo(Lnet/minecraft/entity/player/BlockBreakingInfo;)I", at = @At("HEAD"))
    private void compareTo(BlockBreakingInfo blockBreakingInfo, CallbackInfoReturnable<Integer> cir) {
        UltraByte.EVENT_HANDLER.post(new PlayerMineEvent(blockBreakingInfo.getActorId(), blockBreakingInfo.getPos()));
    }
}
