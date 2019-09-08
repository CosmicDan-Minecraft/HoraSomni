package com.cosmicdan.horasomni.mixin;

import com.cosmicdan.horasomni.HoraSomni;
import net.minecraft.client.MinecraftClient;
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
public class WorldTickTime {
    private int dayTickSkipCounter = 0;
    private int dayLengthMultiplier = 100;

    @Shadow
    private final Dimension dimension = null;

    @Inject(method = "tickTime", cancellable = true, at = @At(value = "JUMP", opcode = Opcodes.IFEQ, shift = At.Shift.AFTER))
    protected void tickTime(final CallbackInfo callbackInfo) {
        // we're injecting before the call to setTimeOfDay (after the JUMP)
        //final World world = (World) (Object) this;
        if (dimension.getType().equals(DimensionType.OVERWORLD)) {
            if (dayTickSkipCounter == (dayLengthMultiplier - 1)) {
                // counter elapsed - reset counter and allow original logic
                dayTickSkipCounter = 0;
            } else {
                // counter NOT elapsed - increment and cancel target (skips original setTimeOfDay call)
                dayTickSkipCounter++;
                callbackInfo.cancel();
            }
        }
    }
}
