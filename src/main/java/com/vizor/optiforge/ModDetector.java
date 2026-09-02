package com.vizor.optiforge;

import net.fabricmc.loader.api.FabricLoader;

public class ModDetector {

    // Оптимизационные моды
    public static boolean sodium = false;
    public static boolean lithium = false;
    public static boolean phosphor = false;
    public static boolean fabricApi = false;
    public static boolean iris = false;
    public static boolean optifine = false;
    public static boolean c2me = false;
    public static boolean starlight = false;
    public static boolean moreculling = false;
    public static boolean entityculling = false;
    public static boolean hydrogen = false;

    // Самописные / пользовательские моды
    public static boolean kotlovan = false;
    public static boolean chatlogger = false;
    public static boolean musicplayer = false;
    public static boolean screenrec = false;
    public static boolean friends = false;
    public static boolean autoafkfish = false;
    public static boolean autofish = false;
    public static boolean inGameAccountSwitcher = false;

    // Другие моды
    public static boolean voicechat = false;
    public static boolean modmenu = false;
    public static boolean voxelmap = false;
    public static boolean jade = false;

    public static void detect() {
        FabricLoader fl = FabricLoader.getInstance();

        sodium = fl.isModLoaded("sodium");
        lithium = fl.isModLoaded("lithium");
        phosphor = fl.isModLoaded("phosphor");
        fabricApi = fl.isModLoaded("fabric") || fl.isModLoaded("fabric-api");
        iris = fl.isModLoaded("iris") || fl.isModLoaded("iris-api");
        optifine = fl.isModLoaded("optifabric") || fl.isModLoaded("optifine");
        c2me = fl.isModLoaded("c2me") || fl.isModLoaded("c2me-fabric");
        starlight = fl.isModLoaded("starlight");
        moreculling = fl.isModLoaded("moreculling") || fl.isModLoaded("entityculling");
        hydrogen = fl.isModLoaded("hydrogen");

        kotlovan = fl.isModLoaded("kotlovan");
        chatlogger = fl.isModLoaded("chatlogger");
        musicplayer = fl.isModLoaded("musicplayer");
        screenrec = fl.isModLoaded("screenrec");
        friends = fl.isModLoaded("friends");
        autoafkfish = fl.isModLoaded("autoafkfish") || fl.isModLoaded("autoafkfish-fabric");
        autofish = fl.isModLoaded("autofish");
        inGameAccountSwitcher = fl.isModLoaded("ingameaccountswitcher") || fl.isModLoaded("in-game-account-switcher");

        voicechat = fl.isModLoaded("voicechat");
        modmenu = fl.isModLoaded("modmenu");
        voxelmap = fl.isModLoaded("voxelmap") || fl.isModLoaded("xaeromap");
        jade = fl.isModLoaded("jade") || fl.isModLoaded("wthit");

        adjustForMods();
    }

    private static void adjustForMods() {
        // ScreenRecorder грузит CPU/GPU сильно — автоопределение должно быть агрессивнее
        if (screenrec && ModConfig.autoDetect) {
            // Компенсируем нагрузку записи: жёстче режем
            if (ModConfig.entityRenderDistance > 4) ModConfig.entityRenderDistance = 4;
            if (ModConfig.particleLimit > 40) ModConfig.particleLimit = 40;
            if (ModConfig.itemLimit > 120) ModConfig.itemLimit = 120;
        }

        // Sodium берёт на себя рендер — можно чуть повысить дистанцию сущностей
        if (sodium && ModConfig.autoDetect) {
            if (ModConfig.entityRenderDistance < 5) ModConfig.entityRenderDistance = 5;
        }

        // Lithium уже оптимизирует тики/ИИ — не конфликтуем, но всё равно помогаем CPU
        if (lithium) {
            ModConfig.skipEntityTicks = true;
        }

        // Phosphor/C2ME уже оптимизируют освещение и чанки
        if (phosphor || c2me || starlight) {
            ModConfig.optimizedChunkLoading = true;
        }

        // музыка не грузит сильно, но если играет + мы любим, ничего не меняем
        // chatlogger/friends — лёгкие, без влияния
    }

    public static String getOptimmodsSummary() {
        StringBuilder sb = new StringBuilder("Опт.моды: ");
        boolean any = false;
        if (sodium) { sb.append("Sodium "); any = true; }
        if (lithium) { sb.append("Lithium "); any = true; }
        if (phosphor) { sb.append("Phosphor "); any = true; }
        if (c2me) { sb.append("C2ME "); any = true; }
        if (starlight) { sb.append("Starlight "); any = true; }
        if (iris) { sb.append("Iris "); any = true; }
        if (!any) sb.append("нет");
        return sb.toString().trim();
    }

    public static String getCustomModsSummary() {
        StringBuilder sb = new StringBuilder("Свои моды: ");
        boolean any = false;
        if (kotlovan) { sb.append("Kotlovan "); any = true; }
        if (chatlogger) { sb.append("Чат-логгер "); any = true; }
        if (musicplayer) { sb.append("Музыка "); any = true; }
        if (screenrec) { sb.append("ScreenRec "); any = true; }
        if (friends) { sb.append("Друзья "); any = true; }
        if (autoafkfish) { sb.append("AFKFish "); any = true; }
        if (!any) sb.append("нет");
        return sb.toString().trim();
    }
}
