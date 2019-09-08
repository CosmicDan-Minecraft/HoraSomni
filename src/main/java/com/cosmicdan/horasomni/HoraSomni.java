package com.cosmicdan.horasomni;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;

@Log4j2(topic = "HoraSomni")
public class HoraSomni implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		log.info("Hello from HoraSomni!");
	}
}
