package dev.opan.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.opan.UltraByte;
import dev.opan.modules.impl.movement.VelocityModule;
import dev.opan.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin implements IMinecraft {
    @WrapOperation(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;pullHookedEntity(Lnet/minecraft/entity/Entity;)V"))
    private void pushOutOfBlocks(FishingBobberEntity instance, Entity entity, Operation<Void> original) {
        if (entity == mc.player && UltraByte.MODULE_MANAGER.getModule(VelocityModule.class).isToggled() && UltraByte.MODULE_MANAGER.getModule(VelocityModule.class).antiFishingRod.getValue())
            return;

        original.call(instance, entity);
    }
}
