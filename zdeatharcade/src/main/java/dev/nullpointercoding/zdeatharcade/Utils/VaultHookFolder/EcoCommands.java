package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EcoCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg2,
            @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("economy")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission("zdeatharcade.economy"))) {
                    p.sendMessage("§cYou do not have permission to use this command!");
                    return true;

                }
                if (args.length == 0) {
                    p.sendMessage("§cPlease use /economy <give/take/set/> <player> <amount>");
                    p.sendMessage("§cPlease use /economy <bank> <create/delete> <name>");
                    return true;
                }
                if (args.length == 1) {
                    if(args[0].equalsIgnoreCase("bank")){
                        p.sendMessage("§cPlease use /economy <bank> <create/delete> <name>");
                        return true;
                    }
                }
                if(args.length == 2){
                    if(args[0].equalsIgnoreCase("bank")){
                        if(args[1].equalsIgnoreCase("create")){
                            p.sendMessage("§cPlease use /economy <bank> <create/delete> <name>");
                            return true;
                        }
                        if(args[1].equalsIgnoreCase("delete")){
                            p.sendMessage("§cPlease use /economy <bank> <create/delete> <name>");
                            return true;
                        }
                    }
                }
                if(args.length == 3){
                    if(args[0].equalsIgnoreCase("bank")){
                        if(args[1].equalsIgnoreCase("create")){
                            if(args[2] instanceof String){
                                p.sendMessage("Created Bank with the name: " + args[2]);
                            }else{
                                p.sendMessage("Must only use letters and '_' inside the name of the bank!");
                            }
                        }
                    }
                }
            }

        }
        return true;
    }

}
