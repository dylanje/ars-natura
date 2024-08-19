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

public class ConjureBiomeTaigaRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isSnowy,
            isSpruce,
            isPine;

    public ConjureBiomeTaigaRitual() {
        super(Biomes.TAIGA);
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
                return Blocks.GRASS_BLOCK.defaultBlockState();
            } else if (depth == 3) {
                return Blocks.STONE.defaultBlockState();
            }
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }
        return placePos.getY() == getPos().getY() - 1 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.STONE.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Biome: Taiga"; }

    @Override
    public String getLangDescription() {
        return "Creates a taiga biome in a circle around the ritual, converting the area to Taiga, Snowy Taiga, Old Growth Spruce Taiga, or Old Growth Pine Taiga biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isSnowy && stack.is(Items.SNOW_BLOCK.asItem()))
                || (!isSpruce && stack.is(Items.STRIPPED_SPRUCE_LOG.asItem()))
                || (!isPine && stack.is(Items.STRIPPED_SPRUCE_WOOD.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.TAIGA); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isSnowy", isSnowy);
        tag.putBoolean("isSpruce", isSpruce);
        tag.putBoolean("isPine", isPine);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isSnowy = getConsumedItems().stream().anyMatch(i-> i.is(Items.SNOW_BLOCK.asItem()));
        isSpruce = getConsumedItems().stream().anyMatch(i-> i.is(Items.STRIPPED_SPRUCE_LOG.asItem()));
        isPine = getConsumedItems().stream().anyMatch(i-> i.is(Items.STRIPPED_SPRUCE_WOOD.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.TAIGA;
        if (isSnowy) {
            this.biome = Biomes.SNOWY_TAIGA;
        } else if (isSpruce) {
            this.biome = Biomes.OLD_GROWTH_SPRUCE_TAIGA;
        } else if (isPine) {
            this.biome = Biomes.OLD_GROWTH_PINE_TAIGA;
        }
        return this.biome;
    }
}
