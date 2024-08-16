package com.dylanensor.ars_natura.datagen;

import com.dylanensor.ars_natura.ArsNatura;
import com.dylanensor.ars_natura.lib.RitualLib;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeData extends RecipeDatagen {
    public RecipeData(PackOutput pack) {
        super(pack);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        shapelessBuilder(getAddonRitual(RitualLib.OCEAN)).requires(Tags.Items.NETHER_STARS).requires(Items.WATER_BUCKET).requires(SOURCE_GEM_BLOCK).save(consumer);
    }

    public RitualTablet getAddonRitual(String name) {
        return RitualRegistry.getRitualItemMap().get(new ResourceLocation(ArsNatura.MODID, name));
    }
}
