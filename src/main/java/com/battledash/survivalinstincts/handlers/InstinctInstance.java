package com.battledash.survivalinstincts.handlers;

import com.battledash.survivalinstincts.SurvivalInstincts;
import com.battledash.survivalinstincts.instincts.WaypointInstinct;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InstinctInstance {

    private final Player player;

    public InstinctInstance(Player player) {
        this.player = player;
    }

    public void start() {
        this.playStartEffect();
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(SurvivalInstincts.getInstance(), this::tick, 0L, 1L);
        Bukkit.getScheduler().runTaskLater(SurvivalInstincts.getInstance(), () -> {
            this.playEndEffect();
            task.cancel();
            SurvivalInstincts.getInstance().getInstinctDataHandler().removeInstinctData(player);
        }, 3 * 20L);
    }

    public void tick() {
        final InstinctData instinctData = SurvivalInstincts.getInstance().getInstinctDataHandler().getInstinctData(player);
        for (WaypointInstinct instinct : instinctData.getInstinctsOfType(WaypointInstinct.class)) {
            final Location clone = instinct.getLocation().clone().add(0.5, 0, 0.5);
            clone.setY(0);
            for (int y = 0; y < 512; y++) {
                clone.add(0, 0.5, 0);
                // 1, 1, 0 = 255, 255, 0 (Yellow)
                player.spawnParticle(Particle.REDSTONE, clone, 0, 1, 1, 0, 1);
            }
        }
    }

    /**
     * We set the time *and* send it here because it doesn't resend the time packet instantly, and if we don't set it, it gets undone in a second
     */
    public void playStartEffect() {
        player.setPlayerTime(18000, false);
        final org.bukkit.World world = player.getLocation().getWorld();
        final Chunk chunk = player.getLocation().getChunk();
        final PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        playerConnection.sendPacket(new PacketPlayOutUpdateTime(((CraftWorld) world).getHandle().getTime(),
                18000,
                ((CraftWorld) world).getHandle().getGameRules().getBoolean("doDaylightCycle")));
        for (int x = chunk.getX() - Bukkit.getViewDistance(); x < chunk.getX() + Bukkit.getViewDistance(); x++) {
            for (int z = chunk.getZ() - Bukkit.getViewDistance(); z < chunk.getZ() + Bukkit.getViewDistance(); z++) {
                final Chunk chunkAt = world.getChunkAt(x, z);
                playerConnection.sendPacket(createChunkUpdatePacket(chunkAt, false));
                for (Entity entity : chunkAt.getEntities()) {

                }
            }
        }
    }

    public void playEndEffect() {
        player.setPlayerTime(0, true);
        final org.bukkit.World world = player.getLocation().getWorld();
        final Chunk chunk = player.getLocation().getChunk();
        final PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        playerConnection.sendPacket(new PacketPlayOutUpdateTime(((CraftWorld) world).getHandle().getTime(),
                ((CraftWorld) world).getHandle().getDayTime(),
                ((CraftWorld) world).getHandle().getGameRules().getBoolean("doDaylightCycle")));
        for (int x = chunk.getX() - Bukkit.getViewDistance(); x < chunk.getX() + Bukkit.getViewDistance(); x++) {
            for (int z = chunk.getZ() - Bukkit.getViewDistance(); z < chunk.getZ() + Bukkit.getViewDistance(); z++) {
                playerConnection.sendPacket(createChunkUpdatePacket(world.getChunkAt(x, z), true));
            }
        }
    }

    private PacketPlayOutMapChunk createChunkUpdatePacket(Chunk chunk, boolean hasLight) {
        final net.minecraft.server.v1_12_R1.Chunk handle = ((CraftChunk) chunk).getHandle();
        List<ChunkSectionWrapper> sections = Arrays.stream(handle.getSections()).filter(Objects::nonNull).map(ChunkSectionWrapper::new).collect(Collectors.toList());
        for (ChunkSectionWrapper section : sections) {
            if (!hasLight)
                section.removeLight();
        }
        final PacketPlayOutMapChunk packetPlayOutMapChunk = new PacketPlayOutMapChunk(handle, '\uffff');
        for (ChunkSectionWrapper section : sections) {
            if (!hasLight)
                section.revertLight();
        }
        return packetPlayOutMapChunk;
    }

}
