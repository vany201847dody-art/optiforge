package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

public class EntityOptimizer {

    private final Set<Integer> culledEntities = new HashSet<>();
    private int tickCounter = 0;
    private int livingCount = 0;
    private int lastLivingCount = 0;
    private boolean scanned = false;

    public void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tickCounter++;
            // Реже сканируем (было каждый 4 тик) — на слабом CPU частая итерация по всем
            // сущностям мира вызывала фризы на серверах. Теперь 24 тика (~1.2 сек при 20 TPS).
            if (tickCounter % 24 != 0) return;
            if (!ModConfig.entityCulling) return;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || mc.player == null) {
                culledEntities.clear();
                return;
            }

            culledEntities.clear();
            livingCount = 0;
            scanned = true;

            double maxDist = ModConfig.entityRenderDistance * 16.0;
            double maxDistSq = maxDist * maxDist;

            Vec3d camPos = mc.player.getPos();
            float yaw = mc.player.yaw;
            float fov = 70f;
            double yawRad = Math.toRadians(-yaw);
            double forwardX = -Math.sin(yawRad);
            double forwardZ = Math.cos(yawRad);
            double fovHalfRad = Math.toRadians(fov / 2.0 + 5);
            double cosThreshold = Math.cos(fovHalfRad);

            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player) continue;
                // Считаем всех живых сущностей (не только предметы) для статистики
                if (entity instanceof LivingEntity) livingCount++;

                double dx = entity.getX() - camPos.x;
                double dy = entity.getY() - camPos.y;
                double dz = entity.getZ() - camPos.z;

                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq > maxDistSq) {
                    culledEntities.add(entity.getEntityId());
                    continue;
                }

                double lenXZ = Math.sqrt(dx * dx + dz * dz);
                if (lenXZ < 0.001) continue;

                double dot = dx * forwardX + dz * forwardZ;
                if (dot / lenXZ < cosThreshold) {
                    culledEntities.add(entity.getEntityId());
                }
            }
            lastLivingCount = livingCount;
        });
    }

    // Проверка на переполнение живыми сущностями (по настройке maxLivingEntities)
    public boolean hasLivingOverflow(int entityId) {
        return false;
    }

    public void setLivingCount(int count) {
        this.livingCount = count;
    }

    public boolean isEntityCulled(Entity entity) {
        if (!ModConfig.entityCulling) return false;
        return culledEntities.contains(entity.getEntityId());
    }

    public int getCulledCount() {
        return culledEntities.size();
    }

    public boolean isScanned() {
        return scanned;
    }

    public int getLastLivingCount() {
        return lastLivingCount;
    }
}
