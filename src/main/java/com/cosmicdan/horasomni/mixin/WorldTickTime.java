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

@Mixin(World.class)
@SuppressWarnings({"WeakerAccess", "InstanceVariableMayNotBeInitialized"})
@Log4j2(topic = "Hora Somni")
public class WorldTickTime {
    @Shadow
    public Dimension dimension;

    private int overworldDayTicksSkipped = 0;

    @Inject(method = "tickTime", /*locals = LocalCapture.PRINT,*/ cancellable = true, at = @At(value = "JUMP", opcode = Opcodes.IFEQ, shift = At.Shift.AFTER))
    protected void tickTime(final CallbackInfo callbackInfo) {
        // we're injecting before the call to setTimeOfDay (after the JUMP)
        if ((dimension != null) && dimension.getType().equals(DimensionType.OVERWORLD)) {
            if (overworldDayTicksSkipped >= (ModConfig.DAY_LENGTH_MULTIPLIER.value - 1)) {
                // counter elapsed - reset counter and allow original logic
                overworldDayTicksSkipped = 0;
            } else {
                // counter NOT elapsed - increment and cancel target (skips original setTimeOfDay call)
                overworldDayTicksSkipped++;
                callbackInfo.cancel();
            }
        }

    }
}
