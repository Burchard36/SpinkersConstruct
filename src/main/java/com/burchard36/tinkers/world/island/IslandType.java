package com.burchard36.tinkers.world.island;

public enum IslandType {

    SLIME_EARTH(0);

    int slimeIndex;

    IslandType(int index) {
        this.slimeIndex = index;
    }

    public int getIndex() {
        return this.slimeIndex;
    }
}
