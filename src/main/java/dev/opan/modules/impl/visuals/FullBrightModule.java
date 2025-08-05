package dev.opan.modules.impl.visuals;

import dev.opan.events.SubscribeEvent;
import dev.opan.events.impl.PlayerUpdateEvent;
import dev.opan.modules.Module;
import dev.opan.modules.RegisterModule;
import dev.opan.settings.impl.ModeSetting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

@RegisterModule(name = "FullBright", description = "Gives you the ability to clearly see even when in the dark.", category = Module.Category.VISUALS)
public class FullBrightModule extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "The way that will be used to change the game's brightness.", "Gamma", new String[]{"Gamma", "Potion"});

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!mode.getValue().equalsIgnoreCase("Potion")) return;

        if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE));
        }
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        if (!mode.getValue().equalsIgnoreCase("Potion")) return;

        if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE));
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        if (!mode.getValue().equalsIgnoreCase("Potion")) return;

        if (mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    @Override
    public String getMetaData() {
        return mode.getValue();
    }
}
