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

public class ConjureBiomeOceanRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isDeep,
            isMushroom;

    public ConjureBiomeOceanRitual() {
        super(Biomes.OCEAN);
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
        if (isMushroom) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.MUSHROOM_STEM.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.RED_MUSHROOM_BLOCK.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
            }
            return Blocks.MYCELIUM.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Ocean"; }

    @Override
    public String getLangDescription() {
        return "Creates an ocean biome in a circle around the ritual, converting the area to Ocean, Deep Ocean, or Mushroom Fields biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isDeep && stack.is(Items.DRIED_KELP_BLOCK.asItem()))
                || (!isMushroom && stack.is(Items.SUSPICIOUS_STEW.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.OCEAN); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isDeep", isDeep);
        tag.putBoolean("isMushroom", isMushroom);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isDeep = getConsumedItems().stream().anyMatch(i-> i.is(Items.DRIED_KELP_BLOCK.asItem()));
        isMushroom = getConsumedItems().stream().anyMatch(i-> i.is(Items.SUSPICIOUS_STEW.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.OCEAN;
        if (isDeep) {
            this.biome = Biomes.DEEP_OCEAN;
        } else if (isMushroom) {
            this.biome = Biomes.MUSHROOM_FIELDS;
        }
        return this.biome;
    }
}
