package io.github.bluelhf.naturaldisasters.util;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockUtils {

    public static List<Location> generateCircle(Location centerBlock, int radius) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) { for (int z = bz - radius; z <= bz + radius; z++) {
            double distance = ((bx-x)*(bx-x)) + ((bz-z) * (bz-z));
            if (distance < radius * radius) {
                circleBlocks.add(new Location(centerBlock.getWorld(), x, by, z));
            }
        }}
        return circleBlocks;
    }
    public static List<Location> generateSphere(Location centerBlock, int radius) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) { for (int y = by - radius; y <= by + radius; y++) { for (int z = bz - radius; z <= bz + radius; z++) {
            double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));
            if (distance < radius * radius) { circleBlocks.add(new Location(centerBlock.getWorld(), x, y, z)); }
        }}}

        return circleBlocks;
    }

    public static Location randomLocation(World w) {
        Random r = new Random();
        Chunk[] chunks = w.getLoadedChunks();
        Chunk rChunk = chunks[r.nextInt(chunks.length)];
        Block rBlock = rChunk.getBlock(r.nextInt(16), 0, r.nextInt(16));
        return w.getHighestBlockAt(rBlock.getLocation()).getLocation();
    }

    public static Location randomLocation() {
        Random r = new Random();
        World rWorld = Bukkit.getWorlds().get(r.nextInt(Bukkit.getWorlds().size()));
        return randomLocation(rWorld);
    }

    public static void fall(Block b) {
        if (b.getType() == Material.AIR) return;
        BlockData bd = b.getBlockData().clone();
        b.setType(Material.AIR);
        b.getWorld().spawnFallingBlock(b.getLocation(), bd).setDropItem(false);
    }
}
