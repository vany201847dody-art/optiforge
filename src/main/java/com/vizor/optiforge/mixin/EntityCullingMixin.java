package com.vizor.optiforge.mixin;

import com.vizor.optiforge.OptiForgeMod;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityCullingMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void optiforge_cullEntity(E entity, Frustum frustum, double x, double y, double z,
                                                         CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) return;

        if (OptiForgeMod.entityOptimizer != null && OptiForgeMod.entityOptimizer.isEntityCulled(entity)) {
            cir.setReturnValue(false);
            return;
        }

        if (OptiForgeMod.itemOptimizer != null && entity instanceof net.minecraft.entity.ItemEntity) {
            if (OptiForgeMod.itemOptimizer.isItemCulled((net.minecraft.entity.ItemEntity) entity)) {
                cir.setReturnValue(false);
            }
        }
    }
}
