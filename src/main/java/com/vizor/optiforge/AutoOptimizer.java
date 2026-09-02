package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class AutoOptimizer {

    private static int tick = 0;
    private static boolean gateOpened = false;
    private static int autoTierApplied = -1;
    private static boolean autoDetectSet = false;
    private static int lowFpsCount = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            tick++;

            if (!autoDetectSet && tick > 40) {
                autoDetectSet = true;
                autoTierApplied = HardwareDetector.tier;
                if (ModConfig.autoDetect && !OptiForgeMod.hasAppliedAutoDetect) {
                    OptiForgeMod.hasAppliedAutoDetect = true;
                    OptiForgeMod.applyAutoDetectNow();
                }
            }

            if (autoTierApplied < 0) return;
            if (!ModConfig.autoDetect) return;

            // Динамическое понижение, если всё равно лагает
            if (!ModConfig.showWarnings) return;

            int fps = OptiForgeMod.fpsMeter != null ? OptiForgeMod.fpsMeter.getCurrentFps() : 0;
            int threshold = autoTierApplied == 0 ? 20 : autoTierApplied == 1 ? 25 : 30;

            if (fps > 0 && fps < threshold) {
                lowFpsCount++;
                if (lowFpsCount > 10 && autoTierApplied > 0) {
                    lowFpsCount = 0;
                    autoTierApplied--;
                    PerfMode.applyTier(autoTierApplied);
                    ModConfig.save();
                    GameSettingsApplier.apply();
                    OptiForgeMod.LOGGER.warn("OptiForge: FPS низкий (" + fps + "), понизил настройки до " + PerfMode.tierName(autoTierApplied));
                }
            } else {
                lowFpsCount = 0;
            }
        });
    }
}
