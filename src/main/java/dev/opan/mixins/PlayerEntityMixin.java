package dev.opan.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.opan.UltraByte;
import dev.opan.events.impl.PlayerTravelEvent;
import dev.opan.modules.impl.movement.KeepSprintModule;
import dev.opan.modules.impl.movement.SafeWalkModule;
import dev.opan.modules.impl.movement.SpeedModule;
import dev.opan.modules.impl.movement.VelocityModule;
import dev.opan.modules.impl.player.ReachModule;
import dev.opan.utils.IMinecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IMinecraft {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "isPushedByFluids", at = @At("RETURN"))
    private boolean isPushedByFluids(boolean original) {
        if ((Object) this == mc.player && UltraByte.MODULE_MANAGER.getModule(VelocityModule.class).isToggled() && UltraByte.MODULE_MANAGER.getModule(VelocityModule.class).antiLiquidPush.getValue()) return false;
        return original;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", shift = At.Shift.AFTER))
    private void attack(CallbackInfo callbackInfo) {
        if (UltraByte.MODULE_MANAGER.getModule(KeepSprintModule.class).isToggled()) {
            float multiplier = 0.6f + 0.4f * UltraByte.MODULE_MANAGER.getModule(KeepSprintModule.class).motion.getValue().floatValue();
            mc.player.setVelocity(mc.player.getVelocity().x / 0.6 * multiplier, mc.player.getVelocity().y, mc.player.getVelocity().z / 0.6 * multiplier);
            mc.player.setSprinting(true);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void travel(Vec3d movementInput, CallbackInfo info) {
        PlayerTravelEvent event = new PlayerTravelEvent(movementInput);
        UltraByte.EVENT_HANDLER.post(event);

        if (event.isCancelled()) {
            move(MovementType.SELF, getVelocity());
            info.cancel();
        }
    }

    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void getBlockInteractionRange(CallbackInfoReturnable<Double> info) {
        if (UltraByte.MODULE_MANAGER.getModule(ReachModule.class).isToggled()) {
            info.setReturnValue(UltraByte.MODULE_MANAGER.getModule(ReachModule.class).amount.getValue().doubleValue());
        }
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void getEntityInteractionRange(CallbackInfoReturnable<Double> info) {
        if (UltraByte.MODULE_MANAGER.getModule(ReachModule.class).isToggled()) {
            info.setReturnValue(UltraByte.MODULE_MANAGER.getModule(ReachModule.class).amount.getValue().doubleValue());
        }
    }

    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    private void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        if (UltraByte.MODULE_MANAGER.getModule(SafeWalkModule.class).isToggled()) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void getMovementSpeed(CallbackInfoReturnable<Float> info) {
        if (UltraByte.MODULE_MANAGER.getModule(SpeedModule.class).isToggled() && UltraByte.MODULE_MANAGER.getModule(SpeedModule.class).mode.getValue().equalsIgnoreCase("Vanilla")) {
            info.setReturnValue(UltraByte.MODULE_MANAGER.getModule(SpeedModule.class).vanillaSpeed.getValue().floatValue());
        }
    }
}
