package com.vizor.optiforge;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class BlockEntityOptimizer {

    private final Set<Long> culledPositions = new HashSet<>();
    private int tickCounter = 0;

    public void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tickCounter++;
            // Реже сканируем блок-энтити (было каждый 10 тик). Сдвиг фазы так,
            // чтобы не совпадало со сканами сущностей/дропа (отдельные "заикания").
            if (tickCounter % 40 != 0) return;
            if (!ModConfig.blockEntityCulling) return;

            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || mc.player == null) {
                culledPositions.clear();
                return;
            }

            culledPositions.clear();

            double camX = mc.player.getX();
            double camY = mc.player.getY();
            double camZ = mc.player.getZ();
            double maxDistSq = 128.0 * 128.0;
            // Дорогая проверка "полностью окружён блоками" только вблизи игрока
            double nearSq = 32.0 * 32.0;

            for (BlockEntity be : mc.world.blockEntities) {
                if (be instanceof net.minecraft.block.entity.ChestBlockEntity) continue;
                if (be instanceof net.minecraft.block.entity.SignBlockEntity) continue;
                if (be instanceof net.minecraft.block.entity.BannerBlockEntity) continue;

                BlockPos pos = be.getPos();
                double dx = pos.getX() + 0.5 - camX;
                double dy = pos.getY() + 0.5 - camY;
                double dz = pos.getZ() + 0.5 - camZ;
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq > maxDistSq) {
                    culledPositions.add(pos.asLong());
                    continue;
                }

                // Проверка закупоренности только для близких — для дальних дешёвая дистанция
                if (distSq <= nearSq && isFullySurrounded(mc, pos)) {
                    culledPositions.add(pos.asLong());
                }
            }
        });
    }

    private boolean isFullySurrounded(MinecraftClient mc, BlockPos pos) {
        return isSolid(mc, pos.north())
                && isSolid(mc, pos.south())
                && isSolid(mc, pos.east())
                && isSolid(mc, pos.west())
                && isSolid(mc, pos.up())
                && isSolid(mc, pos.down());
    }

    private boolean isSolid(MinecraftClient mc, BlockPos pos) {
        return mc.world.getBlockState(pos).isOpaqueFullCube(mc.world, pos);
    }

    public boolean isBlockEntityCulled(BlockEntity be) {
        if (!ModConfig.blockEntityCulling) return false;
        return culledPositions.contains(be.getPos().asLong());
    }

    public int getCulledCount() {
        return culledPositions.size();
    }
}
