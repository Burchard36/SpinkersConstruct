package com.burchard36.tinkers.block.generators;

import com.burchard36.Logger;
import com.burchard36.tinkers.block.BlockRegistry;
import com.burchard36.tinkers.block.BlockGenerationUtils;
import com.burchard36.tinkers.block.TinkersBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class GenerateOreTask implements Listener {

    private final BlockRegistry registry;

    public GenerateOreTask(final BlockRegistry registry) {
        this.registry = registry;
        this.registry.getPlugin().getServer().getPluginManager().registerEvents(this, this.registry.getPlugin());
    }

    @EventHandler
    public void onGenerate(final ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        if (!event.isNewChunk()) return;
        final World.Environment chunkEnvironment = chunk.getWorld().getEnvironment();
        final List<TinkersBlock> naturallyGeneratingBlocks = this.registry.getNaturallyGeneratingBlocks(chunkEnvironment);
        if (naturallyGeneratingBlocks.isEmpty()) return;

        naturallyGeneratingBlocks.forEach((tinkersBlock) -> {
            switch (chunkEnvironment) {
                case NETHER -> {
                    for (int x = tinkersBlock.getRandomVeinCount(); x --> 0;) {
                        CompletableFuture<Block> futureBlock = BlockGenerationUtils.getRandomNetherBlock(chunk);

                        final Block block = futureBlock.join();
                        if (block != null) {
                            block.setType(Material.NOTE_BLOCK, false);
                            block.setMetadata("tinkers_block", new FixedMetadataValue(registry.getPlugin(), tinkersBlock.getType().name()));
                            block.setBlockData(tinkersBlock.getBlockData(), false);
                        }
                    }
                }

                case NORMAL -> {}

                case THE_END -> {}
            }
        });
    }
}
