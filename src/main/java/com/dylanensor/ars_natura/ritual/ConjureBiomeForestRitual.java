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

public class ConjureBiomeForestRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isFlower,
            isDark,
            isBirch,
            isOldBirch;

    public ConjureBiomeForestRitual() {
        super(Biomes.FOREST);
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
        if (isFlower) {
            int depth = getPos().getY() - placePos.getY();
            if (depth == 1) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            } else if (depth == 2) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.MOSS_BLOCK.defaultBlockState();
            }
            return Blocks.STONE.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.STONE.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Forest"; }

    @Override
    public String getLangDescription() {
        return "Creates a forest biome in a circle around the ritual, converting the area to Forest, Flower Forest, Dark Forest, Birch Forest, or Old Growth Birch Forest biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isFlower && stack.is(Items.SPORE_BLOSSOM.asItem()))
                || (!isDark && stack.is(Items.RED_MUSHROOM_BLOCK.asItem()))
                || (!isBirch && stack.is(Items.BEE_NEST.asItem()))
                || (!isOldBirch && stack.is(Items.STRIPPED_BIRCH_LOG.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.FOREST); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isFlower", isFlower);
        tag.putBoolean("isDark", isDark);
        tag.putBoolean("isBirch", isBirch);
        tag.putBoolean("isOldBirch", isOldBirch);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isFlower = getConsumedItems().stream().anyMatch(i -> i.is(Items.SPORE_BLOSSOM.asItem()));
        isDark = getConsumedItems().stream().anyMatch(i-> i.is(Items.RED_MUSHROOM_BLOCK.asItem()));
        isBirch = getConsumedItems().stream().anyMatch(i-> i.is(Items.BEE_NEST.asItem()));
        isOldBirch = getConsumedItems().stream().anyMatch(i-> i.is(Items.STRIPPED_BIRCH_LOG.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.FOREST;
        if (isFlower) {
            this.biome = Biomes.FLOWER_FOREST;
        } else if (isDark) {
            this.biome = Biomes.DARK_FOREST;
        } else if (isBirch) {
            this.biome = Biomes.BIRCH_FOREST;
        } else if (isOldBirch) {
            this.biome = Biomes.OLD_GROWTH_BIRCH_FOREST;
        }
        return this.biome;
    }
}
