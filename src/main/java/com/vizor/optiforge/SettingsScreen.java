package com.vizor.optiforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class SettingsScreen extends Screen {

    public SettingsScreen() {
        super(new LiteralText("Настройки OptiForge"));
    }

    private void rebuild() {
        MinecraftClient.getInstance().openScreen(new SettingsScreen());
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int btnWidth = 280;
        int btnHeight = 20;
        int y = 28;

        // Кнопка автоопределения — применяет подходящие настройки под железо + моды
        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, 22,
                new LiteralText("§a§l⚡ ОПРЕДЕЛИТЬ НАСТРОЙКИ ПОД МОЁ ЖЕЛЕЗО"),
                button -> {
                    OptiForgeMod.applyAutoDetectNow();
                    rebuild();
                }));
        y += 28;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Оптимизация сущностей", ModConfig.entityCulling),
                button -> { ModConfig.entityCulling = !ModConfig.entityCulling; ModConfig.save(); rebuild(); }));
        y += 24;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Пропускать тики дальних мобов", ModConfig.skipEntityTicks),
                button -> { ModConfig.skipEntityTicks = !ModConfig.skipEntityTicks; ModConfig.save(); rebuild(); }));
        y += 24;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Ограничить частицы", ModConfig.optimizedParticles),
                button -> { ModConfig.optimizedParticles = !ModConfig.optimizedParticles; ModConfig.save(); GameSettingsApplier.apply(); rebuild(); }));
        y += 24;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Оптимизация предметов (дроп)", ModConfig.optimizeItems),
                button -> { ModConfig.optimizeItems = !ModConfig.optimizeItems; ModConfig.save(); rebuild(); }));
        y += 24;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Оптимизация блок-энтити", ModConfig.blockEntityCulling),
                button -> { ModConfig.blockEntityCulling = !ModConfig.blockEntityCulling; ModConfig.save(); rebuild(); }));
        y += 24;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Автоопределение при входе", ModConfig.autoDetect),
                button -> { ModConfig.autoDetect = !ModConfig.autoDetect; ModConfig.save(); rebuild(); }));
        y += 24;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                toggle("Показать HUD", ModConfig.showFpsHud),
                button -> { ModConfig.showFpsHud = !ModConfig.showFpsHud; ModConfig.save(); rebuild(); }));
        y += 28;

        this.addButton(new ButtonWidget(centerX - btnWidth / 2, y, btnWidth, btnHeight,
                new LiteralText("Готово"), button -> this.onClose()));
    }

    private LiteralText toggle(String name, boolean enabled) {
        return new LiteralText(name + ": " + (enabled ? "§aВКЛ" : "§cВЫКЛ"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        net.minecraft.client.gui.DrawableHelper.drawCenteredText(matrices, this.textRenderer,
                "§6§lOptiForge §7— §fоптимизация Minecraft", this.width / 2, 10, 0xFFFFFF);

        int h = this.height - 4;
        textRenderer.drawWithShadow(matrices, "§7Железо: " + HardwareDetector.getSummary(), 4, h - 42, 0xFFFFFF);
        textRenderer.drawWithShadow(matrices, "§7" + ModDetector.getOptimmodsSummary(), 4, h - 26, 0xFFFFFF);
        textRenderer.drawWithShadow(matrices, "§7" + ModDetector.getCustomModsSummary(), 4, h - 10, 0xFFFFFF);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
