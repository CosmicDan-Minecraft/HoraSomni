package com.cosmicdan.horasomni.mixin;

import com.cosmicdan.horasomni.Main;
import com.cosmicdan.horasomni.ModConfig;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@Mixin(ServerWorld.class)
@SuppressWarnings({"WeakerAccess", "InstanceVariableMayNotBeInitialized"})
@Log4j2(topic = "Hora Somni")
public class ServerWorldHooks {
    @Shadow
    private boolean allPlayersSleeping;

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;allPlayersSleeping:Z", opcode = Opcodes.GETFIELD))
    public boolean shouldSkipToDay(final ServerWorld owner) {
        // always update our internal flag with current gamestate, so day length can act accordingly
        boolean shouldSkip = false; // always prevent skip-to-day by default
        if (!ModConfig.ENABLE_TIMELAPSE.value) // timelapse disabled so do vanilla logic
            shouldSkip = allPlayersSleeping;
        return shouldSkip;
    }
}
