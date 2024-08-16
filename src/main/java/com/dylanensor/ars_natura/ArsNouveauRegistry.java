package com.dylanensor.ars_natura;

import com.dylanensor.ars_natura.ritual.ConjureOceanRitual;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;


public class ArsNouveauRegistry {

    public static void registerGlyphs(){
        RitualRegistry.registerRitual(new ConjureOceanRitual());
    }

}
