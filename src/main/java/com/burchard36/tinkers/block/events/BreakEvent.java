package com.burchard36.tinkers.block.events;

import com.burchard36.tinkers.block.BlockRegistry;
import com.burchard36.tinkers.block.TinkersBlock;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BreakEvent implements Listener {

    private final BlockRegistry registry;

    public BreakEvent(final BlockRegistry registry) {
        this.registry = registry;
        this.registry.getPlugin().getServer().getPluginManager().registerEvents(this, this.registry.getPlugin());
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block brokenBlock = event.getBlock();
        final TinkersBlock tinkersBlock = this.registry.getTinkersBlock(brokenBlock);

        if (tinkersBlock == null) return;

        List<ItemStack> drops = tinkersBlock.getDrops();
        final Location blockLocation = brokenBlock.getLocation();

        event.setDropItems(false);
        if (player.getGameMode() != GameMode.CREATIVE)
            blockLocation.getWorld().dropItemNaturally(blockLocation, drops.get(0));
    }
}
