package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.nullpointercoding.zdeatharcade.Main;
import net.milkbowl.vault.economy.Economy;

public class EcoCommands implements CommandExecutor {

    private Main plugin = Main.getInstance();
    Economy econ = new VaultHook();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("economy")) {
            if (args.length == 0) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                sender.sendMessage("§cUsage: /economy <add|remove|set> <player> <amount>");
                return true;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);
                    econ.depositPlayer(target, amount);
                    sender.sendMessage("§aAdded " + amount + " to " + target.getName() + "'s balance!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);
                    econ.withdrawPlayer(target, amount);
                    sender.sendMessage("§aRemoved " + amount + " from " + target.getName() + "'s balance!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")) {
                    Player target = plugin.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage("§cPlayer not found!");
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);
                    econ.withdrawPlayer(target, econ.getBalance(target));
                    econ.depositPlayer(target, amount);
                    sender.sendMessage("§aSet " + target.getName() + "'s balance to " + amount + "!");
                    return true;
                }
            }
        }
        return true;
    }

}
