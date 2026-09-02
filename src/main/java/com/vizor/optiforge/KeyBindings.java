package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    private static KeyBinding openSettings;

    public static void register() {
        openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Открыть настройки OptiForge",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                "OptiForge"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSettings.wasPressed()) {
                if (MinecraftClient.getInstance().currentScreen == null) {
                    MinecraftClient.getInstance().openScreen(new SettingsScreen());
                }
            }
        });
    }
}
