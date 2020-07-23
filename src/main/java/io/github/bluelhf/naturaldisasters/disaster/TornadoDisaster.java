package io.github.bluelhf.naturaldisasters.disaster;

import io.github.bluelhf.naturaldisasters.util.BlockUtils;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class TornadoDisaster extends NaturalDisaster implements LocationedDisaster {
    private Location location;
    @Override public String getName() { return "Tornado"; }
    @Override public Location getLocation() { return this.location; }
    @Override public TornadoDisaster setLocation(Location location) { this.location = location; return this; }

    @Override
    protected void run() {
        Location centre = this.location != null ? this.location : BlockUtils.randomLocation();
        //TODO add
    }

    class TornadoLine {
        public double theta = 0;
        public double radius = 0;
        public double y = 0;
        public Location centre;

        public TornadoLine(Location centre, double radius) {
            this.centre = centre;
            this.radius = radius;
        }

        public Location tick() {
            radius = Math.pow(y, 1.5);
            theta = 1.2*y;

            double x = radius*Math.cos(theta);
            double z = radius*Math.sin(theta);

            y += 0.1;
        }
    }
}
