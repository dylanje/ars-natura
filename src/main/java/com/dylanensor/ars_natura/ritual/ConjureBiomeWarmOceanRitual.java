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

public class ConjureBiomeWarmOceanRitual extends ConjureBiomeRitual {
    public int radius = 9;
    public ManhattenTracker tracker;

    boolean isWarm,
            isDeepLukewarm;

    public ConjureBiomeWarmOceanRitual() {
        super(Biomes.LUKEWARM_OCEAN);
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
        if (isWarm) {
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
    public String getLangName() { return "Conjure Biome: Warm Ocean"; }

    @Override
    public String getLangDescription() {
        return "Creates a warm ocean biome in a circle around the ritual, converting the area to Lukewarm Ocean, Warm Ocean, or Deep Lukewarm Ocean.";
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        checkForBiomeModifier();
        return super.canConsumeItem(stack)
                || (!isWarm && (stack.is(Items.BRAIN_CORAL_FAN.asItem())
                || stack.is(Items.BUBBLE_CORAL_FAN.asItem())
                || stack.is(Items.HORN_CORAL_FAN.asItem())
                || stack.is(Items.FIRE_CORAL_FAN.asItem())
                || stack.is(Items.TUBE_CORAL_FAN.asItem())))
                || (!isDeepLukewarm && (stack.is(Items.BRAIN_CORAL_BLOCK.asItem())
                || stack.is(Items.BUBBLE_CORAL_BLOCK.asItem())
                || stack.is(Items.HORN_CORAL_BLOCK.asItem())
                || stack.is(Items.FIRE_CORAL_BLOCK.asItem())
                || stack.is(Items.TUBE_CORAL_BLOCK.asItem())));
    }

    @Override
    public ResourceLocation getRegistryName() { return new ResourceLocation(ArsNatura.MODID, RitualLib.WARM_OCEAN); }

    @Override
    public void write(CompoundTag tag) {
        super.write(tag);
        tag.putBoolean("isWarm", isWarm);
        tag.putBoolean("isDeepLukewarm", isDeepLukewarm);
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        checkForBiomeModifier();
        this.biome = setBiome();
    }

    private void checkForBiomeModifier() {
        isWarm = getConsumedItems().stream().anyMatch(i ->
                i.is(Items.BRAIN_CORAL_FAN.asItem()) ||
                        i.is(Items.BUBBLE_CORAL_FAN.asItem()) ||
                        i.is(Items.HORN_CORAL_FAN.asItem()) ||
                        i.is(Items.FIRE_CORAL_FAN.asItem()) ||
                        i.is(Items.TUBE_CORAL_FAN.asItem()));

        isDeepLukewarm = getConsumedItems().stream().anyMatch(i ->
                i.is(Items.BRAIN_CORAL_BLOCK.asItem()) ||
                        i.is(Items.BUBBLE_CORAL_BLOCK.asItem()) ||
                        i.is(Items.HORN_CORAL_BLOCK.asItem()) ||
                        i.is(Items.FIRE_CORAL_BLOCK.asItem()) ||
                        i.is(Items.TUBE_CORAL_BLOCK.asItem()));
    }

    private ResourceKey<Biome> setBiome() {
        this.biome = Biomes.LUKEWARM_OCEAN;
        if (isWarm) {
            this.biome = Biomes.WARM_OCEAN;
        } else if (isDeepLukewarm) {
            this.biome = Biomes.DEEP_LUKEWARM_OCEAN;
        }
        return this.biome;
    }
}
