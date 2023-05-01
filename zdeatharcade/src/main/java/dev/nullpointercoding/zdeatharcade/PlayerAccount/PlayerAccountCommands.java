package dev.nullpointercoding.zdeatharcade.PlayerAccount;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class PlayerAccountCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("account")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission("zdeatharcade.account"))) {
                    p.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }

                PlayerAccountGUI gui = new PlayerAccountGUI(p);
                gui.openGUI(p);

            }
        }
        return true;
    }

}
