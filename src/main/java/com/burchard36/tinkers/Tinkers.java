package com.burchard36.tinkers;

import com.burchard36.tinkers.block.BlockRegistry;
import com.burchard36.Api;
import com.burchard36.Logger;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import org.bukkit.*;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Tinkers extends JavaPlugin implements Listener, Api {

    @Getter
    private BlockRegistry registry;

    @Getter
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Logger.init(this);

        if (!this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            Logger.error("ProtocolLib is required to use this plugin! Please install it.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        this.registry = new BlockRegistry(this);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final ItemStack customBlock = new ItemStack(Material.NOTE_BLOCK, 64);
        final ItemMeta meta = customBlock.getItemMeta();
        if (meta == null) return;

        final BlockDataMeta noteBlock = (BlockDataMeta) meta;
        final NoteBlock note = (NoteBlock) Bukkit.createBlockData(Material.NOTE_BLOCK);
        note.setInstrument(Instrument.BANJO);

        note.setNote(new Note(0));
        noteBlock.setCustomModelData(100); // this is just for setting the item in hands texture
        noteBlock.setBlockData(note);

        customBlock.setItemMeta(meta);

        event.getPlayer().getInventory().addItem(customBlock);
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public String loggerPrefix() {
        return "TinkersConstructSpigotPort";
    }
}
