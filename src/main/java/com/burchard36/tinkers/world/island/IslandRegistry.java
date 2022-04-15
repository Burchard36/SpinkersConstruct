package com.burchard36.tinkers.world.island;

import com.burchard36.tinkers.Tinkers;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class IslandRegistry {

    private final Tinkers plugin;

    public IslandRegistry(final Tinkers pluginInstance) {
        this.plugin = pluginInstance;

        final Reflections reflections = new Reflections("com.burchard36.tinkers.world.island.islands");
        final Set<Class<? extends Island>> classes = reflections.getSubTypesOf(Island.class);

        classes.forEach((clazz) -> {
            try {
                final Island tinkersIsland = clazz.getDeclaredConstructor().newInstance();
                tinkersIsland.registerPluginInstance(this.plugin);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }


}
