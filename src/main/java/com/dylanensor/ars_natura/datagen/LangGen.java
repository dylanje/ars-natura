package com.dylanensor.ars_natura.datagen;

import com.dylanensor.ars_natura.ArsNatura;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGen extends LanguageProvider {
    public LangGen(PackOutput pack, String modid, String locale) {
        super(pack, modid, locale);
    }

    @Override
    protected void addTranslations() {
        for (RitualTablet i : RitualRegistry.getRitualItemMap().values().stream().filter(tab -> tab.ritual.getRegistryName().getNamespace().equals(ArsNatura.MODID)).toList()) {
            add("ars_natura.ritual_desc." + i.ritual.getRegistryName().getPath(), i.ritual.getLangDescription());
            add("item.ars_natura." + i.ritual.getRegistryName().getPath(), i.ritual.getLangName());
        }

        add("ars_natura.tablet_of", "Tablet of %s");
    }
}
