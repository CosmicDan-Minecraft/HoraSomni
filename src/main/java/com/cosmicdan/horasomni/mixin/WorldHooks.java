package com.cosmicdan.horasomni.mixin;

import com.cosmicdan.horasomni.ModConfig;
import lombok.extern.log4j.Log4j2;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * World hooks for Hora Somni
 * @author Daniel 'CosmicDan' Connolly
 */
@Mixin(World.class)
@SuppressWarnings({"WeakerAccess", "InstanceVariableMayNotBeInitialized"})
@Log4j2
public class WorldHooks {
    @Shadow
    public Dimension dimension;

    @Shadow
    public long getTimeOfDay() { return 0L; }

    /**
     * We just use a simple integer multiplier for day length to avoid expensive math - a simple counter "consumes" time progression ticks
     * that we want to skip, only letting every n ticks through.
     */
    private static int OVERWORLD_TIMETICKS_SKIPPED = 0;

    /**
     * Hook for day/night length multiplier. Only cares about Overworld.
     * @see WorldHooks#OVERWORLD_TIMETICKS_SKIPPED
     * @param callbackInfo see {@link CallbackInfo}
     */
    @Inject(method = "tickTime", /*locals = LocalCapture.PRINT,*/ cancellable = true, at = @At(value = "JUMP", opcode = Opcodes.IFEQ, shift = At.Shift.AFTER))
        protected void tickTime(final CallbackInfo callbackInfo) {
        // TODO: Divider for Turbo Timelapse. This would be another hook though.
        // we're injecting before the call to setTimeOfDay (after the JUMP)
        if ((dimension != null) && dimension.getType().equals(DimensionType.OVERWORLD)) {
            int multiplierToUse = ModConfig.DAY_MULTIPLIER.value;
            if (ModConfig.NIGHT_MULTIPLIER_ENABLED.value) {
                // TODO: add another counter (configurable) to only check every n ticks
                // custom night/day starts are enabled, check if inside night time
                // note that we've already verified that custom night end is above custom night start
                final long currentTimeOfDay = getTimeOfDay();
                if ((ModConfig.NIGHT_MULTIPLIER_START.value <= currentTimeOfDay) &&
                        (currentTimeOfDay <= ModConfig.NIGHT_MULTIPLIER_END.value)) {
                    multiplierToUse = ModConfig.NIGHT_MULTIPLIER.value;
                }
            }
            if (OVERWORLD_TIMETICKS_SKIPPED >= (multiplierToUse - 1)) {
                // counter elapsed - reset counter and allow original logic
                OVERWORLD_TIMETICKS_SKIPPED = 0;
            } else {
                // counter NOT elapsed - increment and cancel target (skips original setTimeOfDay call)
                OVERWORLD_TIMETICKS_SKIPPED++;
                callbackInfo.cancel();
            }
        }
    }
}
