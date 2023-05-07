package dev.nullpointercoding.zdeatharcade.Zombies;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.ZombieSpawnLocationManager;
import net.kyori.adventure.text.Component;

public class ZombieCommands implements CommandExecutor {

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
                p.sendMessage(Component.text("§eUsage: §6/zdeatharcade zombie <setspawner/killall>"));
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
                    if(args[0].equalsIgnoreCase("killall")){
                        if (!(p.hasPermission("zdeatharcade.admin"))) {
                            p.sendMessage("§cYou do not have permission to use this command.");
                            return true;
                        }
                        for (Entity z : p.getWorld().getEntitiesByClass(Zombie.class)) {
                            z.remove();
                        }
                        p.sendMessage("§aSuccessfully removed all zombies.");
                    }
                }
            }
        }
        return true;
    }

}
