package com.burchard36.tinkers.world.island;

import com.burchard36.tinkers.Tinkers;
import com.burchard36.tinkers.world.island.generators.GenerateIslandTask;
import lombok.Getter;
import org.bukkit.World;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class IslandRegistry {

    @Getter
    private final Tinkers plugin;
    @Getter
    private final HashMap<IslandType, Island> islands;

    public IslandRegistry(final Tinkers pluginInstance) {
        this.plugin = pluginInstance;
        this.islands = new HashMap<>();

        final Reflections reflections = new Reflections("com.burchard36.tinkers.world.island.islands");
        final Set<Class<? extends Island>> classes = reflections.getSubTypesOf(Island.class);

        classes.forEach((clazz) -> {
            try {
                final Island tinkersIsland = clazz.getDeclaredConstructor().newInstance();
                tinkersIsland.registerPluginInstance(this.plugin);

                this.islands.putIfAbsent(tinkersIsland.getIslandType(), tinkersIsland);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        new GenerateIslandTask(this);
    }

    public List<Island> getAllowedIslandsForEnvironment(final World.Environment env) {
        final List<Island> islands = new ArrayList<>();
        this.getIslands().values().forEach((island) -> {
            if (island.allowedGenerationEnvironments().contains(env)) islands.add(island);
        });
        return islands;
    }
}
