package dev.nullpointercoding.zdeatharcade.ShootingRange;

/**
 * TODO: RE WRITE TO WORK WITH BOWS AND CROSSBOWS ONLY
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.SavePlayerInventoryToFile;
import me.zombie_striker.qg.api.QAWeaponDamageBlockEvent;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.guns.projectiles.RealtimeCalculationProjectile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class TheRange implements Listener {

    public static HashMap<Player, UUID> playersInRange = new HashMap<Player, UUID>();
    private Location cameFrom;
    private Main plugin = Main.getInstance();

    public TheRange() {
    }

    @EventHandler
    public void playerTP(PlayerTeleportEvent e) {
        if (playersInRange.containsKey(e.getPlayer())) {
            cameFrom = e.getFrom();
        }

    }

    @EventHandler
    public void onPlayerRangeSignRightClick(PlayerInteractEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getClickedBlock() == null) {
            return;
        }
        if (e.getClickedBlock().getType().toString().contains("SIGN")) {
            if (isBlockinRegion(e.getClickedBlock().getLocation(), "shootingrange")) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                if (sign.line(0).equals(Component.text("§6[§eAmmo§6]"))) {
                    for (ItemStack is : p.getInventory().getContents()) {
                        if (is == null) {
                            continue;
                        }
                        if (QualityArmory.isGun(is)) {
                            Gun g = QualityArmory.getGun(is);
                            QualityArmory.addAmmoToInventory(p, g.getAmmoType(), 50);
                            p.sendMessage("§aYou have been given 50 " + g.getAmmoType().getName() + " ammo");
                        }
                    }
                }
                if (sign.line(0).equals(Component.text("§6[§cExit§6]"))) {
                    if (playersInRange.containsKey(p)) {
                        SavePlayerInventoryToFile SPI = new SavePlayerInventoryToFile(p.getUniqueId().toString());
                        try {
                            p.getInventory().setContents(SavePlayerInventoryToFile.itemStackArrayFromBase64(
                                    SPI.getConfig().getString(p.getName() + ".InvAsBase64")));

                            SPI.deletePlayerInventoryFile();

                        } catch (IllegalArgumentException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        p.teleportAsync(cameFrom, TeleportCause.PLUGIN);
                        playersInRange.remove(p);

                    } else {
                        p.sendMessage("§cYou are not in the shooting range!");
                    }
                }

            }
        }
    }

    @EventHandler
    public void setGunSigns(SignChangeEvent e) {
        if (!(isBlockinRegion(e.getBlock().getLocation(), "shootingrange"))) {
            return;
        }
        if (e.line(0).equals(Component.text("[ammo]"))) {
            e.line(0, Component.text("§6[§eAmmo§6]"));
            e.line(1, Component.text("Refill Ammo Here", TextColor.color(0, 0, 0)));
        }
        if (e.line(0).equals(Component.text("[exit]"))) {
            e.line(0, Component.text("§6[§cExit§6]"));
            e.line(1, Component.text("Click to Leave", TextColor.color(0, 0, 0)));
        }

    }

    @EventHandler
    public void onPlayerDropinRange(PlayerDropItemEvent e) {
        if (playersInRange.containsKey(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onTargetHitEvent(QAWeaponDamageBlockEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if (!(playersInRange.containsKey(p)))
            return;

        if (b.getType() == Material.TARGET) {
            Location blockLoc = b.getLocation();
            double radius = 0.4;
            for (double t = 0; t <= 2 * Math.PI * radius; t += 0.05) {
                double x = (radius * Math.cos(t)) + blockLoc.getX();
                double z = (blockLoc.getZ() + radius * Math.sin(t));
                Location particleLoc = new Location(blockLoc.getWorld(), x, blockLoc.getY() + 1, z);
                Particle particle = Particle.REDSTONE;
                DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 0, 0), 1);
                blockLoc.getWorld().spawnParticle(particle, particleLoc, 50, dustOptions);
                p.getWorld().playSound(p, Sound.BLOCK_ANVIL_HIT, (float) 1.0, (float) 1.0);
            }
            blockLoc.getBlock().setType(Material.AIR);
            p.sendActionBar(Component.text("§a§lHIT!"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    blockLoc.getBlock().setType(Material.TARGET);
                    blockLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, blockLoc.add(blockLoc, 0.4, 1.2, 0.8),
                            10);
                    blockLoc.getWorld().playSound(blockLoc, Sound.ITEM_ARMOR_EQUIP_CHAIN, (float) 4.0, (float) 0.0);
                }
            }.runTaskLater(plugin, 20 * 3);
        }
    }

    /**
     * This method is used to check if the Block that was shot is a indeed a
     * {@link Material#TARGET}
     * 
     * @param e
     */

    @EventHandler
    public void onTargetHit(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            if (playersInRange.containsKey(p)) {
                RealtimeCalculationProjectile pro = (RealtimeCalculationProjectile) e.getEntity();
                Bukkit.getConsoleSender().sendMessage("§aProjectile: " + pro.getName().toString() + p.getName());

            }
        }

    }

    /*
     * This method is not used, but I'm keeping it here for reference.
     * Used to check if a player is in a region.
     */
    // private boolean isPlayerAtTheRange(Player p, String region) {
    // RegionContainer container =
    // WorldGuard.getInstance().getPlatform().getRegionContainer();
    // RegionQuery query = container.createQuery();
    // ApplicableRegionSet set =
    // query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
    // for (ProtectedRegion pr : set) if (pr.getId().equalsIgnoreCase(region))
    // return true;
    // return false;
    // }
    private boolean isBlockinRegion(Location location, String region) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));
        for (ProtectedRegion pr : set)
            if (pr.getId().equalsIgnoreCase(region))
                return true;
        return false;
    }
}
