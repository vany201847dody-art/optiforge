package com.vizor.optiforge;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ModConfig {

    public static final Logger LOGGER = LogManager.getLogger("OptiForge");

    // ==== Сущности / мобы ====
    public static boolean entityCulling = true;
    public static int entityRenderDistance = 5;
    public static boolean skipEntityTicks = true;        // не тикать дальних сущностей
    public static float entityTickDistance = 32f;         // дальше этой дистанции не тикать
    public static boolean optimizeMobs = true;
    public static int mobSpawnLimit = 40;
    public static boolean noItemShadow = true;
    public static boolean noEntityShadow = true;
    public static boolean cullLivingBehind = true;
    public static int maxLivingEntities = 60;

    // ==== Частицы ====
    public static boolean optimizedParticles = true;
    public static int particleLimit = 40;
    public static boolean clearParticlesOnLowFps = true;
    public static int lowFpsParticleThreshold = 25;
    public static boolean reduceParticlesWhenMoving = true;

    // ==== Предметы / дроп ====
    public static boolean optimizeItems = true;
    public static int itemLimit = 150;
    public static boolean mergeNearbyItems = true;
    public static boolean despawnFarItems = true;
    public static int itemDespawnDistance = 64;
    public static boolean noItemBob = true;

    // ==== Блоки / блок-энтити ====
    public static boolean blockEntityCulling = true;
    public static int blockEntityDistance = 48;
    public static boolean fastLeaves = false;
    public static boolean noFancyLeaves = false;
    public static boolean noParticlesFromBlocks = true;

    // ==== Чанки / мир ====
    public static boolean optimizedChunkLoading = true;
    public static int chunkLoadRadius = 4;
    public static boolean noSkyRender = false;
    public static boolean noClouds = false;
    public static int renderDistanceOverride = 0;         // 0 = не трогать
    public static boolean fasterChunkRebuild = true;

    // ==== Рендер ====
    public static boolean lowShadows = true;
    public static boolean disableParticlesUnderWater = false;
    public static boolean disableRainSplash = true;
    public static boolean reduceFov = false;
    public static int maxFov = 80;
    public static boolean noWeatherRender = false;

    // ==== HUD / авто ====
    public static boolean showFpsHud = false;
    public static boolean autoDetect = true;             // автоопределение при входе
    public static boolean autoLimitFps = true;
    public static int targetFps = 60;
    public static boolean dynamicResolution = false;
    public static boolean showWarnings = false;

    // ==== Мобы тёмные/освещение ====
    public static boolean dynamicLighting = false;
    public static boolean noShadowRender = true;

    // ==== Качество ====
    public static String graphicsMode = "fast";          // fast / fancy / auto
    public static boolean smoothLightingEnabled = true;
    public static boolean biomeBlend = false;
    public static boolean entityShadowsDistance = true;
    public static int entityShadowDistance = 16;

    public static void load() {
        File configFile = getConfigFile();
        if (!configFile.exists()) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(configFile.toPath())) {
            JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
            if (obj.has("entityCulling")) entityCulling = obj.get("entityCulling").getAsBoolean();
            if (obj.has("entityRenderDistance")) entityRenderDistance = obj.get("entityRenderDistance").getAsInt();
            if (obj.has("skipEntityTicks")) skipEntityTicks = obj.get("skipEntityTicks").getAsBoolean();
            if (obj.has("optimizeMobs")) optimizeMobs = obj.get("optimizeMobs").getAsBoolean();
            if (obj.has("mobSpawnLimit")) mobSpawnLimit = obj.get("mobSpawnLimit").getAsInt();
            if (obj.has("noItemShadow")) noItemShadow = obj.get("noItemShadow").getAsBoolean();
            if (obj.has("noEntityShadow")) noEntityShadow = obj.get("noEntityShadow").getAsBoolean();
            if (obj.has("maxLivingEntities")) maxLivingEntities = obj.get("maxLivingEntities").getAsInt();
            if (obj.has("optimizedParticles")) optimizedParticles = obj.get("optimizedParticles").getAsBoolean();
            if (obj.has("particleLimit")) particleLimit = obj.get("particleLimit").getAsInt();
            if (obj.has("clearParticlesOnLowFps")) clearParticlesOnLowFps = obj.get("clearParticlesOnLowFps").getAsBoolean();
            if (obj.has("lowFpsParticleThreshold")) lowFpsParticleThreshold = obj.get("lowFpsParticleThreshold").getAsInt();
            if (obj.has("optimizeItems")) optimizeItems = obj.get("optimizeItems").getAsBoolean();
            if (obj.has("itemLimit")) itemLimit = obj.get("itemLimit").getAsInt();
            if (obj.has("mergeNearbyItems")) mergeNearbyItems = obj.get("mergeNearbyItems").getAsBoolean();
            if (obj.has("despawnFarItems")) despawnFarItems = obj.get("despawnFarItems").getAsBoolean();
            if (obj.has("itemDespawnDistance")) itemDespawnDistance = obj.get("itemDespawnDistance").getAsInt();
            if (obj.has("noItemBob")) noItemBob = obj.get("noItemBob").getAsBoolean();
            if (obj.has("blockEntityCulling")) blockEntityCulling = obj.get("blockEntityCulling").getAsBoolean();
            if (obj.has("blockEntityDistance")) blockEntityDistance = obj.get("blockEntityDistance").getAsInt();
            if (obj.has("fastLeaves")) fastLeaves = obj.get("fastLeaves").getAsBoolean();
            if (obj.has("noFancyLeaves")) noFancyLeaves = obj.get("noFancyLeaves").getAsBoolean();
            if (obj.has("noParticlesFromBlocks")) noParticlesFromBlocks = obj.get("noParticlesFromBlocks").getAsBoolean();
            if (obj.has("optimizedChunkLoading")) optimizedChunkLoading = obj.get("optimizedChunkLoading").getAsBoolean();
            if (obj.has("chunkLoadRadius")) chunkLoadRadius = obj.get("chunkLoadRadius").getAsInt();
            if (obj.has("noClouds")) noClouds = obj.get("noClouds").getAsBoolean();
            if (obj.has("renderDistanceOverride")) renderDistanceOverride = obj.get("renderDistanceOverride").getAsInt();
            if (obj.has("fasterChunkRebuild")) fasterChunkRebuild = obj.get("fasterChunkRebuild").getAsBoolean();
            if (obj.has("lowShadows")) lowShadows = obj.get("lowShadows").getAsBoolean();
            if (obj.has("disableRainSplash")) disableRainSplash = obj.get("disableRainSplash").getAsBoolean();
            if (obj.has("reduceFov")) reduceFov = obj.get("reduceFov").getAsBoolean();
            if (obj.has("maxFov")) maxFov = obj.get("maxFov").getAsInt();
            if (obj.has("noWeatherRender")) noWeatherRender = obj.get("noWeatherRender").getAsBoolean();
            if (obj.has("showFpsHud")) showFpsHud = obj.get("showFpsHud").getAsBoolean();
            if (obj.has("autoDetect")) autoDetect = obj.get("autoDetect").getAsBoolean();
            if (obj.has("autoLimitFps")) autoLimitFps = obj.get("autoLimitFps").getAsBoolean();
            if (obj.has("targetFps")) targetFps = obj.get("targetFps").getAsInt();
            if (obj.has("dynamicResolution")) dynamicResolution = obj.get("dynamicResolution").getAsBoolean();
            if (obj.has("showWarnings")) showWarnings = obj.get("showWarnings").getAsBoolean();
            if (obj.has("dynamicLighting")) dynamicLighting = obj.get("dynamicLighting").getAsBoolean();
            if (obj.has("noShadowRender")) noShadowRender = obj.get("noShadowRender").getAsBoolean();
            if (obj.has("graphicsMode")) graphicsMode = obj.get("graphicsMode").getAsString();
            if (obj.has("smoothLightingEnabled")) smoothLightingEnabled = obj.get("smoothLightingEnabled").getAsBoolean();
            if (obj.has("biomeBlend")) biomeBlend = obj.get("biomeBlend").getAsBoolean();
            if (obj.has("entityShadowDistance")) entityShadowDistance = obj.get("entityShadowDistance").getAsInt();
        } catch (Exception e) {
            LOGGER.error("Не удалось прочитать конфиг OptiForge", e);
        }
    }

    public static void save() {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("entityCulling", entityCulling);
            obj.addProperty("entityRenderDistance", entityRenderDistance);
            obj.addProperty("skipEntityTicks", skipEntityTicks);
            obj.addProperty("optimizeMobs", optimizeMobs);
            obj.addProperty("mobSpawnLimit", mobSpawnLimit);
            obj.addProperty("noItemShadow", noItemShadow);
            obj.addProperty("noEntityShadow", noEntityShadow);
            obj.addProperty("maxLivingEntities", maxLivingEntities);
            obj.addProperty("optimizedParticles", optimizedParticles);
            obj.addProperty("particleLimit", particleLimit);
            obj.addProperty("clearParticlesOnLowFps", clearParticlesOnLowFps);
            obj.addProperty("lowFpsParticleThreshold", lowFpsParticleThreshold);
            obj.addProperty("optimizeItems", optimizeItems);
            obj.addProperty("itemLimit", itemLimit);
            obj.addProperty("mergeNearbyItems", mergeNearbyItems);
            obj.addProperty("despawnFarItems", despawnFarItems);
            obj.addProperty("itemDespawnDistance", itemDespawnDistance);
            obj.addProperty("noItemBob", noItemBob);
            obj.addProperty("blockEntityCulling", blockEntityCulling);
            obj.addProperty("blockEntityDistance", blockEntityDistance);
            obj.addProperty("fastLeaves", fastLeaves);
            obj.addProperty("noFancyLeaves", noFancyLeaves);
            obj.addProperty("noParticlesFromBlocks", noParticlesFromBlocks);
            obj.addProperty("optimizedChunkLoading", optimizedChunkLoading);
            obj.addProperty("chunkLoadRadius", chunkLoadRadius);
            obj.addProperty("noClouds", noClouds);
            obj.addProperty("renderDistanceOverride", renderDistanceOverride);
            obj.addProperty("fasterChunkRebuild", fasterChunkRebuild);
            obj.addProperty("lowShadows", lowShadows);
            obj.addProperty("disableRainSplash", disableRainSplash);
            obj.addProperty("reduceFov", reduceFov);
            obj.addProperty("maxFov", maxFov);
            obj.addProperty("noWeatherRender", noWeatherRender);
            obj.addProperty("showFpsHud", showFpsHud);
            obj.addProperty("autoDetect", autoDetect);
            obj.addProperty("autoLimitFps", autoLimitFps);
            obj.addProperty("targetFps", targetFps);
            obj.addProperty("dynamicResolution", dynamicResolution);
            obj.addProperty("showWarnings", showWarnings);
            obj.addProperty("dynamicLighting", dynamicLighting);
            obj.addProperty("noShadowRender", noShadowRender);
            obj.addProperty("graphicsMode", graphicsMode);
            obj.addProperty("smoothLightingEnabled", smoothLightingEnabled);
            obj.addProperty("biomeBlend", biomeBlend);
            obj.addProperty("entityShadowDistance", entityShadowDistance);

            File configFile = getConfigFile();
            configFile.getParentFile().mkdirs();
            try (Writer writer = Files.newBufferedWriter(configFile.toPath())) {
                OptiForgeMod.GSON.toJson(obj, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Не удалось сохранить конфиг OptiForge", e);
        }
    }

    public static File getConfigFile() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "optiforge.json");
    }
}
