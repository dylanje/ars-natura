package com.dylanensor.ars_natura;

import com.dylanensor.ars_natura.glyphs.TestEffect;
import com.dylanensor.ars_natura.registry.ModRegistry;
import com.dylanensor.ars_natura.ritual.ConjureOceanRitual;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.registry.SpellSoundRegistry;
import com.hollingsworth.arsnouveau.api.sound.SpellSound;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {

    public static void registerGlyphs(){
        RitualRegistry.registerRitual(new ConjureOceanRitual());
    }

}
