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

public class ConjureBiomeSwampRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isMangrove;

    public ConjureBiomeSwampRitual() {
        super(Biomes.SWAMP);
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
        if (isMangrove) {
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
    public String getLangName() { return "Conjure Biome: Swamp"; }

    @Override
    public String getLangDescription() {
        return "Creates a swamp biome in a circle around the ritual, converting the area to Swamp or Mangrove Swamp biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isMangrove && stack.is(Items.MUDDY_MANGROVE_ROOTS.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.SWAMP); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isMangrove", isMangrove);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isMangrove = getConsumedItems().stream().anyMatch(i -> i.is(Items.MUDDY_MANGROVE_ROOTS.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.SWAMP;
        if (isMangrove) {
            this.biome = Biomes.MANGROVE_SWAMP;
        }
        return this.biome;
    }
}
