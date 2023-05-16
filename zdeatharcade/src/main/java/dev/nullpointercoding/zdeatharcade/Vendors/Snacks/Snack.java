package dev.nullpointercoding.zdeatharcade.Vendors.Snacks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Snack {

    private final String name;
    private final Material material;
    private final Component displayName;
    private final PotionEffectType effect;
    private final ItemStack item;
    private final Double healAmount;
    private final Double worth;
    private final Integer duration;
    private final Integer ampilfer;
    private static final List<Snack> snacks = new ArrayList<>();

    public Snack(String name, Material material, Component dName, PotionEffectType effect, Integer dur, Integer amp,
            Double healAmount, Double worth) {
        this.name = name;
        this.material = material;
        this.displayName = dName;
        this.effect = effect;
        this.healAmount = healAmount;
        this.worth = worth;
        this.duration = dur;
        this.ampilfer = amp;
        this.item = createItemStack();
        snacks.add(this);
    }

    public String getName() {
        return name;
    }

    private ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Effect: ", NamedTextColor.WHITE, TextDecoration.ITALIC)
                .append(Component.text(effect.getName(), NamedTextColor.DARK_PURPLE)));
        lore.add(Component.text("Price: ", NamedTextColor.GREEN, TextDecoration.ITALIC)
                .append(Component.text(worth.toString(), NamedTextColor.WHITE)));
        lore.add(Component.text("Heal Amount: ", NamedTextColor.WHITE, TextDecoration.ITALIC)
                .append(Component.text(healAmount.toString(), NamedTextColor.RED)));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSnack() {
        return item;
    }

    public Double getWorth() {
        return worth;
    }

    public Double getHealAmount() {
        return healAmount;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getAmplifier() {
        return ampilfer;
    }

    public static Boolean isSnack(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            Component displayName = meta.displayName();

            // Check if the display name or any other unique identifier
            // matches the snack identifier you have set
            for (Snack snack : snacks) {
                if (displayName != null && snack.getSnack().getItemMeta().displayName().equals(displayName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public PotionEffectType getEffect() {
        return effect;
    }

    public static Snack ItemStackToSnack(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            Component displayName = meta.displayName();
            // Check if the display name or any other unique identifier
            // matches the snack identifier you have set
            for (Snack snack : snacks) {
                if (displayName != null && snack.getSnack().getItemMeta().displayName().equals(displayName)) {
                    return snack;
                }
            }
        }
        return null;

    }

    public ItemStack getSnackItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Right-Click to eat", NamedTextColor.GRAY, TextDecoration.ITALIC));
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
