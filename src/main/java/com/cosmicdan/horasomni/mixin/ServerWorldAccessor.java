package com.cosmicdan.horasomni.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@SuppressWarnings({"InterfaceMayBeAnnotatedFunctional", "WeakerAccess"})
@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {
    @Accessor
    boolean isAllPlayersSleeping();
}
