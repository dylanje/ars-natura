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

public class ConjureBiomeRiverRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isFrozen;

    public ConjureBiomeRiverRitual() {
        super(Biomes.RIVER);
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
        if (isFrozen) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.PACKED_ICE.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.PACKED_ICE.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.STONE.defaultBlockState();
            }
            return Blocks.STONE.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.GRASS.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: River"; }

    @Override
    public String getLangDescription() {
        return "Creates a river biome in a circle around the ritual, converting the area to River or Frozen River biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isFrozen && stack.is(Items.PACKED_ICE.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.RIVER); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isFrozen", isFrozen);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isFrozen = getConsumedItems().stream().anyMatch(i-> i.is(Items.PACKED_ICE.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.RIVER;
        if (isFrozen) {
            this.biome = Biomes.FROZEN_RIVER;
        }
        return this.biome;
    }
}
