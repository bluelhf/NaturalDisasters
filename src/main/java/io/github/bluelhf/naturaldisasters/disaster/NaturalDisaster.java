package io.github.bluelhf.naturaldisasters.disaster;

import io.github.bluelhf.naturaldisasters.event.NaturalDisasterEvent;
import org.bukkit.Bukkit;

public class NaturalDisaster {
    private boolean spawnNaturally = false;

    /**
     * Gets the name of the natural disaster.
     *
     * @return The name of the disaster as a {@link String}
     */
    public String getName() {
        return null;
    }

    /**
     * Gets whether this disaster will spawn naturally or not.
     * @return True if the disaster will spawn naturally, false otherwise.
     * */
    public final boolean willSpawnNaturally() {
        return spawnNaturally;
    }

    /**
     * Sets whether this disaster will spawn naturally or not.
     * @param doNatural Whether this disaster should spawn naturally or not.
     * */
    public final void setSpawnNaturally(boolean doNatural) {
        spawnNaturally = doNatural;
    }

    /**
     * Creates the natural disaster.
     */
    protected void run() {
    }

    public final void start() {
        NaturalDisasterEvent disasterEvent = new NaturalDisasterEvent(this);
        Bukkit.getPluginManager().callEvent(disasterEvent);
        run();
    }
}
