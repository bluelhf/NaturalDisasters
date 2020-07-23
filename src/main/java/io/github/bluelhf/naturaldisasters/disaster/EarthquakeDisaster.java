package io.github.bluelhf.naturaldisasters.disaster;

import io.github.bluelhf.naturaldisasters.NaturalDisasters;
import io.github.bluelhf.naturaldisasters.util.BlockUtils;
import io.github.bluelhf.naturaldisasters.util.BlueMath;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EarthquakeDisaster extends NaturalDisaster implements LocationedDisaster {
    private long radius = 50;
    private Location location;
    private long duration = 10;
    @Override public String getName() { return "Earthquake"; }
    @Override public Location getLocation() { return this.location; }
    @Override public EarthquakeDisaster setLocation(Location location) { this.location = location; return this; }
    public long getRadius() { return this.radius; }
    public EarthquakeDisaster setRadius(long radius) { this.radius = radius; return this; }
    public long getDuration() { return this.duration; }
    public EarthquakeDisaster setDuration(long duration) { this.duration = duration; return this; }

    @Override
    protected void run() {
        final Location centre = this.location != null ? this.location : BlockUtils.randomLocation();
        final World w = centre.getWorld();
        final long start = System.currentTimeMillis();
        BlueMath.Random rand = new BlueMath.Random();
        new BukkitRunnable() {@Override public void run() {
            if (System.currentTimeMillis() > start+duration*1000) {
                this.cancel();
                return;
            }
            Collection<Entity> entities = w.getNearbyEntities(centre, radius/2F, w.getMaxHeight()/2F, radius/2F, e -> {
                double dSq = e.getLocation().distanceSquared(centre);
                if (dSq > radius*radius) return false;
                return true;
            });

            for (Entity e : entities) {
                if (e instanceof LivingEntity) {
                    LivingEntity livingE = (LivingEntity)e;
                    livingE.damage(0.25);
                    if (!livingE.hasPotionEffect(PotionEffectType.CONFUSION)) {
                        int ticksRemaining = (int) (((start+duration*1000)-System.currentTimeMillis())/50);
                        livingE.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, ticksRemaining+40, 255, true, false));
                    }
                }
                if (e instanceof Player) {
                    ((Player) e).playSound(e.getLocation(), Sound.ENTITY_BOAT_PADDLE_LAND, 10, 0);
                }
                e.setVelocity(e.getVelocity().add(new Vector(
                        rand.randomDouble(-0.1, 0.1),
                        rand.randomDouble(-0.1, 0.3),
                        rand.randomDouble(-0.1, 0.1)
                )));
            }



        }}.runTaskTimer(NaturalDisasters.getInstance(), 0, 3);
    }
}
