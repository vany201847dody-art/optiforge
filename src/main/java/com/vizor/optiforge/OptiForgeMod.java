package com.vizor.optiforge;

import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptiForgeMod implements ClientModInitializer {

    public static final String MODID = "optiforge";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Gson GSON = new Gson();

    public static EntityOptimizer entityOptimizer;
    public static ParticleOptimizer particleOptimizer;
    public static ItemOptimizer itemOptimizer;
    public static BlockEntityOptimizer blockEntityOptimizer;
    public static StatsHud statsHud;
    public static FpsMeter fpsMeter;
    public static boolean hasAppliedAutoDetect = false;

    @Override
    public void onInitializeClient() {
        ModConfig.load();
        HardwareDetector.detect();
        ModDetector.detect();

        entityOptimizer = new EntityOptimizer();
        particleOptimizer = new ParticleOptimizer();
        itemOptimizer = new ItemOptimizer();
        blockEntityOptimizer = new BlockEntityOptimizer();
        statsHud = new StatsHud();
        fpsMeter = new FpsMeter();

        entityOptimizer.register();
        particleOptimizer.register();
        itemOptimizer.register();
        blockEntityOptimizer.register();
        statsHud.register();
        KeyBindings.register();
        AutoOptimizer.register();

        LOGGER.info("OptiForge загружен! " + HardwareDetector.getSummary());
        LOGGER.info("OptiForge: " + ModDetector.getOptimmodsSummary() + " | " + ModDetector.getCustomModsSummary());
    }

    public static void applyAutoDetectNow() {
        PerfMode.applyAutoDetected();
        ModDetector.detect();
        ModConfig.save();
        GameSettingsApplier.apply();
        OptiForgeMod.LOGGER.info("OptiForge применил автонастройки под тир " + HardwareDetector.tier
                + " (" + HardwareDetector.getTierName() + ")");
    }
}
