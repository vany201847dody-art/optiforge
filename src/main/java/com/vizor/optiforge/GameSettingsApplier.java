package com.vizor.optiforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;

public class GameSettingsApplier {

    public static void apply() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options == null) return;

        // Дистанция рендера
        if (ModConfig.renderDistanceOverride > 0) {
            mc.options.viewDistance = ModConfig.renderDistanceOverride;
        }

        // Частицы
        if (ModConfig.optimizedParticles) {
            int limit = ModConfig.particleLimit;
            if (limit <= 20) mc.options.particles = ParticlesMode.MINIMAL;
            else if (limit <= 60) mc.options.particles = ParticlesMode.DECREASED;
            else mc.options.particles = ParticlesMode.ALL;
        }

        // Графика
        if ("fast".equalsIgnoreCase(ModConfig.graphicsMode)) mc.options.graphicsMode = GraphicsMode.FAST;
        else if ("fancy".equalsIgnoreCase(ModConfig.graphicsMode)) mc.options.graphicsMode = GraphicsMode.FANCY;

        // FOV
        if (ModConfig.reduceFov) {
            double fov = mc.options.fov;
            if (fov > ModConfig.maxFov) {
                mc.options.fov = ModConfig.maxFov;
            }
        }

        try {
            mc.options.write();
        } catch (Exception ignored) {
        }
    }
}
