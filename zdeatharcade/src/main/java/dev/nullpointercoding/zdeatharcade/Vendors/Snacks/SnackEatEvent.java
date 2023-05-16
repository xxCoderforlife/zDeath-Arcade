package dev.nullpointercoding.zdeatharcade.Vendors.Snacks;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SnackEatEvent implements Listener {

    /**
     * @param e
     */
    @EventHandler
    public void onSnackEat(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action pa = e.getAction();

        if (!pa.equals(Action.RIGHT_CLICK_AIR) && !pa.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if (Snack.isSnack(mainHand)) {
            e.setCancelled(true);
            if (p.getHealth() == p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                p.sendMessage("You are at Full Health");
                return;
            }
            Snack s = Snack.ItemStackToSnack(mainHand);
            Integer amount = mainHand.getAmount();
            if (amount > 1) {
                mainHand.setAmount(amount - 1);
            } else {
                p.getInventory().setItemInMainHand(null);
            }
            p.addPotionEffect(new PotionEffect(s.getEffect(), s.getDuration(), s.getAmplifier()));
            p.setHealth(p.getHealth() + s.getHealAmount());
        }
    }
}
