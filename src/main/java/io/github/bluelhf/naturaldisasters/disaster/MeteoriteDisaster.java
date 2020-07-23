package io.github.bluelhf.naturaldisasters.disaster;

import io.github.bluelhf.naturaldisasters.NaturalDisasters;
import io.github.bluelhf.naturaldisasters.util.BlockUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MeteoriteDisaster extends NaturalDisaster implements LocationedDisaster {
    Location location = null;
    float force = 20F;
    boolean fiery = true;

    public MeteoriteDisaster setLocation(Location location) { this.location = location; return this; }
    public @Nullable Location getLocation() { return location; }
    @Override public String getName() { return "Meteorite"; }

    public MeteoriteDisaster() { }

    public boolean isFiery() { return fiery; }
    public MeteoriteDisaster setFiery(boolean fiery) { this.fiery = fiery; return this; }
    public float getForce() { return force; }
    public MeteoriteDisaster setForce(float force) { this.force = force; return this; }


    @Override
    protected void run() {
        Random r = new Random();

        Location loc = this.location != null ? this.location : BlockUtils.randomLocation();
        World world = loc.getWorld();

        Location startLocation = loc.clone();
        startLocation.add((r.nextDouble()*2D-1D)*100D, 200D, (r.nextFloat()*2D-1D)*100D);

        final Location endLocation = loc.clone();
        final Vector between = endLocation.toVector().subtract(startLocation.toVector());

        final int[] i = {0};
        final float explosionForce = this.force;
        final boolean explosionFiery = this.fiery;
        float particleRadius = this.force / 15; // Gotta love a magic number
        int max = 50;
        new BukkitRunnable() {@Override public void run() {
            Vector cOff = between.clone().multiply(i[0] / (double)max);
            Location cLocation = startLocation.toVector().add(cOff).toLocation(world);
            List<Block> collisionSphere = BlockUtils.generateSphere(cLocation, (int) particleRadius).stream()
                    .map(Location::getBlock)
                    .filter((b) -> !b.isEmpty())
                    .collect(Collectors.toList());
            if (i[0] >= max || collisionSphere.size() > 0) {
                for (Block b : BlockUtils.generateSphere(cLocation, (int)explosionForce).stream()
                        .map(Location::getBlock)
                        .filter((b) -> !b.isEmpty())
                        .collect(Collectors.toList())) {
                    if (Math.random() < 0.2) {
                        Object fb = b.getType() == Material.TNT
                                        ? b.getWorld().spawn(b.getLocation().add(0,1,0), TNTPrimed.class)
                                        : b.getWorld().spawnFallingBlock(b.getLocation().add(0,1,0), b.getBlockData());

                        Vector other = b.getLocation().toVector().subtract(endLocation.toVector());
                        ((Entity)fb).setVelocity(other.add(new Vector(0, 0.5, 0)).divide(new Vector(2, 2, 2)));
                    }
                    b.setType(Material.AIR);
                }
                world.playSound(cLocation, Sound.ENTITY_GENERIC_EXPLODE, 8, 0);
                world.createExplosion(cLocation, explosionForce, explosionFiery);
                this.cancel();
            }

            world.spawnParticle(Particle.FLAME, cLocation, 100, particleRadius + 0.3, particleRadius + 0.3, particleRadius + 0.3, 0, null, true);
            world.spawnParticle(Particle.SQUID_INK, cLocation, 1000, particleRadius, particleRadius, particleRadius, 0, null, true);
            world.playSound(cLocation, Sound.ENTITY_BOAT_PADDLE_LAND, 8, 0);
            i[0]++;
        }}.runTaskTimer(NaturalDisasters.getInstance(), 0, 1);
    }
}
