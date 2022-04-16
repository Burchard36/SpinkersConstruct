package com.burchard36.tinkers.block;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.util.List;

//TODO Maybe make this abstract and make redundant methods already created here eg  matches() and getBlockData()
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
