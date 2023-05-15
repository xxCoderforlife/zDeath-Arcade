package dev.nullpointercoding.zdeatharcade.Utils.Snacks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.util.formatting.text.serializer.plain.PlainComponentSerializer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Snacks {

    private Double cookieWorth = 5.00;


    public Snacks() {
        
    }

    public void createSnacks(){
        createSnack(new ItemStack(Material.COOKIE), "&d&oCookies",  cookieWorth, cookieAbilities());
        getAllSnacks().add()
    }

    private ItemStack createSnack(ItemStack itemstack,String dName,Double worth,ArrayList<String> abilities){
        ItemStack item = itemstack;
        ItemMeta meta = item.getItemMeta();
        TextComponent name = LegacyComponentSerializer.legacyAmpersand().deserialize(dName);
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;

        

    }

    public ArrayList<Snacks> getAllSnacks(){
        return null;
    }

    private ArrayList<String> cookieAbilities(){
        ArrayList<String> abilities = new ArrayList<>();
        abilities.add("ABSORPTION");
        return abilities;
    }
}
