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

public class ConjureBiomePlainsRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isSunflower,
            isSnowy,
            isIce;

    public ConjureBiomePlainsRitual() {
        super(Biomes.PLAINS);
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
        if (isSunflower) {
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
    public String getLangName() { return "Conjure Biome: Plains"; }

    @Override
    public String getLangDescription() {
        return "Creates a plains biome in a circle around the ritual, converting the area to Plains, Sunflower Plains, Snowy Plains, or Ice Spikes biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isSunflower && stack.is(Items.SUNFLOWER.asItem()))
                || (!isSnowy && stack.is(Items.SNOW_BLOCK.asItem()))
                || (!isIce && stack.is(Items.BLUE_ICE.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.PLAINS); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isSunflower", isSunflower);
        tag.putBoolean("isSnowy", isSnowy);
        tag.putBoolean("isIce", isIce);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isSunflower = getConsumedItems().stream().anyMatch(i-> i.is(Items.SUNFLOWER.asItem()));
        isSnowy = getConsumedItems().stream().anyMatch(i-> i.is(Items.SNOW_BLOCK.asItem()));
        isIce = getConsumedItems().stream().anyMatch(i-> i.is(Items.BLUE_ICE.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.PLAINS;
        if (isSunflower) {
            this.biome = Biomes.SUNFLOWER_PLAINS;
        } else if (isSnowy) {
            this.biome = Biomes.SNOWY_PLAINS;
        } else if (isIce) {
            this.biome = Biomes.ICE_SPIKES;
        }
        return this.biome;
    }
}

