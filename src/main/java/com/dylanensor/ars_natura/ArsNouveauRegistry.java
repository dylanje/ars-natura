package com.dylanensor.ars_natura;

import com.dylanensor.ars_natura.ritual.*;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;


public class ArsNouveauRegistry {

    public static void registerRituals(){
        RitualRegistry.registerRitual(new ConjureBiomeBeachRitual());
        RitualRegistry.registerRitual(new ConjureBiomeColdOceanRitual());
        RitualRegistry.registerRitual(new ConjureBiomeForestRitual());
        RitualRegistry.registerRitual(new ConjureBiomeJungleRitual());
        RitualRegistry.registerRitual(new ConjureBiomeMountainRitual());
        RitualRegistry.registerRitual(new ConjureBiomeDesertRitual());
        RitualRegistry.registerRitual(new ConjureBiomePlainsRitual());
        RitualRegistry.registerRitual(new ConjureBiomeOceanRitual());
        RitualRegistry.registerRitual(new ConjureBiomeRiverRitual());
        RitualRegistry.registerRitual(new ConjureBiomeSavannaRitual());
        RitualRegistry.registerRitual(new ConjureBiomeSwampRitual());
        RitualRegistry.registerRitual(new ConjureBiomeTaigaRitual());
        RitualRegistry.registerRitual(new ConjureBiomeWarmOceanRitual());
        RitualRegistry.registerRitual(new ConjureBiomeWindsweptHillsRitual());
    }

}
