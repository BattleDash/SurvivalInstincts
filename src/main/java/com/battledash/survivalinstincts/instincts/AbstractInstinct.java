package com.battledash.survivalinstincts.instincts;

import org.bukkit.Location;

public abstract class AbstractInstinct {

    private final Location location;

    public AbstractInstinct(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
