package dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EcoTabCommands implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
            @NotNull String arg2, @NotNull String[] args) {

        List<String> tab = new ArrayList<String>();
        if (cmd.getName().equalsIgnoreCase("economy") || cmd.getName().equalsIgnoreCase("token")) {
            if (args.length == 0) {

            }
            if (args.length == 1) {
                tab.add("add");
                tab.add("remove");
                tab.add("set");
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")
                        || args[0].equalsIgnoreCase("set")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        tab.add(p.getName());
                    }
                }
                if (args[0].equalsIgnoreCase("bank")) {
                    tab.add("create");
                    tab.add("delete");
                    tab.add("info");
                }

            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("bank")) {
                    if (args[1].equalsIgnoreCase("delete")) {
                        tab.add("Still need to add the bank file system.");
                    }
                }
            }
        }
        return tab;
    }
}
