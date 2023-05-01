package dev.nullpointercoding.zdeatharcade.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TabCommands implements TabCompleter{

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender arg0, @NotNull Command arg1,
            @NotNull String arg2, @NotNull String[] args) {
            
         List<String> tab = new ArrayList<String>();
         if(args.length == 0){

         }
         if(args.length == 1){
             tab.add("zombie");
             tab.add("range");
         }
         if(args.length == 2){
            if(args[0].equalsIgnoreCase("zombie")){
                tab.add("setspawner");
            }
            if(args[0].equalsIgnoreCase("range")){
                tab.add("setspawn");
                tab.add("resetspawn");
                tab.add("gunsmith");
            }

         }
         if(args.length == 3){
            if(args[0].equalsIgnoreCase("range")){
                if(args[1].equalsIgnoreCase("gunsmith")){
                    tab.add("Enter the NPC ID Number");
                }
            }
         }
        return tab;
    }
    
}
