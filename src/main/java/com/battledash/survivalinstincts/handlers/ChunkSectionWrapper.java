package com.battledash.survivalinstincts.handlers;

import net.minecraft.server.v1_12_R1.ChunkSection;
import net.minecraft.server.v1_12_R1.NibbleArray;

public class ChunkSectionWrapper {
    private final ChunkSection chunkSection;
    private final byte[] emittedLight;
    private final byte[] skyLight;

    public ChunkSectionWrapper(ChunkSection chunkSection) {
        this.chunkSection = chunkSection;
        this.emittedLight = chunkSection.getEmittedLightArray().asBytes().clone();
        this.skyLight = chunkSection.getSkyLightArray().asBytes().clone();
    }

    public void removeLight() {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    chunkSection.getEmittedLightArray().a(x, y, z, 1);
                    chunkSection.getSkyLightArray().a(x, y, z, 1);
                }
            }
        }
    }

    public void revertLight() {
        chunkSection.a(new NibbleArray(emittedLight));
        chunkSection.b(new NibbleArray(skyLight));
    }
}
