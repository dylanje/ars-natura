package com.dylanensor.ars_natura.ritual;

import com.dylanensor.ars_natura.ArsNatura;
import com.dylanensor.ars_natura.lib.RitualLib;
import com.hollingsworth.arsnouveau.api.ritual.ConjureBiomeRitual;
import com.hollingsworth.arsnouveau.api.ritual.ManhattenTracker;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ConjureBiomeDesertRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isBadlands,
            isWooded,
            isEroded;

    public ConjureBiomeDesertRitual() {
        super(Biomes.DESERT);
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
        if (isBadlands) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.TERRACOTTA.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.BROWN_TERRACOTTA.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.WHITE_TERRACOTTA.defaultBlockState();
            }
            return Blocks.SANDSTONE.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SMOOTH_SANDSTONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Desert"; }

    @Override
    public String getLangDescription() {
        return "Creates a desert biome in a circle around the ritual, converting the area to Desert, Badlands, Wooded Badlands, or Eroded Badlands biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isBadlands && stack.is(Items.TERRACOTTA.asItem()))
                || (!isWooded && stack.is(Items.STRIPPED_OAK_WOOD.asItem()))
                || (!isEroded && stack.is(Items.POWERED_RAIL.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.NEW_DESERT); }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isBadlands = getConsumedItems().stream().anyMatch(i-> i.is(Items.TERRACOTTA.asItem()));
        isWooded = getConsumedItems().stream().anyMatch(i-> i.is(Items.STRIPPED_OAK_WOOD.asItem()));
        isEroded = getConsumedItems().stream().anyMatch(i-> i.is(Items.POWERED_RAIL.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        if (isBadlands) {
            this.biome = Biomes.BADLANDS;
        } else if (isWooded) {
            this.biome = Biomes.WOODED_BADLANDS;
        } else if (isEroded) {
            this.biome = Biomes.ERODED_BADLANDS;
        } else {
            this.biome = Biomes.DESERT;
        }
    }
}

