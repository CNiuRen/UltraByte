package dev.opan.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.opan.events.Event;
import net.minecraft.entity.player.PlayerEntity;

@Getter @AllArgsConstructor
public class PlayerPopEvent extends Event {
    private final PlayerEntity player;
    private final int pops;
}
