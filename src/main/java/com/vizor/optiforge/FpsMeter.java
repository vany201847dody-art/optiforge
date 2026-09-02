package com.vizor.optiforge;

import net.minecraft.client.MinecraftClient;

public class FpsMeter {

    private long lastSampleTime = 0;
    private int frames = 0;
    public int currentFps = 0;
    private long windowStart = 0;
    private int windowFrames = 0;
    public int averageFps = 0;

    public void tick() {
        long now = System.currentTimeMillis();
        frames++;
        windowFrames++;

        if (lastSampleTime == 0) lastSampleTime = now;
        long elapsed = now - lastSampleTime;
        if (elapsed >= 1000) {
            currentFps = (int) ((frames * 1000L) / elapsed);
            frames = 0;
            lastSampleTime = now;
        }

        if (windowStart == 0) windowStart = now;
        long windowElapsed = now - windowStart;
        if (windowElapsed >= 3000) {
            averageFps = (int) ((windowFrames * 1000L) / windowElapsed);
            windowFrames = 0;
            windowStart = now;
        }
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public int getAverageFps() {
        return averageFps;
    }

    public static int readDebugFps() {
        MinecraftClient mc = MinecraftClient.getInstance();
        try {
            if (mc.fpsDebugString != null && !mc.fpsDebugString.isEmpty()) {
                String s = mc.fpsDebugString;
                int sp = s.indexOf(' ');
                if (sp > 0) {
                    return Integer.parseInt(s.substring(0, sp));
                }
            }
        } catch (Exception ignored) {
        }
        return 0;
    }
}
