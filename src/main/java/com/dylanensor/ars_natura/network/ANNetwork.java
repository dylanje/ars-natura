package com.dylanensor.ars_natura.network;

import com.dylanensor.ars_natura.ArsNatura;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ANNetwork {
    public static SimpleChannel INSTANCE;

    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ArsNatura.MODID, "network"), () -> "1.0", s -> true, s-> true);
    }
}
