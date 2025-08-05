package dev.ultrabyte.modules.impl.movement;

import dev.ultrabyte.events.SubscribeEvent;
import dev.ultrabyte.events.impl.PacketSendEvent;
import dev.ultrabyte.events.impl.PlayerUpdateEvent;
import dev.ultrabyte.mixins.accessors.PlayerMoveC2SPacketAccessor;
import dev.ultrabyte.modules.Module;
import dev.ultrabyte.modules.RegisterModule;
import dev.ultrabyte.settings.impl.ModeSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@RegisterModule(name = "NoFall", description = "Prevents you from taking fall damage.", category = Module.Category.MOVEMENT)
public class NoFallModule extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "The method that will be used in preventing fall damage.", "Packet", new String[]{"Packet", "Grim"});

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (!isFalling()) return;

        if (mode.getValue().equalsIgnoreCase("Grim")) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 0.000000001, mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), false, mc.player.horizontalCollision));
            mc.player.onLanding();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (!mode.getValue().equalsIgnoreCase("Packet")) return;

        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && isFalling()) {
            ((PlayerMoveC2SPacketAccessor) packet).setOnGround(true);
        }
    }

    @Override
    public String getMetaData() {
        return mode.getValue();
    }

    private boolean isFalling() {
        return mc.player.fallDistance > 3f;
        //return mc.player.fallDistance > mc.player.getSafeFallDistance() && !mc.player.isOnGround() && !mc.player.isGliding();
    }
}
