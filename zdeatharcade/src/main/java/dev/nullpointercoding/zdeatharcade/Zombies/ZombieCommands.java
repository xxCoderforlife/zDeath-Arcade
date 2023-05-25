package dev.nullpointercoding.zdeatharcade.Zombies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.MainConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.ZombieSpawnLocationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ZombieCommands implements TabExecutor {

    private Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("zombie")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission("zdeatharcade.admin"))) {
                    p.sendMessage("§cYou do not have permission to use this command.");
                    return true;
                }
                if (args.length == 0) {
                    p.sendMessage(Component.text("§eUsage: §6/zdeatharcade zombie <setspawner/killall>"));
                    return true;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("setspawner")) {
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }

                        if (p.getLocation().subtract(p.getLocation(), 0, 1, 0).getBlock().getType() == Material.AIR) {
                            p.sendMessage("§cYou must be standing on a block to set a spawner.");
                            return true;
                        }
                        String blockName = p.getLocation().subtract(p.getLocation(), 0, 1, 0).getBlock().getType()
                                .name();
                        Double playerX = p.getLocation().getX();
                        Double playerY = p.getLocation().subtract(p.getLocation(), 0, 1, 0).getY();
                        Double playerZ = p.getLocation().getZ();
                        for (File f : plugin.getZombieSpawnLocationFolder().listFiles()) {
                            if (f.getName().equalsIgnoreCase(blockName + " X: " + Double.toString(playerX) +
                                    " Y: " + Double.toString(playerY) + " Z: " + Double.toString(playerZ) + ".yml")) {
                                p.sendMessage("§cThis spawner already exists.");
                                return true;
                            }

                        }

                        ZombieSpawnLocationManager ZSLM = new ZombieSpawnLocationManager(
                                blockName + " X: " + Double.toString(playerX) +
                                        " Y: " + Double.toString(playerY) + " Z: " + Double.toString(playerZ) + ".yml");

                        ZSLM.getConfig().set("X", playerX);
                        ZSLM.getConfig().set("Y", playerY);
                        ZSLM.getConfig().set("Z", playerZ);
                        ZSLM.saveConfig();
                        p.sendMessage("§aSuccessfully set zombie spawner.");
                        p.sendMessage("There are now " + ZSLM.getZombieSpawnLocationFolder().listFiles().length
                                + " zombie spawners.");
                    }
                    if (args[0].equalsIgnoreCase("killall")) {
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }
                        for (Entity z : p.getWorld().getEntitiesByClass(Zombie.class)) {
                            z.remove();
                        }
                        p.sendMessage("§aSuccessfully removed all zombies.");
                    }
                    if (args[0].equalsIgnoreCase("spawnlimit")) {
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }
                        p.sendMessage(Component.text("The current spawn limit is: ", NamedTextColor.YELLOW)
                                .append(Component.text(MainConfigManager.getZombieSpawnLimit().toString(),
                                        NamedTextColor.GOLD)));
                    }
                    if(args[0].equalsIgnoreCase("spawned")){
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }
                        p.sendMessage(Component.text("The current number of spawned zombies is: ", NamedTextColor.YELLOW)
                                .append(Component.text(p.getWorld().getEntitiesByClass(Zombie.class).size(),
                                        NamedTextColor.GOLD)));
                        }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("spawnlimit")) {
                        if (args[1].equalsIgnoreCase("set")) {
                            if (!(p.hasPermission("zdeatharcade.admin"))) {
                                p.sendMessage("§cYou do not have permission to use this command.");
                                return true;
                            }
                            p.sendMessage("§cUsage: /zombie spawnlimit set <number>");
                            return true;
                        }

                    }
                }
                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("spawnlimit")) {
                        if (args[1].equalsIgnoreCase("set")) {
                            if (!(p.hasPermission("zdeatharcade.admin"))) {
                                p.sendMessage("§cYou do not have permission to use this command.");
                                return true;
                            }
                            try {
                                Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage("§cYou must enter a number.");
                                return true;
                            }
                            MainConfigManager mCM = new MainConfigManager();
                            ZombieHandler.resetSpawnCount();
                            for (Entity z : p.getWorld().getEntitiesByClass(Zombie.class)) {
                                z.remove();
                            }
                            mCM.setZombieSpawnLimit(Integer.parseInt(args[2]));
                            p.sendMessage("§aSuccessfully set spawn limit to " + args[2] + ".");
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("zombie")) {
            if (args.length == 1) {
                tab.add("setspawner");
                tab.add("killall");
                tab.add("spawnlimit");
                tab.add("spawned");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("spawnlimit")) {
                    tab.add("set");
                }
            }
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("set")) {
                    tab.add("100");
                    tab.add("200");
                    tab.add("300");
                    tab.add("400");
                    tab.add("500");
                    tab.add("600");
                    tab.add("700");
                    tab.add("800");
                    tab.add("900");
                    tab.add("1000");
                }
            }

        }
        return tab;

    }

}
