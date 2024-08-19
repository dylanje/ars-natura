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

public class ConjureBiomeColdOceanRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isDeepCold,
            isFrozen,
            isDeepFrozen;

    public ConjureBiomeColdOceanRitual() {
        super(Biomes.COLD_OCEAN);
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
        if (isDeepCold) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.WATER.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.WATER.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.SAND.defaultBlockState();
            }
            return Blocks.SANDSTONE.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.SAND.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Cold Ocean"; }

    @Override
    public String getLangDescription() {
        return "Creates a cold ocean biome in a circle around the ritual, converting the area to Cold Ocean, Deep Cold Ocean, Frozen Ocean, or Deep Frozen Ocean.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isDeepCold && stack.is(Items.ICE.asItem()))
                || (!isFrozen && stack.is(Items.PACKED_ICE.asItem()))
                || (!isDeepFrozen && stack.is(Items.BLUE_ICE.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.COLD_OCEAN); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isDeepCold", isDeepCold);
        tag.putBoolean("isFrozen", isFrozen);
        tag.putBoolean("isDeepFrozen", isDeepFrozen);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isDeepCold = getConsumedItems().stream().anyMatch(i-> i.is(Items.ICE.asItem()));
        isFrozen = getConsumedItems().stream().anyMatch(i-> i.is(Items.PACKED_ICE.asItem()));
        isDeepFrozen = getConsumedItems().stream().anyMatch(i-> i.is(Items.BLUE_ICE.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.COLD_OCEAN;
        if (isDeepCold) {
            this.biome = Biomes.DEEP_COLD_OCEAN;
        } else if (isFrozen) {
            this.biome = Biomes.FROZEN_OCEAN;
        } else if (isDeepFrozen) {
            this.biome = Biomes.DEEP_FROZEN_OCEAN;
        }
        return this.biome;
    }
}
