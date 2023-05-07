package dev.nullpointercoding.zdeatharcade.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.ShootingRange.TheRange;
import dev.nullpointercoding.zdeatharcade.Utils.NPCConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.SavePlayerInventoryToFile;
import dev.nullpointercoding.zdeatharcade.Utils.ShootingRangeConfigManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;

public class Commands implements CommandExecutor {

    private Main plugin = Main.getInstance();
    private Integer GUNSMITH_ID = 0;
    private Boolean isRangeSpawnSet = plugin.getIsRangeSpawnSet();
    private List<String> comments = new ArrayList<String>();
    private static HashMap<Player, UUID> playersInRange = TheRange.playersInRange;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("zdeatharcade")) {
            if (!(isPlayerinSpawn(p, "spawn"))) {
                p.sendMessage("§cYou must be in the spawn to use this command.");
                return true;
            }
            if (args.length == 0) {
                p.sendMessage("§9§oDefault Test Message");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("range")) {
                    if (isRangeSpawnSet) {
                        ShootingRangeConfigManager SRCM = new ShootingRangeConfigManager("config.yml");
                        Location locToTPPlayer = new Location(p.getWorld(),
                                SRCM.getConfig().getDouble("SpawnLocation.X"),
                                SRCM.getConfig().getDouble("SpawnLocation.Y") + 1,
                                SRCM.getConfig().getDouble("SpawnLocation.Z"));
                        p.sendTitlePart(TitlePart.TITLE, Component.text("§aWelcome to the shooting range!"));
                        p.sendTitlePart(TitlePart.SUBTITLE, Component.text("§c§oShoot the targets!"));
                        SavePlayerInventoryToFile SPI = new SavePlayerInventoryToFile(p.getUniqueId().toString());
                        SPI.getConfig().set(p.getName() + ".InvAsBase64",
                                SavePlayerInventoryToFile.playerInventoryToBase64(p.getInventory()));
                        SPI.saveConfig();
                        p.getInventory().clear();
                        playersInRange.put(p, p.getUniqueId());
                        p.teleportAsync(locToTPPlayer, TeleportCause.PLUGIN);

                    } else {
                        p.sendMessage("§cThe range spawn has not been set yet.");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("range")) {
                    if (args[1].equalsIgnoreCase("setspawn")) {
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }

                        if (p.getLocation().subtract(p.getLocation(), 0, 1, 0).getBlock().getType() == Material.AIR) {
                            p.sendMessage("§cYou must be standing on a block to set a spawner.");
                            return true;
                        }
                        Double playerX = p.getLocation().getX();
                        Double playerY = p.getLocation().subtract(p.getLocation(), 0, 1, 0).getY();
                        Double playerZ = p.getLocation().getZ();
                        for (File f : plugin.getRangeConfigFolder().listFiles()) {
                            if (f.getName().equalsIgnoreCase("config.yml")) {
                                p.sendMessage("The spawn location for the Shooting Range has already been set.");
                                p.sendMessage(
                                        "§cPlease delete the config.yml file in the Range folder to reset the spawn location.");
                                p.sendMessage(
                                        "Use the Command '/zdeatharcade range resetspawn' to reset the spawn location.");
                                return true;
                            }
                        }
                        ShootingRangeConfigManager SRCM = new ShootingRangeConfigManager("config.yml");
                        SRCM.getConfig().set("SpawnLocation.X", playerX);
                        SRCM.getConfig().set("SpawnLocation.Y", playerY);
                        SRCM.getConfig().set("SpawnLocation.Z", playerZ);
                        SRCM.saveConfig();
                        p.sendMessage("§aSuccessfully set Shooting Range spawn location.");
                        isRangeSpawnSet = true;
                        return true;

                    }
                    if (args[1].equalsIgnoreCase("resetspawn")) {
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }
                        for (File f : plugin.getRangeConfigFolder().listFiles()) {
                            if (f.getName().equalsIgnoreCase("config.yml")) {
                                f.delete();
                                p.sendMessage("§aSuccessfully reset Shooting Range spawn location.");
                                isRangeSpawnSet = false;
                                return true;
                            }
                        }
                        p.sendMessage("§cThe Shooting Range spawn location has not been set.");
                        p.sendMessage("§cUse the Command '/zdeatharcade range setspawn' to set the spawn location.");
                    }
                    if (args[1].equalsIgnoreCase("gunsmith")) {
                        p.sendMessage("§cPlease enter the Vendor ID of the Gunsmith NPC.");
                    }

                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("range")) {
                    if (args[1].equalsIgnoreCase("gunsmith")) {
                        try {
                            GUNSMITH_ID = Integer.parseInt(args[2]);
                            NPCConfigManager NPCM = new NPCConfigManager("gunsmith.yml");
                            NPC gunsmithNPC = CitizensAPI.getNPCRegistry().getById(GUNSMITH_ID);
                            Location npcLOC = gunsmithNPC.getStoredLocation();
                            for (File f : plugin.getNPCDataFolder().listFiles()) {
                                if (f.getName().equalsIgnoreCase("gunsmith.yml")) {
                                    f.delete();
                                    try {
                                        NPCM.updateConfig(f);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    comments.add("NPC Date File used for reading and credential validating");
                                    NPCM.getConfig().set("NPC.Name", gunsmithNPC.getName());
                                    NPCM.getConfig().set("NPC.ID", GUNSMITH_ID);
                                    NPCM.getConfig().set("NPC.Location.X", npcLOC.getX());
                                    NPCM.getConfig().set("NPC.Location.Y", npcLOC.getY());
                                    NPCM.getConfig().set("NPC.Location.Z", npcLOC.getZ());
                                    NPCM.getConfig().set("NPC.Location.World", npcLOC.getWorld().getName());
                                    NPCM.getConfig().setComments("NPC", comments);
                                    NPCM.saveConfig();
                                    p.sendMessage("The Gunsmith has been updated with the ID: " + GUNSMITH_ID);
                                    return true;
                                }
                            }
                            comments.add("NPC Date File used for reading and credential validating");
                            NPCM.getConfig().set("NPC.Name", gunsmithNPC.getName());
                            NPCM.getConfig().set("NPC.ID", GUNSMITH_ID);
                            NPCM.getConfig().set("NPC.Location.X", npcLOC.getX());
                            NPCM.getConfig().set("NPC.Location.Y", npcLOC.getY());
                            NPCM.getConfig().set("NPC.Location.Z", npcLOC.getZ());
                            NPCM.getConfig().set("NPC.Location.World", npcLOC.getWorld().getName());
                            NPCM.getConfig().setComments("NPC", comments);
                            NPCM.saveConfig();
                        } catch (NumberFormatException e) {
                            p.sendMessage("§cVendor ID must be a number.");
                        }
                    }
                }
            } else if (args.length == 4) {
            }
        }
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (!p.hasPermission("zdeatharcade.spawn")) {
                p.sendMessage("§cYou do not have permission to use this command.");
                return true;
            }
            p.sendActionBar(Component.text("§b§oGetting ready..."));
            ;
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.sendTitlePart(TitlePart.TITLE, Component.text("§a§oWelcome back to Spawn"));
                    p.teleportAsync(p.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                }
            }.runTaskLater(plugin, 20 * 3);
        }
        return true;
    }

    private boolean isPlayerinSpawn(Player p, String region) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for (ProtectedRegion pr : set)
            if (pr.getId().equalsIgnoreCase(region))
                return true;
        return false;
    }
}
