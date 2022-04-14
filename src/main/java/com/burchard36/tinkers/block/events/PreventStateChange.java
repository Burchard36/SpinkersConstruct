package com.burchard36.tinkers.block.events;

import org.bukkit.block.BlockFace;
import com.burchard36.tinkers.block.BlockRegistry;
import com.burchard36.tinkers.block.TinkersBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;


public record PreventStateChange(BlockRegistry registry) implements Listener {

    public PreventStateChange(final BlockRegistry registry) {
        this.registry = registry;
        this.registry.getPlugin().getServer().getPluginManager().registerEvents(this, this.registry.getPlugin());

        //TODO: Try out ProtocolLib again
        /*ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(this.registry.getPlugin(), ListenerPriority.HIGHEST,
                        PacketType.Play.Server.BLOCK_CHANGE) {


                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() ==
                                PacketType.Play.Server.BLOCK_CHANGE) {

                            final WrappedBlockData blockData = event.getPacket().getBlockData().read(0);
                            if (blockData.getType() == Material.NOTE_BLOCK) {
                                final BlockPosition position = event.getPacket().getBlockPositionModifier().read(0);
                                BlockState noteBlock = (BlockState) blockData.getHandle();
                                BlockPosition pos = event.getPacket().getSectionPositions().read(0);

                                final World world = event.getPlayer().getWorld();
                                final Block block = new Location(world, pos.getX(), pos.getY(), pos.getZ()).getBlock();
                                final TinkersBlock tinkersBlock = registry.getTinkersBlock(block);
                                if (tinkersBlock != null) {
                                    Bukkit.broadcastMessage("We are good to go my dudes");
                                    final WrappedBlockData wrappedData = WrappedBlockData.createData(tinkersBlock.getBlockData());
                                }
                            }
                        }
                    }
                });*/
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChange(final BlockPhysicsEvent event) {
        final Block changedBlock = event.getBlock();
        final Block blockAbove = changedBlock.getLocation().add(0, 1, 0).getBlock();
        if (changedBlock.getType() == Material.NOTE_BLOCK) {
            event.setCancelled(true);
            final TinkersBlock block = this.registry.getTinkersBlock(changedBlock);
            if (block == null) {
                Bukkit.broadcastMessage("Couldn't find the block!");
                return;
            }
            changedBlock.setBlockData(block.getBlockData(), false);
            changedBlock.getState().update(true, false);
            this.updateForAll(changedBlock);
        }

        if (blockAbove.getType() == Material.NOTE_BLOCK) {
            event.setCancelled(true);
            checkBlock(blockAbove);
        }
    }

    @EventHandler
    public void onNotePlayer(final NotePlayEvent event) {
        if (this.registry.isTinkersBlock(event.getNote(), event.getInstrument())) event.setCancelled(true);
    }

    @EventHandler
    public void onStateChange(final PlayerInteractEvent event) {
        final Block clickedBlock = event.getClickedBlock();
        final ItemStack itemInHand = event.getItem();
        if (clickedBlock == null) return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (!(clickedBlock.getType().name().equalsIgnoreCase("NOTE_BLOCK"))) return;

        if (!this.registry.isTinkersBlock(clickedBlock)) return;

        if (event.isBlockInHand() && itemInHand != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final TinkersBlock block = this.registry.getTinkersBlock(clickedBlock);
            if (block != null) {
                event.setCancelled(true);

                final BlockFace face = event.getBlockFace();
                final Location location = clickedBlock.getRelative(face).getLocation();
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                location.getBlock().setType(itemInHand.getType(), false);
                if (itemInHand.getItemMeta() != null) {
                    if (itemInHand.getItemMeta() instanceof BlockDataMeta)
                        if (((BlockDataMeta) itemInHand.getItemMeta()).hasBlockData())
                            location.getBlock().setBlockData(((BlockDataMeta) itemInHand.getItemMeta()).getBlockData(itemInHand.getType()), false);

                }
                event.getPlayer().playSound(location, Sound.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            }
        } else if (event.getAction() != Action.LEFT_CLICK_BLOCK) event.setCancelled(true);
    }

    private void checkBlock(final Block block) {
        final TinkersBlock tinkersBlock = this.registry.getTinkersBlock(block);
        if (tinkersBlock == null) return;

        block.setBlockData(tinkersBlock.getBlockData());
        block.getState().update(true, true);
        this.updateForAll(block);
        final Block nextBlock = block.getLocation().add(0, 1, 0).getBlock();
        if (nextBlock.getType() == Material.NOTE_BLOCK) this.checkBlock(nextBlock);
    }

    private void updateForAll(final Block block) {
        final World world = block.getLocation().getWorld();
        if (world == null) throw new RuntimeException("World did not exist when updating! Did you unload the world?");
        final int chunkDistance = world.getViewDistance();
        world.getNearbyEntities(block.getLocation(), chunkDistance * 16, world.getMaxHeight(), chunkDistance * 16).forEach((entity) -> {
            if (!(entity instanceof Player)) return;
            ((Player) entity).sendBlockChange(block.getLocation(), block.getBlockData());
        });
    }
}
