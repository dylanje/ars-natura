package com.dylanensor.ars_natura.ritual;

import com.dylanensor.ars_natura.ArsNatura;
import com.dylanensor.ars_natura.lib.RitualLib;
import com.hollingsworth.arsnouveau.api.ritual.ConjureBiomeRitual;
import com.hollingsworth.arsnouveau.api.ritual.ManhattenTracker;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ConjureBiomeBeachRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isSnowy,
            isStony;

    public ConjureBiomeBeachRitual() {
        super(Biomes.BEACH);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkForBiomeModifier();
        this.biome = setBiome();

        if(getWorld().isClientSide){
            return;
        }
        for(ItemStack i : getConsumedItems()){
            if(i.is(ItemTagProvider.SOURCE_GEM_TAG)) {
                radius += i.getCount();
            }
        }
        tracker = new ManhattenTracker(getPos().below(7), radius, 2, radius);
    }

    @Override
    public BlockState stateForPos(BlockPos placePos) {
        checkForBiomeModifier();
        if (isSnowy) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.SNOW_BLOCK.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.PACKED_ICE.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.SANDSTONE.defaultBlockState();
            }
            return Blocks.SAND.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.SAND.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Beach"; }

    @Override
    public String getLangDescription() {
        return "Creates a biome of beach in a circle around the ritual, converting the area to Beach, Snowy Beach, or Stony Shore biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isSnowy && stack.is(Items.SNOW_BLOCK.asItem()))
                || (!isStony && stack.is(Items.CHISELED_STONE_BRICKS.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.BEACH); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isSnowy", isSnowy);
        tag.putBoolean("isStony", isStony);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isSnowy = getConsumedItems().stream().anyMatch(i-> i.is(Items.SNOW_BLOCK.asItem()));
        isStony = getConsumedItems().stream().anyMatch(i-> i.is(Items.CHISELED_STONE_BRICKS.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.BEACH;
        if (isSnowy) {
            this.biome = Biomes.SNOWY_BEACH;
        } else if (isStony) {
            this.biome = Biomes.STONY_SHORE;
        }
        return this.biome;
    }
}
