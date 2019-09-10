package com.cosmicdan.horasomni.client;

import com.cosmicdan.horasomni.Main;
import com.cosmicdan.horasomni.ModConfig;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class ModMenuInitializer implements ModMenuApi {
    @Override
    public String getModId() {
        return Main.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (parentScreen) -> ModConfig.getScreen();
    }
}
