package io.github.bluelhf.naturaldisasters.disaster;

import org.bukkit.Location;

public interface LocationedDisaster {
    Location location = null;
    public Location getLocation();
    public LocationedDisaster setLocation(Location location);
}
