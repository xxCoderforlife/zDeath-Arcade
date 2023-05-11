package dev.nullpointercoding.zdeatharcade.ShootingRange;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;


import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.NPCConfigManager;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class RangeGunSmith implements Listener {

    private Inventory gunsmithInv;
    private Component shopTitle = Component.text("Gunsmith Freebies",NamedTextColor.RED,TextDecoration.BOLD);
    private static Villager gunsmithNPC;
    private static final Component name = Component.text("Gunsmith",NamedTextColor.RED,TextDecoration.ITALIC);
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



    private Inventory gunsmithInventory() {

        gunsmithInv.setItem(0, fn_GUN.getItemStack());
        gunsmithInv.setItem(1, aa12_GUN.getItemStack());
        gunsmithInv.setItem(2, mp40_GUN.getItemStack());
        gunsmithInv.setItem(3, pkp_GUN.getItemStack());
        gunsmithInv.setItem(4, vz_GUN.getItemStack());

        return gunsmithInv;
    }

    @EventHandler
    public void onGunSmithClick(PlayerInteractEntityEvent e){
        if(e.getRightClicked().getType() == EntityType.VILLAGER){
            e.setCancelled(true);
            LivingEntity entity = (LivingEntity) e.getRightClicked();
            if(entity.customName() == null){return;}
            if(entity.customName().equals(name)){
                e.getPlayer().openInventory(gunsmithInv);
            }
        }
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
    public static Villager spawnGunsmithNPC(Player p,Integer npcID){
        for(File f : Main.getInstance().getNPCDataFolder().listFiles()){
            if(f.getName().equalsIgnoreCase("gunsmith.yml")){
                p.sendMessage(Component.text("Gunsmith already spawned!"));
                return gunsmithNPC;
            }
        }
        Location spawnLoc = p.getLocation().add(1, 0.0, 0);
        gunsmithNPC = (Villager) p.getWorld().spawnEntity(spawnLoc, EntityType.VILLAGER);
        gunsmithNPC.setSilent(true);
        gunsmithNPC.setCollidable(false);
        gunsmithNPC.setVillagerType(Villager.Type.TAIGA);
        gunsmithNPC.setProfession(Profession.ARMORER);
        gunsmithNPC.setPersistent(true);
        gunsmithNPC.setAI(false);
        gunsmithNPC.customName(name);
        gunsmithNPC.setCustomNameVisible(true);
        new NPCConfigManager("gunsmith",p.getWorld(),npcID,spawnLoc.getX(),spawnLoc.getY(),spawnLoc.getZ());
        return gunsmithNPC;
    }

    public static Villager getGunSmithNPC(){
        return gunsmithNPC;
    }


}
