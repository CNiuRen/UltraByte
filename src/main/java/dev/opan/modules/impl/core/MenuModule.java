package dev.opan.modules.impl.core;

import dev.opan.modules.Module;
import dev.opan.modules.RegisterModule;
import dev.opan.settings.impl.BooleanSetting;
import dev.opan.settings.impl.CategorySetting;

@RegisterModule(name = "Menu", description = "Replaces the default title screen with the client's custom main menu screen.", category = Module.Category.CORE, persistent = true, drawn = false)
public class MenuModule extends Module {
    public CategorySetting mainMenuCategory = new CategorySetting("MainMenu", "The category for settings related to the main menu.");
    public BooleanSetting mainMenu = new BooleanSetting("MainMenu", "Enabled", "Replaces Minecraft's default main menu with a customizable one.", new CategorySetting.Visibility(mainMenuCategory), true);
}
