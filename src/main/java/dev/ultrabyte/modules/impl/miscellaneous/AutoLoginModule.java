package dev.ultrabyte.modules.impl.miscellaneous;

import dev.ultrabyte.UltraByte;
import dev.ultrabyte.events.SubscribeEvent;
import dev.ultrabyte.events.impl.PacketReceiveAsyncEvent;
import dev.ultrabyte.modules.Module;
import dev.ultrabyte.modules.RegisterModule;
import dev.ultrabyte.settings.impl.StringSetting;
import dev.ultrabyte.utils.system.Timer;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

@RegisterModule(name = "AutoLogin", description = "Automatically logs in on cracked servers.", category = Module.Category.MISCELLANEOUS)
public class AutoLoginModule extends Module {
    public StringSetting password = new StringSetting("Password", "The password to use when logging in.", "password");

    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveAsyncEvent event) {
        if(getNull() || !timer.hasTimeElapsed(10000)) return;

        if(event.getPacket() instanceof GameMessageS2CPacket packet) {
            String s = packet.content().getString().toLowerCase();

            if(s.contains("/register")) {
                mc.getNetworkHandler().sendChatCommand("register " + password.getValue() + " " + password.getValue());
                UltraByte.CHAT_MANAGER.tagged("Registered successfully.", getName());
                timer.reset();
            } else if(s.contains("/login")) {
                mc.getNetworkHandler().sendChatCommand("login " + password.getValue());
                UltraByte.CHAT_MANAGER.tagged("Logged in as " + mc.getSession().getUsername() + ".", getName());
                timer.reset();
            }
        }
    }
}
