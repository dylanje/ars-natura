package com.dylanensor.ars_natura.lib;

public class RitualLib {
    public static final String OCEAN = prependRitual("conjure_island_ocean");

    public static String prependRitual(String ritual) { return "ritual_" + ritual; }
}
