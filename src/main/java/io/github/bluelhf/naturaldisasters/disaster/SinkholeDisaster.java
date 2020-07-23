package io.github.bluelhf.naturaldisasters.disaster;

import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedLong;
import com.moderocky.mask.template.Reflective;
import io.github.bluelhf.naturaldisasters.NaturalDisasters;
import io.github.bluelhf.naturaldisasters.util.BlockUtils;
import io.github.bluelhf.naturaldisasters.util.NMSUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SinkholeDisaster extends NaturalDisaster implements LocationedDisaster {
    private Location location;
    private long radius = 5;
    private boolean doFallingBlocks = true;
    @Override public String getName() { return "Sinkhole"; }
    @Override public Location getLocation() { return this.location; }
    @Override public SinkholeDisaster setLocation(Location location) { this.location = location; return this; }

    public SinkholeDisaster() { }

    public long getRadius() { return radius; }
    public SinkholeDisaster setRadius(UnsignedLong newRadius) { this.radius = newRadius.longValue();return this; }

    public boolean doesFallingBlocks() {
        return doFallingBlocks;
    }
    public SinkholeDisaster setDoFallingBlocks(boolean doFallingBlocks) {
        this.doFallingBlocks = doFallingBlocks;
        return this;
    }


    @Override
    protected void run() {
        Random r = new Random();
        Location center = getLocation();
        final int[] y = new int[]{255};
        final List<Block> airedBlocks = new ArrayList<>();
        new BukkitRunnable(){@Override public void run() {
            if (y[0] <3) {
                airedBlocks.forEach(SinkholeDisaster.this::updateFall);
                this.cancel();
            }

            Location loc = center.clone();
            loc.setY(y[0]);
            double localRadius = Math.pow(y[0], 1-(1/((double)radius / 2))) + radius;

            List<Block> blockList = BlockUtils.generateCircle(loc, (int)Math.ceil(localRadius)).stream()
                    .map(Location::getBlock)
                    .filter((b) -> !b.isEmpty())
                    .collect(Collectors.toList());
            while (blockList.size() == 0 && y[0] >= 3) {
                y[0]--;
                loc = center.clone();
                loc.setY(y[0]);
                blockList = BlockUtils.generateCircle(loc, (int)Math.ceil(localRadius)).stream()
                        .map(Location::getBlock)
                        .filter((b) -> !b.isEmpty())
                        .collect(Collectors.toList());
            }
            for (Block b : blockList) {
                double distance = b.getLocation().distance(loc);
                if ((0.9 + r.nextDouble()) * distance < localRadius) {
                    if (y[0] > 8) {
//                        Object craftBlock = NMSUtils.cbClass("block.CraftBlock").cast(b);
//                        Object ibd = Reflective.callMethod(craftBlock, "getNMS").get();
//
//                        Object stepSound = Reflective.callMethod(ibd, "r").get();
//                        Object soundEffectType = NMSUtils.nmsClass("SoundEffectType").cast(stepSound);
//                        Object breakField = Reflective.getField(soundEffectType, "z").get();
//                        Object breakSound = NMSUtils.nmsClass("SoundEffect").cast(breakField);
//
//                        Object keyField = Reflective.getField(breakSound, "a").get();
//                        Object key = NMSUtils.nmsClass("MinecraftKey").cast(keyField);
//                        String keyName = (String) Reflective.callMethod(key, "getKey").get();
//                        Sound soundName = Sound.valueOf(keyName.replace(".", "_").toUpperCase());
//                        b.getWorld().playSound(b.getLocation(), soundName, 5, 0);
                        b.setType(Material.AIR);
                    }
                    if (y[0] <= 8) b.setType(Material.LAVA);
                    updateFall(b);
                    airedBlocks.add(b);
                }
            }
            y[0]--;
        }}.runTaskTimer(NaturalDisasters.getInstance(), 0, 1);
    }

    private void updateFall(Block b) {
        int x = b.getX();
        int z = b.getZ();
        for (int y = 0; y < 255; y++) {
            Block c = b.getWorld().getBlockAt(x, y, z);
            Block above = c.getRelative(0, 1, 0);
            if (c.getType() == Material.AIR) BlockUtils.fall(above);
        }
    }
}
