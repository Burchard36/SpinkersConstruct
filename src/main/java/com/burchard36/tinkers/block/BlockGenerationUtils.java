package com.burchard36.tinkers.block;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class BlockGenerationUtils {

    public static CompletableFuture<Block> getRandomNetherBlock(final Chunk chunk) {
        final ChunkSnapshot snap = chunk.getChunkSnapshot();
        final ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int x = 0; x <= 15; x++) {
            for (int z = 0; z <= 15; z++) {

                boolean generatingOnSurface = false;
                int airBlocksHit = 0; // need to detect if were actually generation on the floor or not, hard limit we'll try 10
                for (int y = 126; y >= 0; y--) {
                    final Material blockType = snap.getBlockType(x, y, z);
                    switch (blockType.name()) {
                        case "NETHERRACK", "NETHER_QUARTZ_ORE",
                                "NOTE_BLOCK", "SOUL_SOIL",
                                "NETHER_GOLD_ORE" -> { // Use named enums so version check are more safe
                            if (generatingOnSurface && airBlocksHit >= 10) {
                                final int locX = x;
                                final int locY = y;
                                final int locZ = z;
                                return CompletableFuture.supplyAsync(() -> chunk.getBlock(locX, locY, locZ));
                            }
                        }
                        case "AIR" -> {
                            if (generatingOnSurface) {
                                airBlocksHit++;
                                continue;
                            }

                            final int chance = rand.nextInt(100);
                            if (chance >= 51) {// generate on the surface
                                final int locX = x;
                                final int locY = y + 1;
                                final int locZ = z;
                                return CompletableFuture.supplyAsync(() -> chunk.getBlock(locX, locY, locZ));
                            } else generatingOnSurface = true;
                        }
                    }
                }
            }
        }

        return CompletableFuture.supplyAsync(() -> null);
    }
}
