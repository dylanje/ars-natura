package com.dylanensor.ars_natura.datagen;

import com.dylanensor.ars_natura.ArsNatura;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsNatura.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Setup {

    //use runData configuration to generate stuff, event.includeServer() for data, event.includeClient() for assets
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new ArsProviders.PatchouliProvider(gen));
        gen.addProvider(event.includeClient(), new LangGen(gen.getPackOutput(), ArsNatura.MODID, "en_us"));
        gen.addProvider(event.includeServer(), new RecipeData(gen.getPackOutput()));
        gen.addProvider(event.includeClient(), new ItemModelGen(gen.getPackOutput(), ArsNatura.MODID, event.getExistingFileHelper()));
    }

}