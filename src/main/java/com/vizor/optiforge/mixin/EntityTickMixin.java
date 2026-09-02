package com.vizor.optiforge.mixin;

import com.vizor.optiforge.ModConfig;
import com.vizor.optiforge.OptiForgeMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class EntityTickMixin {

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void optiforge_skipFarTicks(Entity entity, CallbackInfo ci) {
        if (!ModConfig.skipEntityTicks) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || entity == null || entity == mc.player) return;

        // Не трогаем предметы и игроков других — их тики дешёвые/важные
        if (entity instanceof ItemEntity) {
            // дешёвые, но если их очень много - тоже ограничим через itemOptimizer
            return;
        }

        if (entity instanceof LivingEntity && ModConfig.optimizeMobs) {
            double distSq = entity.squaredDistanceTo(mc.player.getX(), mc.player.getY(), mc.player.getZ());
            double maxDist = ModConfig.entityTickDistance;
            if (distSq > maxDist * maxDist) {
                ci.cancel();
            }
        }
    }
}
