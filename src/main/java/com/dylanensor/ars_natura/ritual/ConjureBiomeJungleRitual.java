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

public class ConjureBiomeJungleRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isBamboo,
            isSparse;

    public ConjureBiomeJungleRitual() {
        super(Biomes.JUNGLE);
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
        if (isBamboo) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.PODZOL.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.STONE.defaultBlockState();
            }
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Jungle"; }

    @Override
    public String getLangDescription() {
        return "Creates a jungle biome in a circle around the ritual, converting the area to Jungle, Bamboo Jungle, or Sparse Jungle biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isBamboo && stack.is(Items.BAMBOO_MOSAIC.asItem()))
                || (!isSparse && stack.is(Items.MELON.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.JUNGLE); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isBamboo", isBamboo);
        tag.putBoolean("isSparse", isSparse);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isBamboo = getConsumedItems().stream().anyMatch(i-> i.is(Items.BAMBOO_MOSAIC.asItem()));
        isSparse = getConsumedItems().stream().anyMatch(i-> i.is(Items.MELON.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.JUNGLE;
        if (isBamboo) {
            this.biome = Biomes.BAMBOO_JUNGLE;
        } else if (isSparse) {
            this.biome = Biomes.SPARSE_JUNGLE;
        }
        return this.biome;
    }
}
