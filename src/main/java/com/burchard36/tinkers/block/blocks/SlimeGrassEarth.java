package com.burchard36.tinkers.block.blocks;

import com.burchard36.tinkers.block.TinkersBlock;
import com.burchard36.tinkers.block.TinkersBlockType;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SlimeGrassEarth implements TinkersBlock {

    @Override
    public ItemStack getItemStack() {
        final ItemStack item = new ItemStack(Material.NOTE_BLOCK, 1);
        final BlockDataMeta dataMeta = (BlockDataMeta) item.getItemMeta();
        if (dataMeta == null) throw new RuntimeException("BlockDataMeta was null for SlimeGrassEarth block");

        final NoteBlock noteBlock = (NoteBlock) Bukkit.createBlockData(Material.NOTE_BLOCK);

        noteBlock.setNote(this.getNote());
        noteBlock.setInstrument(this.getInstrument());
        dataMeta.setCustomModelData(200);
        dataMeta.setBlockData(noteBlock);
        item.setItemMeta(dataMeta);
        return item;
    }

    @Override
    public Note getNote() {
        return new Note(1);
    }

    @Override
    public Integer getBlockDurability() {
        return 50;
    }

    @Override
    public Instrument getInstrument() {
        return Instrument.SNARE_DRUM;
    }

    @Override
    public TinkersBlockType getType() {
        return TinkersBlockType.SLIME_EARTH_GRASS_BLOCK;
    }

    @Override
    public List<ItemStack> getDrops() {
        return List.of(this.getItemStack());
    }

    @Override
    public BlockData getBlockData() {
        final ItemStack stack = this.getItemStack();
        if (stack.getItemMeta() == null) {
            Bukkit.broadcastMessage("returning null because no itemMeta");
            return null;
        }
        final ItemMeta meta = stack.getItemMeta();
        if (!(meta instanceof final BlockDataMeta dataMeta)) {
            Bukkit.broadcastMessage("Data wasnt instance of BDM returning null");
            return null;
        }
        if (!dataMeta.hasBlockData()) {
            Bukkit.broadcastMessage("Block didnt have meta data, returning null");
            return null;
        }

        return dataMeta.getBlockData(stack.getType());
    }

    @Override
    public boolean matches(Note note, Instrument instrument) {
        return this.getNote().equals(note) && this.getInstrument().equals(instrument);
    }

    @Override
    public boolean doesNaturallyGenerate() {
        return false;
    }

    @Override
    public List<World.Environment> allowedGenerationWorldTypes() {
        return null;
    }

    @Override
    public int getRandomVeinCount() {
        return 0;
    }
}
