package com.vizor.optiforge.mixin;

import com.vizor.optiforge.OptiForgeMod;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleSpawningMixin {

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void optiforge_limitParticles(Particle particle, CallbackInfo ci) {
        if (OptiForgeMod.particleOptimizer != null) {
            if (!OptiForgeMod.particleOptimizer.canSpawnParticle()) {
                ci.cancel();
            }
        }
    }
}
