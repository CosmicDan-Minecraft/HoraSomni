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
 * @author Daniel 'CosmicDan' Connolly
 */
@Mixin(MinecraftServer.class)
@Log4j2
public class MinecraftServerHooks {
    @Shadow
    public ServerWorld getWorld(DimensionType dimensionType_1) {return null;}

    @ModifyConstant(method = "run", constant = @Constant(longValue = 50L), require = 4, expect = 4)
    public long getMsPerTicks(final long originalValue) {
        //noinspection CastToIncompatibleInterface
        final ServerWorld overworld = getWorld(DimensionType.OVERWORLD);
        //  TODO: move this to a "onPlayerWake" event
        overworld.updatePlayersSleeping();
        //noinspection CastToIncompatibleInterface
        return (ModConfig.ENABLE_TIMELAPSE.value && ((ServerWorldAccessor)overworld).getAllPlayersSleeping()) ? 1L : originalValue;
    }


}
