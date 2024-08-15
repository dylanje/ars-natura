package com.dylanensor.ars_natura.ritual;

import com.dylanensor.ars_natura.ArsNatura;
import com.dylanensor.ars_natura.lib.RitualLib;
import com.hollingsworth.arsnouveau.api.ritual.ConjureBiomeRitual;
import com.hollingsworth.arsnouveau.common.block.ArchwoodChest;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.example.registry.ItemRegistry;

import javax.annotation.Nullable;
/*
    OCEAN
    WARM_OCEAN
    LUKEWARM_OCEAN
    DEEP_LUKEWARM_OCEAN
    DEEP_OCEAN
    COLD_OCEAN
    DEEP_COLD_OCEAN
    FROZEN_OCEAN
    DEEP_FROZEN_OCEAN
 */
public class ConjureOceanRitual extends ConjureBiomeRitual {
    boolean isWarm,
            isLukewarm,
            isDeepLukewarm,
            isDeep,
            isCold,
            isDeepCold,
            isFrozen,
            isDeepFrozen;

    public ConjureOceanRitual() {
        super(Biomes.OCEAN);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkForBiomeModifier();
        setBiome();
    }

    @Override
    public BlockState stateForPos(BlockPos nextPos) {
        checkForBiomeModifier();

        return nextPos.getY() == getPos().getY() - 1 ? Blocks.GRAVEL.defaultBlockState() : Blocks.DIRT.defaultBlockState();
    }

    @Override
    public String getLangName() { return "Conjure Island: Ocean"; }

    @Override
    public String getLangDescription() {
        return "Creates an island of ocean in a circle around the ritual, converting the area to Ocean biomes.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isWarm && (stack.is(Items.BRAIN_CORAL_BLOCK.asItem())
                            || stack.is(Items.BUBBLE_CORAL_BLOCK.asItem())
                            || stack.is(Items.HORN_CORAL_BLOCK.asItem())
                            || stack.is(Items.FIRE_CORAL_BLOCK.asItem())
                            || stack.is(Items.TUBE_CORAL_BLOCK.asItem())))
                || (!isLukewarm && stack.is(Items.PUFFERFISH.asItem()))
                || (!isDeepLukewarm && stack.is(Items.DRIED_KELP.asItem()))
                || (!isDeep && stack.is(Items.DRIED_KELP_BLOCK.asItem()))
                || (!isCold && stack.is(Items.SNOW_BLOCK.asItem()))
                || (!isDeepCold && stack.is(Items.ICE.asItem()))
                || (!isFrozen && stack.is(Items.PACKED_ICE.asItem()))
                || (!isDeepFrozen && stack.is(Items.BLUE_ICE.asItem()));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.OCEAN); }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        setBiome();
    }

    private void checkForBiomeModifier() {
        isWarm = getConsumedItems().stream().anyMatch(i ->
                i.is(Items.BRAIN_CORAL_BLOCK.asItem()) ||
                i.is(Items.BUBBLE_CORAL_BLOCK.asItem()) ||
                i.is(Items.HORN_CORAL_BLOCK.asItem()) ||
                i.is(Items.FIRE_CORAL_BLOCK.asItem()) ||
                i.is(Items.TUBE_CORAL_BLOCK.asItem()));

        isLukewarm = getConsumedItems().stream().anyMatch(i -> i.is(Items.PUFFERFISH.asItem()));
        isDeepLukewarm = getConsumedItems().stream().anyMatch(i-> i.is(Items.DRIED_KELP.asItem()));
        isDeep = getConsumedItems().stream().anyMatch(i-> i.is(Items.DRIED_KELP_BLOCK.asItem()));
        isCold = getConsumedItems().stream().anyMatch(i-> i.is(Items.SNOW_BLOCK.asItem()));
        isDeepCold = getConsumedItems().stream().anyMatch(i-> i.is(Items.ICE.asItem()));
        isFrozen = getConsumedItems().stream().anyMatch(i-> i.is(Items.PACKED_ICE.asItem()));
        isDeepFrozen = getConsumedItems().stream().anyMatch(i-> i.is(Items.BLUE_ICE.asItem()));
    }

    private void setBiome() {
        if (isWarm) {
            biome = Biomes.WARM_OCEAN;
        } else if (isLukewarm) {
            biome = Biomes.LUKEWARM_OCEAN;
        } else if (isDeepLukewarm) {
            biome = Biomes.DEEP_LUKEWARM_OCEAN;
        } else if (isDeep) {
            biome = Biomes.DEEP_OCEAN;
        } else if (isCold) {
            biome = Biomes.COLD_OCEAN;
        } else if (isDeepCold) {
            biome = Biomes.DEEP_COLD_OCEAN;
        } else if (isFrozen) {
            biome = Biomes.FROZEN_OCEAN;
        } else if (isDeepFrozen) {
            biome = Biomes.DEEP_FROZEN_OCEAN;
        } else {
            biome = Biomes.OCEAN;
        }
    }
}
