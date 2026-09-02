package com.vizor.optiforge;

public class PerfMode {

    public static final int MODE_AUTODETECT = 0;
    public static final int MODE_POTOK = 1;
    public static final int MODE_LOW = 2;
    public static final int MODE_MEDIUM = 3;
    public static final int MODE_HIGH = 4;
    public static final int MODE_ULTRA = 5;

    private static int currentAutoTier = -1;

    public static void applyAutoDetected() {
        if (!HardwareDetector.isDetected()) HardwareDetector.detect();
        int tier = HardwareDetector.tier;
        applyTier(tier);
        currentAutoTier = tier;
    }

    public static void applyTier(int tier) {
        switch (tier) {
            case 0: applyPotok(); break;
            case 1: applyLow(); break;
            case 2: applyMedium(); break;
            case 3: applyHigh(); break;
            default: applyUltra(); break;
        }
    }

    public static void applyPotok() {
        // Максимальная производительность для очень слабого железа
        ModConfig.entityRenderDistance = 3;
        ModConfig.skipEntityTicks = true;
        ModConfig.entityTickDistance = 24f;
        ModConfig.mobSpawnLimit = 20;
        ModConfig.maxLivingEntities = 30;
        ModConfig.particleLimit = 20;
        ModConfig.clearParticlesOnLowFps = true;
        ModConfig.lowFpsParticleThreshold = 30;
        ModConfig.itemLimit = 60;
        ModConfig.itemDespawnDistance = 32;
        ModConfig.blockEntityDistance = 32;
        ModConfig.chunkLoadRadius = 2;
        ModConfig.renderDistanceOverride = 2;
        ModConfig.noClouds = true;
        ModConfig.lowShadows = true;
        ModConfig.noWeatherRender = true;
        ModConfig.targetFps = 30;
        ModConfig.graphicsMode = "fast";
        ModConfig.smoothLightingEnabled = false;
        ModConfig.biomeBlend = false;
        ModConfig.noItemShadow = true;
        ModConfig.noEntityShadow = true;
        ModConfig.entityShadowDistance = 0;
        ModConfig.maxFov = 70;
        ModConfig.reduceFov = true;
        ModConfig.noItemBob = true;
        ModConfig.noParticlesFromBlocks = true;
        ModConfig.dynamicResolution = true;
        ModConfig.fasterChunkRebuild = true;
        ModConfig.mergeNearbyItems = true;
        ModConfig.despawnFarItems = true;
    }

    public static void applyLow() {
        ModConfig.entityRenderDistance = 4;
        ModConfig.skipEntityTicks = true;
        ModConfig.entityTickDistance = 32f;
        ModConfig.mobSpawnLimit = 30;
        ModConfig.maxLivingEntities = 40;
        ModConfig.particleLimit = 30;
        ModConfig.clearParticlesOnLowFps = true;
        ModConfig.lowFpsParticleThreshold = 28;
        ModConfig.itemLimit = 90;
        ModConfig.itemDespawnDistance = 40;
        ModConfig.blockEntityDistance = 40;
        ModConfig.chunkLoadRadius = 3;
        ModConfig.renderDistanceOverride = 3;
        ModConfig.noClouds = true;
        ModConfig.lowShadows = true;
        ModConfig.noWeatherRender = false;
        ModConfig.targetFps = 40;
        ModConfig.graphicsMode = "fast";
        ModConfig.smoothLightingEnabled = true;
        ModConfig.biomeBlend = false;
        ModConfig.noItemShadow = true;
        ModConfig.noEntityShadow = true;
        ModConfig.entityShadowDistance = 4;
        ModConfig.maxFov = 75;
        ModConfig.reduceFov = true;
        ModConfig.noItemBob = true;
        ModConfig.noParticlesFromBlocks = true;
        ModConfig.dynamicResolution = true;
        ModConfig.fasterChunkRebuild = true;
        ModConfig.mergeNearbyItems = true;
        ModConfig.despawnFarItems = true;
    }

    public static void applyMedium() {
        ModConfig.entityRenderDistance = 5;
        ModConfig.skipEntityTicks = true;
        ModConfig.entityTickDistance = 40f;
        ModConfig.mobSpawnLimit = 40;
        ModConfig.maxLivingEntities = 55;
        ModConfig.particleLimit = 50;
        ModConfig.clearParticlesOnLowFps = true;
        ModConfig.lowFpsParticleThreshold = 25;
        ModConfig.itemLimit = 150;
        ModConfig.itemDespawnDistance = 48;
        ModConfig.blockEntityDistance = 48;
        ModConfig.chunkLoadRadius = 4;
        ModConfig.renderDistanceOverride = 4;
        ModConfig.noClouds = false;
        ModConfig.lowShadows = true;
        ModConfig.noWeatherRender = false;
        ModConfig.targetFps = 60;
        ModConfig.graphicsMode = "fast";
        ModConfig.smoothLightingEnabled = true;
        ModConfig.biomeBlend = false;
        ModConfig.noItemShadow = true;
        ModConfig.noEntityShadow = true;
        ModConfig.entityShadowDistance = 8;
        ModConfig.maxFov = 80;
        ModConfig.reduceFov = true;
        ModConfig.noItemBob = true;
        ModConfig.noParticlesFromBlocks = true;
        ModConfig.dynamicResolution = true;
        ModConfig.fasterChunkRebuild = true;
        ModConfig.mergeNearbyItems = true;
        ModConfig.despawnFarItems = true;
    }

    public static void applyHigh() {
        ModConfig.entityRenderDistance = 7;
        ModConfig.skipEntityTicks = true;
        ModConfig.entityTickDistance = 48f;
        ModConfig.mobSpawnLimit = 60;
        ModConfig.maxLivingEntities = 80;
        ModConfig.particleLimit = 100;
        ModConfig.clearParticlesOnLowFps = true;
        ModConfig.lowFpsParticleThreshold = 22;
        ModConfig.itemLimit = 250;
        ModConfig.itemDespawnDistance = 64;
        ModConfig.blockEntityDistance = 64;
        ModConfig.chunkLoadRadius = 6;
        ModConfig.renderDistanceOverride = 6;
        ModConfig.noClouds = false;
        ModConfig.lowShadows = false;
        ModConfig.noWeatherRender = false;
        ModConfig.targetFps = 75;
        ModConfig.graphicsMode = "fancy";
        ModConfig.smoothLightingEnabled = true;
        ModConfig.biomeBlend = true;
        ModConfig.noItemShadow = false;
        ModConfig.noEntityShadow = false;
        ModConfig.entityShadowDistance = 32;
        ModConfig.maxFov = 90;
        ModConfig.reduceFov = false;
        ModConfig.noItemBob = false;
        ModConfig.noParticlesFromBlocks = false;
        ModConfig.dynamicResolution = false;
        ModConfig.fasterChunkRebuild = true;
        ModConfig.mergeNearbyItems = false;
        ModConfig.despawnFarItems = false;
    }

    public static void applyUltra() {
        ModConfig.entityRenderDistance = 12;
        ModConfig.skipEntityTicks = false;
        ModConfig.entityTickDistance = 64f;
        ModConfig.mobSpawnLimit = 120;
        ModConfig.maxLivingEntities = 160;
        ModConfig.particleLimit = 300;
        ModConfig.clearParticlesOnLowFps = false;
        ModConfig.lowFpsParticleThreshold = 20;
        ModConfig.itemLimit = 500;
        ModConfig.itemDespawnDistance = 128;
        ModConfig.blockEntityDistance = 128;
        ModConfig.chunkLoadRadius = 10;
        ModConfig.renderDistanceOverride = 10;
        ModConfig.noClouds = false;
        ModConfig.lowShadows = false;
        ModConfig.noWeatherRender = false;
        ModConfig.targetFps = 144;
        ModConfig.graphicsMode = "fancy";
        ModConfig.smoothLightingEnabled = true;
        ModConfig.biomeBlend = true;
        ModConfig.noItemShadow = false;
        ModConfig.noEntityShadow = false;
        ModConfig.entityShadowDistance = 64;
        ModConfig.maxFov = 110;
        ModConfig.reduceFov = false;
        ModConfig.noItemBob = false;
        ModConfig.noParticlesFromBlocks = false;
        ModConfig.dynamicResolution = false;
        ModConfig.fasterChunkRebuild = true;
        ModConfig.mergeNearbyItems = false;
        ModConfig.despawnFarItems = false;
    }

    public static int getCurrentAutoTier() {
        return currentAutoTier;
    }

    public static String tierName(int tier) {
        switch (tier) {
            case 0: return "Поток";
            case 1: return "Низко";
            case 2: return "Средне";
            case 3: return "Хорошо";
            default: return "Ультра";
        }
    }
}
