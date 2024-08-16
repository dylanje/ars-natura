package com.dylanensor.ars_natura.datagen;

import com.dylanensor.ars_natura.ArsNatura;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;

import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;

public class ArsProviders {

    static String root = ArsNatura.MODID;


    public static class PatchouliProvider extends com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider {

        public PatchouliProvider(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        public void collectJsons(CachedOutput cache) {

            /*for (AbstractSpellPart spell : ArsNouveauRegistry.registeredSpells) {
                addGlyphPage(spell);
            }*/

            //check the superclass for examples

            for (PatchouliPage patchouliPage : pages) {
                DataProvider.saveStable(cache, patchouliPage.build(), patchouliPage.path());
            }

        }

        @Override
        public PatchouliPage addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage){
            PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                    .withIcon(item.asItem())
                    .withPage(new TextPage(root + ".page." + getRegistryName(item.asItem()).getPath()))
                    .withPage(recipePage);
            var page = new PatchouliPage(builder, getPath(category, getRegistryName(item.asItem()).getPath()));
            this.pages.add(page);
            return page;
        }

        /*public void addFamiliarPage(AbstractFamiliarHolder familiarHolder) {
            PatchouliBuilder builder = new PatchouliBuilder(FAMILIARS, "entity." + root + "." + familiarHolder.getRegistryName().getPath())
                    .withIcon(root + ":" + familiarHolder.getRegistryName().getPath())
                    .withTextPage(root + ".familiar_desc." + familiarHolder.getRegistryName().getPath())
                    .withPage(new EntityPage(familiarHolder.getRegistryName().toString()));
            this.pages.add(new PatchouliPage(builder, getPath(FAMILIARS, familiarHolder.getRegistryName().getPath())));
        }*/

        public void addRitualPage(AbstractRitual ritual) {
            PatchouliBuilder builder = new PatchouliBuilder(RITUALS, "item." + root + '.' + ritual.getRegistryName().getPath())
                    .withIcon(ritual.getRegistryName().toString())
                    .withTextPage(ritual.getDescriptionKey())
                    .withPage(new CraftingPage(root + ":tablet_" + ritual.getRegistryName().getPath()));

            this.pages.add(new PatchouliPage(builder, getPath(RITUALS, ritual.getRegistryName().getPath())));
        }

        /**
         * Gets a name for this provider, to use in logging.
         */
        @Override
        public String getName() {
            return "Ars Natura Patchouli Datagen";
        }

        @Override
        public Path getPath(ResourceLocation category, String fileName) {
            return this.generator.getPackOutput().getOutputFolder().resolve("data/"+ root +"/patchouli_books/ars_natura/en_us/entries/" + category.getPath() + "/" + fileName + ".json");
        }

    }

}
