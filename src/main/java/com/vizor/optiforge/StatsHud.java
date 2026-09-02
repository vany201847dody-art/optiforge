package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class StatsHud {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int fps = 0;
    private int entityCount = 0;
    private int particleCount = 0;
    private int culledEntities = 0;
    private int culledBlockEntities = 0;

    public void register() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (!ModConfig.showFpsHud) return;

            updateStats();

            int x = 5;
            int y = 5;

            net.minecraft.client.gui.DrawableHelper.fill(matrixStack, x - 2, y - 2, x + 190, y + 74, 0x80000000);

            draw(matrixStack, "§6OptiForge", x, y, 0xFFFFFF);
            y += 12;

            String fpsColor = fps >= 60 ? "§a" : fps >= 30 ? "§e" : "§c";
            draw(matrixStack, fpsColor + "FPS: " + fps, x, y, 0xFFFFFF);
            y += 12;

            draw(matrixStack, "§bСущности: " + entityCount + " §7(скрыто: " + culledEntities + ")", x, y, 0xFFFFFF);
            y += 12;

            draw(matrixStack, "§dЧастицы: " + particleCount, x, y, 0xFFFFFF);
            y += 12;

            draw(matrixStack, "§eБлок-энтити скрыто: " + culledBlockEntities, x, y, 0xFFFFFF);
            y += 12;
            draw(matrixStack, "§7Профиль: " + HardwareDetector.getTierName() + " §8(" + PerfMode.tierName(HardwareDetector.tier) + ")", x, y, 0xFFFFFF);
        });
    }

    private void updateStats() {
        // НЕ итерируем по всем сущностям каждый кадр (это вызывало фризы) —
        // берём кэшированные счётчики из оптимизаторов.
        if (OptiForgeMod.entityOptimizer != null && OptiForgeMod.entityOptimizer.isScanned()) {
            entityCount = OptiForgeMod.entityOptimizer.getLastLivingCount();
        }
        if (OptiForgeMod.entityOptimizer != null) culledEntities = OptiForgeMod.entityOptimizer.getCulledCount();
        if (OptiForgeMod.particleOptimizer != null) particleCount = OptiForgeMod.particleOptimizer.getTotalParticles();
        if (OptiForgeMod.blockEntityOptimizer != null) culledBlockEntities = OptiForgeMod.blockEntityOptimizer.getCulledCount();
        parseFps();
    }

    private void parseFps() {
        try {
            if (mc.fpsDebugString != null) {
                String s = mc.fpsDebugString;
                int space = s.indexOf(' ');
                if (space > 0) {
                    fps = Integer.parseInt(s.substring(0, space));
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void draw(MatrixStack matrixStack, String text, int x, int y, int color) {
        mc.textRenderer.drawWithShadow(matrixStack, text, x, y, color);
    }
}
