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
     * Hook for Timelapse. Prevents "skip to day" logic from occuring.
     * @param owner
     * @return
     */
    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;allPlayersSleeping:Z", opcode = Opcodes.GETFIELD))
    public boolean shouldSkipToDay(final ServerWorld owner) {
        boolean shouldSkip = false; // always prevent skip-to-day by default
        if (!ModConfig.ENABLE_TIMELAPSE.value) // timelapse disabled so do vanilla logic
            shouldSkip = allPlayersSleeping;
        else { // timelapse enabled
            if (allPlayersSleeping) // timelapse has started
                timelapseActive = true;
            else if (timelapseActive) { // timelapse has ended
                timelapseActive = false;
                owner.updatePlayersSleeping();
            }
        }
        return shouldSkip;
    }
}
