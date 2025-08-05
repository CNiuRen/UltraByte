package dev.ultrabyte.modules.impl.player;

import dev.ultrabyte.UltraByte;
import dev.ultrabyte.events.SubscribeEvent;
import dev.ultrabyte.events.impl.PlayerUpdateEvent;
import dev.ultrabyte.modules.Module;
import dev.ultrabyte.modules.RegisterModule;
import dev.ultrabyte.settings.impl.ModeSetting;
import dev.ultrabyte.settings.impl.NumberSetting;
import dev.ultrabyte.utils.minecraft.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

@RegisterModule(name = "Replenish", description = "Automatically replenishes stacks in your hotbar with new ones when they meet a specified threshold.", category = Module.Category.PLAYER)
public class ReplenishModule extends Module {
    public ModeSetting switchMode = new ModeSetting("Switch", "The mode that will be used for switching items.", "Swap", new String[]{"Pickup", "Swap", "Quick"});
    public NumberSetting threshold = new NumberSetting("Threshold", "The minimum amount of items in a stack before that stack is replaced.", 12, 1, 64);
    public NumberSetting minimumCount = new NumberSetting("MinimumCount", "The minimum amount of items that should be in the new stack.", 48, 1, 64);

    private int ticks;

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (InventoryUtils.inInventoryScreen()) return;

        if (ticks <= 0) {
            for (int i = 0; i <= 8; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);

                if (!stack.isStackable()) continue;
                if (stack.getCount() > (int) ((threshold.getValue().floatValue() / 64.0f) * stack.getMaxCount())) continue;
                if (stack.isEmpty()) continue;

                int slot = InventoryUtils.findInventory(stack.getItem(), (int) ((minimumCount.getValue().intValue() / 64.0f) * stack.getMaxCount()));
                if (slot == -1) continue;

                if (switchMode.getValue().equalsIgnoreCase("Quick")) mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, InventoryUtils.indexToSlot(slot), 0, SlotActionType.QUICK_MOVE, mc.player);
                else InventoryUtils.swap(switchMode.getValue(), slot, i);

                ticks = 2 + UltraByte.SERVER_MANAGER.getPingDelay();
            }
        }

        ticks--;
    }

    @Override
    public String getMetaData() {
        return String.valueOf(threshold.getValue().intValue());
    }
}
