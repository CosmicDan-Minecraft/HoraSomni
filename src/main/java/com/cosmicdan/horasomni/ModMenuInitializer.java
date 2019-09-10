package com.cosmicdan.horasomni;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@SuppressWarnings({"ClassWithoutLogger", "WeakerAccess"})
public class ModMenuInitializer implements ModMenuApi {
    @Override
    public String getModId() {
        return ModInfo.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (parentScreen) -> ModConfig.getScreen();
    }
}
