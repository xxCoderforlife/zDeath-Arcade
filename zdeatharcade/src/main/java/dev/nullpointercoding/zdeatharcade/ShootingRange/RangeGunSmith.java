package dev.nullpointercoding.zdeatharcade.ShootingRange;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import dev.nullpointercoding.zdeatharcade.Utils.NPCConfigManager;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;

public class RangeGunSmith implements Listener {

    private NPC gunsmith;
    private Inventory gunsmithInv;
    private Component shopTitle = Component.text("Gunsmith Freebies");
    private NPCConfigManager gunsmithConfig = new NPCConfigManager("gunsmith.yml");
    private Gun fn_GUN;
    private Gun aa12_GUN;
    private Gun mp40_GUN;
    private Gun pkp_GUN;
    private Gun vz_GUN;

    public RangeGunSmith() {
        fn_GUN = QualityArmory.getGunByName("fnfiveseven");
        aa12_GUN = QualityArmory.getGunByName("aa12");
        mp40_GUN = QualityArmory.getGunByName("mp40");
        pkp_GUN = QualityArmory.getGunByName("pkp");
        vz_GUN = QualityArmory.getGunByName("vz58");
        gunsmithInv = Bukkit.createInventory(null, 9, shopTitle);
        gunsmithInventory();
    }

    public NPC getGunSmith() {
        return gunsmith;
    }

    @EventHandler
    public void onGunSmithInteract(NPCRightClickEvent e) {
        Player p = (Player) e.getClicker();
        if (gunsmithConfig.getConfig().getInt("NPC.ID") == e.getNPC().getId()) {
            p.openInventory(gunsmithInventory());
            p.getLocation().getWorld().playEffect(p.getEyeLocation().add(0, 1, 0), Effect.BONE_MEAL_USE, 20, 20);
        } else {
            p.sendMessage("Not working");
        }

    }

    private Inventory gunsmithInventory() {

        gunsmithInv.setItem(0, fn_GUN.getItemStack());
        gunsmithInv.setItem(1, aa12_GUN.getItemStack());
        gunsmithInv.setItem(2, mp40_GUN.getItemStack());
        gunsmithInv.setItem(3, pkp_GUN.getItemStack());
        gunsmithInv.setItem(4, vz_GUN.getItemStack());

        return gunsmithInv;
    }

    @EventHandler
    public void playerOpenInventory(InventoryOpenEvent ev) {
        Player p = (Player) ev.getPlayer();
        InventoryView view = ev.getView();
        if (view.title().equals(shopTitle)) {
            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, 2.0f, 12.0f);

        }
    }

    @EventHandler
    public void gunSmithInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().title().equals(shopTitle)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (QualityArmory.isGun(e.getCurrentItem())) {
                Gun gun = QualityArmory.getGun(e.getCurrentItem());
                for (ItemStack g : p.getInventory().getContents()) {
                    if (QualityArmory.isGun(g)) {
                        if (gun == QualityArmory.getGun(g)) {
                            p.sendMessage("You already have this gun!");
                            return;
                        }
                    }
                }
                p.getInventory().addItem(gun.getItemStack());
                p.playSound(p, Sound.ITEM_ARMOR_EQUIP_TURTLE, (float) 1, (float) 0.80);
                p.closeInventory();
            }
        }

    }

}
