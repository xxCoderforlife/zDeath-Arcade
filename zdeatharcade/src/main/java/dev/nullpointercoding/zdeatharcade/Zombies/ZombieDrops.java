package dev.nullpointercoding.zdeatharcade.Zombies;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ZombieDrops {
    

    //Level 1 Drops
    public ItemStack hayDrop(){
        ItemStack hay = new ItemStack(Material.HAY_BLOCK,1);
        ItemMeta hayMeta = hay.getItemMeta();
        hayMeta.displayName(Component.text("Hay Block",NamedTextColor.AQUA,TextDecoration.ITALIC));
        hayMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        hayMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the shop for money!",NamedTextColor.GRAY,TextDecoration.ITALIC));
        hayMeta.lore(lore);
        hay.setItemMeta(hayMeta);
        return hay;
    }
    public ItemStack beefDrop(){
        ItemStack beef = new ItemStack(Material.BEEF,1);
        ItemMeta beefMeta = beef.getItemMeta();
        beefMeta.displayName(Component.text("Beef",NamedTextColor.AQUA,TextDecoration.ITALIC));
        beefMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        beefMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the shop for money!",NamedTextColor.GRAY,TextDecoration.ITALIC));
        beefMeta.lore(lore);
        beef.setItemMeta(beefMeta);
        return beef;
    }
    public ItemStack grassBlockDrop(){
        ItemStack grassBlock = new ItemStack(Material.GRASS_BLOCK,1);
        ItemMeta grassBlockMeta = grassBlock.getItemMeta();
        grassBlockMeta.displayName(Component.text("Grass Block",NamedTextColor.AQUA,TextDecoration.ITALIC));
        grassBlockMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        grassBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the shop for money!",NamedTextColor.GRAY,TextDecoration.ITALIC));
        grassBlockMeta.lore(lore);
        grassBlock.setItemMeta(grassBlockMeta);
        return grassBlock;
    }
    public ItemStack coalBlockDrop(){
        ItemStack coalBlock = new ItemStack(Material.COAL_BLOCK,1);
        ItemMeta coalBlockMeta = coalBlock.getItemMeta();
        coalBlockMeta.displayName(Component.text("Coal Block",NamedTextColor.AQUA,TextDecoration.ITALIC));
        coalBlockMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        coalBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<Component>();
        lore.add(Component.text("Sell this at the shop for money!",NamedTextColor.GRAY,TextDecoration.ITALIC));
        coalBlockMeta.lore(lore);
        coalBlock.setItemMeta(coalBlockMeta);
        return coalBlock;
    }
    public ItemStack[] getLevel1Drops(){
        ItemStack[] level1Drops = {hayDrop(),beefDrop(),grassBlockDrop(),coalBlockDrop()};
        return level1Drops;
    }

    //Level 2 Drops
}
