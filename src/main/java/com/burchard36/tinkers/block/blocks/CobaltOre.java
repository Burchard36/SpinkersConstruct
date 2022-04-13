package com.burchard36.tinkers.block.blocks;

import com.burchard36.tinkers.block.TinkersBlock;
import com.burchard36.tinkers.block.TinkersBlockType;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

public class CobaltOre implements TinkersBlock {

    @Override
    public ItemStack getItemStack() {
        final ItemStack stack = new ItemStack(Material.NOTE_BLOCK);
        final BlockDataMeta dataMeta = (BlockDataMeta) stack.getItemMeta();
        if (dataMeta == null) {
            Bukkit.broadcastMessage("When creating itemstack blockdatameta was null dude");
        }
        final NoteBlock noteBlock = (NoteBlock) Bukkit.createBlockData(Material.NOTE_BLOCK);

        noteBlock.setNote(this.getNote());
        noteBlock.setInstrument(this.getInstrument());
        dataMeta.setCustomModelData(100);
        dataMeta.setBlockData(noteBlock);
        stack.setItemMeta(dataMeta);

        return stack;
    }

    @Override
    public Note getNote() {
        return new Note(0);
    }

    @Override
    public Integer getBlockDurability() {
        return 10;
    }

    @Override
    public Instrument getInstrument() {
        return Instrument.BANJO;
    }

    @Override
    public TinkersBlockType getType() {
        return TinkersBlockType.COBALT_ORE;
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
        if (!(meta instanceof BlockDataMeta)) {
            Bukkit.broadcastMessage("Data wasnt instance of BDM returning null");
            return null;
        }
        final BlockDataMeta dataMeta = (BlockDataMeta) meta;
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
    public List<World.Environment> allowedGenerationWorldTypes() {
        return List.of(World.Environment.NETHER);
    }

    @Override
    public final int getRandomVeinCount() {
        final int maxVeins = 3;
        final int minVeins = 0;
        return ThreadLocalRandom.current().nextInt(maxVeins - minVeins) + minVeins;
    }

    @Override
    public boolean doesNaturallyGenerate() {
        return true;
    }
}
