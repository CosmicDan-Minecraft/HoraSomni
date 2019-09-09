package com.cosmicdan.horasomni;

import de.siphalor.tweed.client.TweedClothBridge;
import de.siphalor.tweed.config.*;
import de.siphalor.tweed.config.constraints.RangeConstraint;
import de.siphalor.tweed.config.entry.IntEntry;
import de.siphalor.tweed.config.fixers.ConfigEntryLocationFixer;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
//@SuppressWarnings("WeakerAccess") // needed for Cloth discovery
public class ModConfig {

    private static final ConfigFile CONFIG_FILE = TweedRegistry.registerConfigFile(HoraSomni.MOD_ID).setReloadListener(ModConfig::reload).setEnvironment(ConfigEnvironment.SERVER);
    private static final TweedClothBridge TWEED_CLOTH_BRIDGE;

    static {
        TWEED_CLOTH_BRIDGE = (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) ? new TweedClothBridge(CONFIG_FILE) : null;
        CONFIG_FILE.register("dayLengthMultiplier", new ConfigEntryLocationFixer("dayLengthMultiplier", "General"));
    }

    static void init() {} // empty since we use static initializer - this is only here to ping the class from onInitialize

    public static Screen getScreen() {
        return TWEED_CLOTH_BRIDGE.buildScreen();
    }

    private static final ConfigCategory CATEGORY_GENERAL =
            CONFIG_FILE.register("general", new ConfigCategory())
                    .setEnvironment(ConfigEnvironment.SYNCED)
                    .setComment("General settings for the mod" + System.lineSeparator()
                    );

    public static final IntEntry DAY_LENGTH_MULTIPLIER =
            CATEGORY_GENERAL.register("dayLengthMultiplier", new IntEntry(1))
                    .addConstraint(new RangeConstraint<Integer>().greaterThan(0))
                    .setComment("Desired day length multiplier. E.g. 2 is twice as long.");


    private static void reload(final ConfigEnvironment configEnvironment, final ConfigScope configScope) {

    }
}
