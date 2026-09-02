package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

import java.util.HashSet;
import java.util.Set;

public class ItemOptimizer {

    private final Set<Integer> culledItems = new HashSet<>();
    private int tickCounter = 0;

    public void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tickCounter++;
            // Реже сканируем дроп — раньше каждый 8 тик итерация по всем сущностям
            // грузила слабый CPU на серверах. Теперь 20 тиков (~1 сек).
            if (tickCounter % 20 != 0) return;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || mc.player == null) {
                culledItems.clear();
                return;
            }

            culledItems.clear();

            int limit = ModConfig.itemLimit;
            if (limit <= 0) limit = 300;

            int count = 0;
            double camX = mc.player.getX();
            double camY = mc.player.getY();
            double camZ = mc.player.getZ();

            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof ItemEntity)) continue;

                count++;
                if (count > limit) {
                    culledItems.add(entity.getEntityId());
                    continue;
                }

                double dx = entity.getX() - camX;
                double dy = entity.getY() - camY;
                double dz = entity.getZ() - camZ;
                int despawn = ModConfig.itemDespawnDistance;
                if (dx * dx + dy * dy + dz * dz > despawn * despawn) {
                    culledItems.add(entity.getEntityId());
                }
            }
        });
    }

    public boolean isItemCulled(ItemEntity item) {
        if (!ModConfig.optimizeItems) return false;
        return culledItems.contains(item.getEntityId());
    }

    public int getCulledCount() {
        return culledItems.size();
    }
}
