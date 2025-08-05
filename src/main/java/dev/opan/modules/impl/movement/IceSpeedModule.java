package dev.opan.modules.impl.movement;

import dev.opan.modules.Module;
import dev.opan.modules.RegisterModule;
import dev.opan.settings.impl.NumberSetting;

@RegisterModule(name = "IceSpeed", description = "Modifies your speed when walking on ice.", category = Module.Category.MOVEMENT)
public class IceSpeedModule extends Module {
    public NumberSetting speed = new NumberSetting("Speed", "The speed that will be applied when walking on ice.", 0.8f, 0.0f, 1.0f);

    @Override
    public String getMetaData() {
        return String.valueOf(speed.getValue().floatValue());
    }
}
