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
@SuppressWarnings("WeakerAccess")
@Log4j2
public class MinecraftServerHooks {
    @Shadow
    public ServerWorld getWorld(DimensionType DIMENSION_TYPE) {return null;}

    /**
     * Hook for Timelapse. Replaces the inline 50ms-per-tick value with a getter.
     * @param originalValue The current ms-per-tick from #run constants being replaced
     * @return The desired MS-per-tick delay
     */
    @ModifyConstant(method = "run", constant = @Constant(longValue = 50L), require = 4, expect = 4)
    public long getMsPerTicks(final long originalValue) {
        final ServerWorld overworld = getWorld(DimensionType.OVERWORLD);
        //noinspection CastToIncompatibleInterface
        return (ModConfig.ENABLE_TIMELAPSE.value && ((ServerWorldAccessor)overworld).getAllPlayersSleeping()) ? 1L : originalValue;
    }


}
