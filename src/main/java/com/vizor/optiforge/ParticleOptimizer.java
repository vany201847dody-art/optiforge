package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Queue;

public class ParticleOptimizer {

    private int spawnedThisTick = 0;
    private int totalParticles = 0;
    private int tickCounter = 0;

    private Field particlesField = null;

    public void register() {
        try {
            particlesField = ParticleManager.class.getDeclaredField("particles");
            particlesField.setAccessible(true);
        } catch (Exception e) {
            OptiForgeMod.LOGGER.warn("Не удалось получить доступ к щетчику частиц", e);
        }

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            spawnedThisTick = 0;
            tickCounter++;
            if (tickCounter % 20 != 0 && ModConfig.optimizedParticles) return;

            totalParticles = countParticles();

            if (!ModConfig.optimizedParticles) return;
            if (ModConfig.particleLimit <= 0) return;

            if (totalParticles > ModConfig.particleLimit) {
                clearExcessParticles(totalParticles - ModConfig.particleLimit);
                totalParticles = ModConfig.particleLimit;
            }
        });
    }

    private int countParticles() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.particleManager == null || particlesField == null) return 0;
        try {
            Map<?, ?> map = (Map<?, ?>) particlesField.get(mc.particleManager);
            int count = 0;
            for (Object value : map.values()) {
                count += ((Queue<?>) value).size();
            }
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    private void clearExcessParticles(int numToRemove) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.particleManager == null || particlesField == null) return;
        try {
            Map<?, ?> map = (Map<?, ?>) particlesField.get(mc.particleManager);
            int removed = 0;
            for (Object value : map.values()) {
                Queue<?> queue = (Queue<?>) value;
                while (!queue.isEmpty() && removed < numToRemove) {
                    queue.poll();
                    removed++;
                }
                if (removed >= numToRemove) return;
            }
        } catch (Exception ignored) {
        }
    }

    public boolean canSpawnParticle() {
        if (!ModConfig.optimizedParticles) return true;
        if (ModConfig.particleLimit <= 0) return true;

        spawnedThisTick++;
        return spawnedThisTick <= ModConfig.particleLimit / 20;
    }

    public int getTotalParticles() {
        return totalParticles;
    }
}
