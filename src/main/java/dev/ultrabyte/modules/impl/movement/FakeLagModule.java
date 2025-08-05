package dev.ultrabyte.modules.impl.movement;

import dev.ultrabyte.events.SubscribeEvent;
import dev.ultrabyte.events.impl.PacketReceiveAsyncEvent;
import dev.ultrabyte.events.impl.PacketSendEvent;
import dev.ultrabyte.events.impl.TickEvent;
import dev.ultrabyte.modules.Module;
import dev.ultrabyte.modules.RegisterModule;
import dev.ultrabyte.settings.impl.NumberSetting;
import dev.ultrabyte.utils.minecraft.EntityUtils;
import dev.ultrabyte.utils.system.Timer;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

@RegisterModule(name = "FakeLag", description = "Chokes sent packets to look like you are lagging.", category = Module.Category.MOVEMENT)
public class FakeLagModule extends Module {
    public NumberSetting choke = new NumberSetting("Choke", "The delay to choke packets for.", 2, 1, 5);

    private final ArrayList<PlayerMoveC2SPacket> packets = new ArrayList<>();
    private final Timer timer = new Timer();
    private final Timer safety = new Timer();
    private boolean sending = false;

    @SubscribeEvent
    public void onPacketReceive(PacketReceiveAsyncEvent event) {
        if(getNull()) return;

        if(event.getPacket() instanceof PlayerPositionLookS2CPacket || event.getPacket() instanceof DisconnectS2CPacket || event.getPacket() instanceof HealthUpdateS2CPacket packet && packet.getHealth() <= 0) {
            sendPackets();
            safety.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (getNull() || sending || !shouldChoke() || !(event.getPacket() instanceof PlayerMoveC2SPacket packet)) return;

        synchronized (packets) {
            event.setCancelled(true);
            packets.add(packet);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(getNull() || packets.isEmpty()) return;

        if(timer.hasTimeElapsed((int) (choke.getValue().floatValue()*100))) {
            sendPackets();
            timer.reset();
        }
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        if(getNull()) return;
        sendPackets();
    }

    private void sendPackets() {
        synchronized (packets) {
            sending = true;
            for(PlayerMoveC2SPacket packet : packets) {
                mc.player.networkHandler.sendPacket(packet);
            }
            packets.clear();
            sending = false;
        }
    }

    private boolean shouldChoke() {
        return (EntityUtils.getSpeed(mc.player, EntityUtils.SpeedUnit.KILOMETERS) >= 5 || mc.player.fallDistance > 0) && safety.hasTimeElapsed(1000);
    }

    @Override
    public String getMetaData() {
        return (shouldChoke() ? Formatting.GREEN : Formatting.RED) + "Choke";
    }
}
