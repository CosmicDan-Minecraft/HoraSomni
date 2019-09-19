package com.cosmicdan.horasomni.mixin;

import com.cosmicdan.horasomni.ModConfig;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * MinecraftServer hooks for Hora Somni
 * @author Daniel 'CosmicDan' Connolly
 */
@Mixin(MinecraftServer.class)
@SuppressWarnings({"WeakerAccess", "ReturnOfNull", "SameReturnValue"})
@Log4j2
public class MinecraftServerHooks {
    @Shadow
    public ServerWorld getWorld(final DimensionType dimensionType) {return null;}

    /**
     * @return True if Timelapse config is enabled and all players are currently sleeping.
     */
    private boolean shouldTimelapseNow() {
        //noinspection CastToIncompatibleInterface
        return ModConfig.TIMELAPSE_ENABLED.value && ((ServerWorldAccessor)getWorld(DimensionType.OVERWORLD)).isAllPlayersSleeping();
    }

    /**
     * Hook for Timelapse. Replaces the inline 50ms-per-tick value with a getter.
     * @param originalValue The current ms-per-tick from #run constants being replaced
     * @return The desired MS-per-tick delay
     */
    @ModifyConstant(method = "run", constant = @Constant(longValue = 50L), require = 4, expect = 4)
    public long getMsPerTicks(final long originalValue) {
        return shouldTimelapseNow() ? 1L : originalValue;
    }
}
