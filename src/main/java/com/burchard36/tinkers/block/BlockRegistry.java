package com.burchard36.tinkers.block;

import com.burchard36.tinkers.Tinkers;
import com.burchard36.tinkers.block.events.BreakEvent;
import com.burchard36.tinkers.block.events.PreventStateChange;
import com.burchard36.tinkers.block.exceptions.TinkersBlockAlreadyRegisteredException;
import com.burchard36.Logger;
import com.burchard36.tinkers.block.generators.GenerateOreTask;
import lombok.Getter;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BlockRegistry {

    @Getter
    private final Tinkers plugin;
    @Getter
    public HashMap<TinkersBlockType, TinkersBlock> tinkersBlocks;

    public BlockRegistry(final Tinkers plugin) {
        this.plugin = plugin;
        tinkersBlocks = new HashMap<>();

        Reflections reflect = new Reflections("com.burchard36.tinkers.block.blocks");
        Set<Class<? extends TinkersBlock>> classes = reflect.getSubTypesOf(TinkersBlock.class);

        classes.forEach(clazz -> {
            try {
                final TinkersBlock block = clazz.getDeclaredConstructor().newInstance();
                BlockRegistry.this.registerTinkersBlock(block);
            } catch (InstantiationException | IllegalAccessException
                    | InvocationTargetException | NoSuchMethodException | TinkersBlockAlreadyRegisteredException e) {
                e.printStackTrace();
            }
        });

        new PreventStateChange(this);
        Logger.log("Successfully loaded: " + this.getTinkersBlocks().size() + " tinker's construct blocks");

        Logger.log("Running block generation task...");
        new GenerateOreTask(this);

        Logger.log("Initializing block break task...");
        new BreakEvent(this);
    }

    /**
     * Gets a TinkersBlock that is registered
     * @param type TinkersBlockType to get
     * @return null if none found, TinkersBlock if found
     */
    public final TinkersBlock getTinkersBlock(final TinkersBlockType type) {
        return this.getTinkersBlocks().get(type);
    }

    /**
     * Registers a tinkers block to the Hashmap
     * @param block TinkersBlock to register
     */
    private void registerTinkersBlock(final TinkersBlock block) throws TinkersBlockAlreadyRegisteredException {
        if (this.getTinkersBlock(block.getType()) != null) throw new TinkersBlockAlreadyRegisteredException("Tinkers block: " + block.getType().name() + " is already registered!");
        // TODO: Add more code if necesary
        this.getTinkersBlocks().putIfAbsent(block.getType(), block);
    }

    public boolean isTinkersBlock(final ItemStack stack) {
        if (!(stack.getItemMeta() instanceof final NoteBlock noteBlock)) return false;
        return this.isTinkersBlock(noteBlock.getNote(), noteBlock.getInstrument());
    }

    public boolean isTinkersBlock(final Block block) {
        if (!(block.getBlockData() instanceof final NoteBlock noteBlock)) return false;
        if (block.hasMetadata("tinkers_block")) return true;
        return this.isTinkersBlock(noteBlock.getNote(), noteBlock.getInstrument());
    }

    public boolean isTinkersBlock(final Note note, final Instrument instrument) {
        for (final TinkersBlock tinkersBlock : this.getTinkersBlocks().values()) {
            if (tinkersBlock.matches(note, instrument)) {
                return true;
            }
        }
        return false;
    }

    public final TinkersBlock getTinkersBlock(final Block block) {
        if (block.hasMetadata("tinkers_block")) {
            return this.getTinkersBlock(TinkersBlockType.valueOf(block.getMetadata("tinkers_block").get(0).asString()));
        }
        if (!this.isTinkersBlock(block)) return null;
        TinkersBlock toReturn = null;
        final NoteBlock noteBlock = (NoteBlock) block.getBlockData();
        for (final TinkersBlock tinkersBlock : this.getTinkersBlocks().values()) {
            if (tinkersBlock.matches(noteBlock.getNote(), noteBlock.getInstrument())) toReturn = tinkersBlock;
        }
        return toReturn;
    }

    public final TinkersBlock getTinkersBlock(final ItemStack stack) {
        if (!this.isTinkersBlock(stack)) return null;
        TinkersBlock toReturn = null;
        if (!(stack.getItemMeta() instanceof NoteBlock noteBlock)) return null;
        for (final TinkersBlock tinkersBlock : this.getTinkersBlocks().values()) {
            if (tinkersBlock.matches(noteBlock.getNote(), noteBlock.getInstrument())) toReturn = tinkersBlock;
        }
        return toReturn;
    }

    public final List<TinkersBlock> getNaturallyGeneratingBlocks(final World.Environment env) {
        List<TinkersBlock> blocks = new ArrayList<>();
        this.getTinkersBlocks().values().forEach(tinkersBlock -> {
            if (tinkersBlock.doesNaturallyGenerate() && tinkersBlock.allowedGenerationWorldTypes().contains(env))
                blocks.add(tinkersBlock);
        });
        return blocks;
    }

    public final boolean anyMath(final Note note, final Instrument instrument) {
        for (final TinkersBlock block : this.getTinkersBlocks().values()) {
            if (block.matches(note, instrument)) return true;
        }
        return false;
    }

}
