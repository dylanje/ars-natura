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

public class ConjureBiomeMountainRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isGrove,
            isCherry,
            isSnowy,
            isFrozen,
            isStony,
            isJagged;

    public ConjureBiomeMountainRitual() {
        super(Biomes.MEADOW);
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
        if (isCherry) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.DIRT.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.STONE.defaultBlockState();
            }
            return Blocks.STONE.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Mountain"; }

    @Override
    public String getLangDescription() {
        return "Creates a mountain biome in a circle around the ritual, converting the area to Meadow, Grove, Cherry Grove, Snowy Slopes, Frozen Peaks, Stony Peaks, or Jagged Peaks biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isGrove && stack.is(Items.SPRUCE_LOG.asItem()))
                || (!isCherry && stack.is(Items.PINK_PETALS.asItem()))
                || (!isSnowy && stack.is(Items.SNOW_BLOCK.asItem()))
                || (!isFrozen && stack.is(Items.PACKED_ICE.asItem()))
                || (!isStony && stack.is(Items.STONE_BRICKS.asItem()))
                || (!isJagged && stack.is(Items.GRAVEL.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.MOUNTAIN); }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isGrove = getConsumedItems().stream().anyMatch(i-> i.is(Items.SPRUCE_LOG.asItem()));
        isCherry = getConsumedItems().stream().anyMatch(i-> i.is(Items.PINK_PETALS.asItem()));
        isSnowy = getConsumedItems().stream().anyMatch(i-> i.is(Items.SNOW_BLOCK.asItem()));
        isFrozen = getConsumedItems().stream().anyMatch(i-> i.is(Items.PACKED_ICE.asItem()));
        isStony = getConsumedItems().stream().anyMatch(i-> i.is(Items.STONE_BRICKS.asItem()));
        isJagged = getConsumedItems().stream().anyMatch(i-> i.is(Items.GRAVEL.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        if (isGrove) {
            this.biome = Biomes.GROVE;
        } else if (isCherry) {
            this.biome = Biomes.CHERRY_GROVE;
        } else if (isSnowy) {
            this.biome = Biomes.SNOWY_SLOPES;
        } else if (isFrozen) {
            this.biome = Biomes.FROZEN_PEAKS;
        } else if (isStony) {
            this.biome = Biomes.STONY_PEAKS;
        } else if (isJagged) {
            this.biome = Biomes.JAGGED_PEAKS;
        } else {
            this.biome = Biomes.MEADOW;
        }
        return this.biome;
    }
}

