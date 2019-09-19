package com.cosmicdan.horasomni.mixin;

import com.cosmicdan.horasomni.ModConfig;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * ServerWorld hooks
 * @author Daniel 'CosmicDan' Connolly
 */
@Mixin(ServerWorld.class)
@SuppressWarnings({"WeakerAccess", "InstanceVariableMayNotBeInitialized"})
@Log4j2(topic = "Hora Somni")
public class ServerWorldHooks {
    @Shadow
    private boolean allPlayersSleeping;

    private boolean timelapseActive = false;

    /**
     * Hook for Timelapse. Prevents "skip to day" logic from occurring. Note that TODO is responsible for preventing auto-wake during day.
     * @param owner Instance of hook caller
     * @return true if skipping-to-day should occur (vanilla behavior), otherwise false
     */
    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;allPlayersSleeping:Z", opcode = Opcodes.GETFIELD))
    public boolean shouldSkipToDay(final ServerWorld owner) {
        boolean shouldSkip = false; // always prevent skip-to-day by default
        // timelapse disabled so do vanilla logic
        if (ModConfig.TIMELAPSE_ENABLED.value) { // timelapse enabled
            if (allPlayersSleeping) // timelapse has started
                timelapseActive = true;
            else if (timelapseActive) { // timelapse has ended
                timelapseActive = false;
                owner.updatePlayersSleeping();
            }
        } else // timelapse disabled so revert to vanilla behavior
            shouldSkip = allPlayersSleeping;
        return shouldSkip;
    }
}
