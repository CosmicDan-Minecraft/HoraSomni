package com.cosmicdan.horasomni;

import de.siphalor.tweed.client.TweedClothBridge;
import de.siphalor.tweed.config.*;
import de.siphalor.tweed.config.constraints.RangeConstraint;
import de.siphalor.tweed.config.entry.BooleanEntry;
import de.siphalor.tweed.config.entry.IntEntry;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@SuppressWarnings("HardcodedLineSeparator")
@Log4j2
@UtilityClass
public class ModConfig {
    private static final ConfigFile CONFIG_FILE = TweedRegistry.registerConfigFile(ModInfo.MOD_ID).setReloadListener(ModConfig::reload).setEnvironment(ConfigEnvironment.SERVER);
    private static final TweedClothBridge TWEED_CLOTH_BRIDGE;

    static {
        TWEED_CLOTH_BRIDGE = (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) ? new TweedClothBridge(CONFIG_FILE) : null;
    }

    static void init() {} // empty since we use static initializer - this is only here to ping the class from onInitialize

    public static Screen getScreen() {
        return TWEED_CLOTH_BRIDGE.buildScreen();
    }

    private static final ConfigCategory CATEGORY_TIME_SCALE =
            CONFIG_FILE.register("timescale", new ConfigCategory())
                    .setEnvironment(ConfigEnvironment.SYNCED)
                    .setComment("Time scale settings. Includes options such as multipliers for day/night length.\n" +
                            "Further reading: 'Day-night cycle' article on minecraft.gamepedia.com");
    public static final IntEntry DAY_MULTIPLIER =
            CATEGORY_TIME_SCALE.register("dayMultiplier", new IntEntry(1))
                    .addConstraint(new RangeConstraint<Integer>().greaterThan(0))
                    .setComment("Desired day length multiplier. E.g. 2 is twice as long.");
    public static final BooleanEntry NIGHT_MULTIPLIER_ENABLED =
            CATEGORY_TIME_SCALE.register("nightMultiplierEnabled", new BooleanEntry(false))
                    .setComment("Use a separate multiplier for night time, otherwise Day is used.\n" +
                            "WARNING: May reduce per-world tick performance.");
    public static final IntEntry NIGHT_MULTIPLIER =
            CATEGORY_TIME_SCALE.register("nightMultiplier", new IntEntry(1))
                    .addConstraint(new RangeConstraint<Integer>().greaterThan(0))
                    .setComment("Desired night length multiplier. E.g. 2 is twice as long. Only applies if \n" +
                            "Custom Night Length is enabled.");
    public static final IntEntry NIGHT_MULTIPLIER_START =
            CATEGORY_TIME_SCALE.register("nightMultiplierStart", new IntEntry(12542))
                    .addConstraint(new RangeConstraint<Integer>().greaterThan(0))
                    .setComment("Set custom night start (in ticks) for multiplier change.");
    public static final IntEntry NIGHT_MULTIPLIER_END =
            CATEGORY_TIME_SCALE.register("nightMultiplierEnd", new IntEntry(23999))
                    .addConstraint(new RangeConstraint<Integer>().greaterThan(0))
                    .setComment("Set custom night end (in ticks) for multiplier change.");


    private static final ConfigCategory CATEGORY_TIMELAPSE =
            CONFIG_FILE.register("timelapse", new ConfigCategory())
                    .setEnvironment(ConfigEnvironment.SYNCED)
                    .setComment("All timelapse customization. Also set Timescale overrides during timelapse here.");
    public static final BooleanEntry TIMELAPSE_ENABLED =
            CATEGORY_TIMELAPSE.register("enableTimelapse", new BooleanEntry(true))
                    .setComment("Disable for vanilla behavior.");


    private static void reload(final ConfigEnvironment configEnvironment, final ConfigScope configScope) {
        if (NIGHT_MULTIPLIER_END.value < NIGHT_MULTIPLIER_START.value) {
            // TODO: something better than crashing. Ideally show an error in the GUI, like what happens when you have an empty string for an int field.
            throw new RuntimeException("For performance reasons, Custom Night End must be later than Custom Night Start.");
        }
    }
}
