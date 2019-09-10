package com.cosmicdan.horasomni;

/**
 * Storage for global mod *constants* (anything dynamic or runtime-determined will be elsewhere).
 * @see ModConfig
 * @author Daniel 'CosmicDan' Connolly
 */
@SuppressWarnings("WeakerAccess")
public interface ModInfo {
    // be sure this matches whatever is in the fabric mod info json!
    String MOD_ID = "horasomni";
}
