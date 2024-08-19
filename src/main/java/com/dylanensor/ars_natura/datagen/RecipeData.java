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
        shapelessBuilder(getAddonRitual(RitualLib.BEACH)).requires(Tags.Items.NETHER_STARS).requires(Items.SAND).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.COLD_OCEAN)).requires(Tags.Items.NETHER_STARS).requires(Items.BLUE_ICE).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.FOREST)).requires(Tags.Items.NETHER_STARS).requires(Items.OAK_LEAVES).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.JUNGLE)).requires(Tags.Items.NETHER_STARS).requires(Items.COCOA_BEANS).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.MOUNTAIN)).requires(Tags.Items.NETHER_STARS).requires(Items.STONE_BRICKS).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.DESERT)).requires(Tags.Items.NETHER_STARS).requires(Items.SANDSTONE).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.PLAINS)).requires(Tags.Items.NETHER_STARS).requires(Items.GRASS_BLOCK).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.OCEAN)).requires(Tags.Items.NETHER_STARS).requires(Items.WATER_BUCKET).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.RIVER)).requires(Tags.Items.NETHER_STARS).requires(Items.INK_SAC).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.SAVANNA)).requires(Tags.Items.NETHER_STARS).requires(Items.ACACIA_LOG).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.SWAMP)).requires(Tags.Items.NETHER_STARS).requires(Items.MANGROVE_LOG).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.TAIGA)).requires(Tags.Items.NETHER_STARS).requires(Items.SPRUCE_LOG).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.WARM_OCEAN)).requires(Tags.Items.NETHER_STARS).requires(Items.PUFFERFISH).requires(SOURCE_GEM_BLOCK).save(consumer);
        shapelessBuilder(getAddonRitual(RitualLib.WINDSWEPT_HILLS)).requires(Tags.Items.NETHER_STARS).requires(Items.GRAVEL).requires(SOURCE_GEM_BLOCK).save(consumer);
    }

    public RitualTablet getAddonRitual(String name) {
        return RitualRegistry.getRitualItemMap().get(new ResourceLocation(ArsNatura.MODID, name));
    }
}
