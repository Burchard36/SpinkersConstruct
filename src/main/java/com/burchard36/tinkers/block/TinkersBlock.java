package com.burchard36.tinkers.block;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;

import java.util.List;
import java.util.Random;

public interface TinkersBlock {

    ItemStack getItemStack();
    Note getNote();
    Integer getBlockDurability();
    Instrument getInstrument();
    TinkersBlockType getType();
    List<ItemStack> getDrops();
    BlockData getBlockData();
    boolean matches(Note note, Instrument instrument);
    boolean doesNaturallyGenerate();
    List<World.Environment> allowedGenerationWorldTypes();
    int getRandomVeinCount();
}
