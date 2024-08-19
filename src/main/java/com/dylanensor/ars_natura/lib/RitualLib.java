package com.dylanensor.ars_natura.lib;

public class RitualLib {
    public static final String BEACH = prependRitual("conjure_biome_beach");
    public static final String COLD_OCEAN = prependRitual("conjure_biome_cold_ocean");
    public static final String FOREST = prependRitual("conjure_biome_forest");
    public static final String JUNGLE = prependRitual("conjure_biome_jungle");
    public static final String MOUNTAIN = prependRitual("conjure_biome_mountain");
    public static final String NEW_DESERT = prependRitual("conjure_biome_new_desert");
    public static final String NEW_PLAINS = prependRitual("conjure_biome_new_plains");
    public static final String OCEAN = prependRitual("conjure_biome_ocean");
    public static final String RIVER = prependRitual("conjure_biome_river");
    public static final String SAVANNA = prependRitual("conjure_biome_savanna");
    public static final String SWAMP = prependRitual("conjure_biome_swamp");
    public static final String TAIGA = prependRitual("conjure_biome_taiga");
    public static final String WARM_OCEAN = prependRitual("conjure_biome_warm_ocean");
    public static final String WINDSWEPT_HILLS = prependRitual("conjure_biome_windswept_hills");

    public static String prependRitual(String ritual) { return "ritual_" + ritual; }
}
