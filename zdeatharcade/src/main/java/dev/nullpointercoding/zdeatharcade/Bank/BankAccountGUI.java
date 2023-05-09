package dev.nullpointercoding.zdeatharcade.Bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.nullpointercoding.zdeatharcade.Main;
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import dev.nullpointercoding.zdeatharcade.Utils.InventoryUtils.BlankSpaceFiller;
import dev.nullpointercoding.zdeatharcade.Utils.VaultHookFolder.VaultHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;
import net.milkbowl.vault.economy.Economy;

public class BankAccountGUI implements Listener {
    private Main plugin = Main.getInstance();
    private Double amountToMoveToBank = 0.0;
    private Player target;
    private Boolean isDepositingv;
    private AccountType accountT;
    private Economy econ = new VaultHook();
    private static HashMap<Player, Double> bountyAmount = new HashMap<Player, Double>();
    private static HashMap<Player, HashMap<Player, Double>> playerBounty = new HashMap<Player, HashMap<Player, Double>>();
    private Component title = Component.text("§e§o    TRANSACTION MENU");
    private Component error = Component.text("§4§lBROKE ALERT: You ain't got no Bread")
            .hoverEvent(HoverEvent.showText(Component.text("§7§oYou can't move $0")));

    private Inventory inv;

    public enum AccountType {
        BANK(),
        PLAYER(),
        BOUNTY();
    }

    public BankAccountGUI(Player p) {
        boolean isEventRegistered = HandlerList.getRegisteredListeners(plugin).stream()
                .anyMatch(handler -> handler.getListener() instanceof BankAccountGUI);
        if (!isEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        this.target = p;
        inv = plugin.getServer().createInventory(p, 54, title);
    }

    public void openGUI(Player p) {
        addItems();
        BlankSpaceFiller.fillinBlankInv(inv, List.of(0));
        p.openInventory(inv);
    }

    private void addItems() {
        inv.setItem(0, addOne());
        inv.setItem(9, addTen());
        inv.setItem(18, add50());
        inv.setItem(27, add100());
        inv.setItem(36, add1000());
        inv.setItem(8, remove1());
        inv.setItem(17, remove10());
        inv.setItem(26, remove50());
        inv.setItem(35, remove100());
        inv.setItem(44, remove1000());
        if (accountT.equals(AccountType.BANK)) {
            if (isDepositingv) {
                inv.setItem(49, confirmDeposit());
                inv.setItem(4, depoistAll());
                inv.setItem(45, exit());
            } else {
                inv.setItem(49, confirmWithdraw());
                inv.setItem(4, withdrawlAll());
                inv.setItem(45, exit());

            }
        }
        if (accountT.equals(AccountType.PLAYER)) {
            if (isDepositingv) {
                inv.setItem(49, payPlayer());
                inv.setItem(45, exit());

            } else {
                inv.setItem(49, reqPlayer());
                inv.setItem(45, exit());

            }
        }
        if (accountT.equals(AccountType.BOUNTY)) {
            inv.setItem(49, confirmBouty());
            inv.setItem(45, exit());

        }

    }

    @EventHandler
    public void onBankMenuClose(InventoryCloseEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        amountToMoveToBank = 0.0;
        Player playerWhoClosed = (Player) e.getPlayer();
        if (e.getReason().equals(Reason.PLAYER)) {
            playerWhoClosed.sendMessage(Component.text("§c§lERROR: §7You have closed the transcation menu!"));
        }

    }

    @EventHandler
    public void onBankGUIClick(InventoryClickEvent e) {
        if (!(e.getView().title().equals(title))) {
            return;
        }
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) {
            return;
        }
        Player whoClicked = (Player) e.getWhoClicked();
        if (clicked.getItemMeta().displayName().equals(addOne().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank + 1;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(addTen().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank + 10;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(add50().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank + 50;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(add100().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank + 100;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(add1000().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank + 1000;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(remove1().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank - 1;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(remove10().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank - 10;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(remove50().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank - 50;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(remove100().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank - 100;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(remove1000().getItemMeta().displayName())) {
            amountToMoveToBank = amountToMoveToBank - 1000;
            whoClicked.sendMessage(Component.text("§e§oAmount to Transfer: §a§l$" + amountToMoveToBank));
            whoClicked.playSound(whoClicked, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(confirmDeposit().getItemMeta().displayName())) {
            if (amountToMoveToBank > 0) {
                if (econ.getBalance(whoClicked) >= amountToMoveToBank) {
                    econ.withdrawPlayer(whoClicked, amountToMoveToBank);
                    econ.bankDeposit(whoClicked.getUniqueId().toString(), amountToMoveToBank);
                    whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully moved §a§l$"
                            + amountToMoveToBank + " §7to your bank account!"));
                    whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    whoClicked.closeInventory(Reason.PLUGIN);
                } else {
                    sendNotEnounghMoney(whoClicked);
                }
            } else {
                inVaildTransaction(whoClicked);
            }
        }
        if (clicked.getItemMeta().displayName().equals(confirmWithdraw().getItemMeta().displayName())) {
            if (amountToMoveToBank > 0) {
                if (econ.bankBalance(whoClicked.getUniqueId().toString()).balance >= amountToMoveToBank) {
                    econ.depositPlayer(whoClicked, amountToMoveToBank);
                    econ.bankWithdraw(whoClicked.getUniqueId().toString(), amountToMoveToBank);
                    whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully moved §a§l$"
                            + amountToMoveToBank + " §7to your player account!"));
                    whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                    whoClicked.closeInventory(Reason.PLUGIN);
                } else {
                    sendNotEnounghMoney(whoClicked);
                }
            } else {
                inVaildTransaction(whoClicked);
            }
        }
        if (clicked.getItemMeta().displayName().equals(depoistAll().getItemMeta().displayName())) {
            if (econ.getBalance(whoClicked) > 0) {
                econ.bankDeposit(whoClicked.getUniqueId().toString(), econ.getBalance(whoClicked));
                econ.withdrawPlayer(whoClicked, econ.getBalance(whoClicked));
                whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully moved §a§l$"
                        + econ.bankBalance(whoClicked.getUniqueId().toString()).balance + " §7to your bank account!"));
                whoClicked.closeInventory(Reason.PLUGIN);
                whoClicked.playSound(whoClicked, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            } else {
                sendNotEnounghMoney(whoClicked);
            }
        }
        if (clicked.getItemMeta().displayName().equals(withdrawlAll().getItemMeta().displayName())) {
            if (econ.bankBalance(whoClicked.getUniqueId().toString()).balance > 0) {
                econ.depositPlayer(whoClicked, econ.bankBalance(whoClicked.getUniqueId().toString()).balance);
                econ.bankWithdraw(whoClicked.getUniqueId().toString(),
                        econ.bankBalance(whoClicked.getUniqueId().toString()).balance);
                whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully moved §a§l$"
                        + econ.getBalance(whoClicked) + " §7to your player account!"));
                whoClicked.closeInventory(Reason.PLUGIN);
                whoClicked.playSound(whoClicked, Sound.ENTITY_CHICKEN_DEATH, 1.0f, 1.0f);
            } else {
                sendNotEnounghMoney(whoClicked);
            }
        }
        if (clicked.getItemMeta().displayName().equals(payPlayer().getItemMeta().displayName())) {
            if (econ.getBalance(whoClicked) > 0) {
                econ.withdrawPlayer(whoClicked, amountToMoveToBank);
                econ.depositPlayer(target, amountToMoveToBank);
                whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully paid §a§l$"
                        + amountToMoveToBank + " §7to " + target.getName() + "!"));
                whoClicked.closeInventory(Reason.PLUGIN);
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                target.sendMessage(Component.text(whoClicked.getName() + " has paid you §a§l$" + amountToMoveToBank
                        + " §7to your player account!"));
                target.playSound(target, Sound.BLOCK_BELL_RESONATE, 1.0f, 1.0f);

            }
        }
        if (clicked.getItemMeta().displayName().equals(reqPlayer().getItemMeta().displayName())) {
            target.sendMessage(
                    Component.text(whoClicked.getName() + " wants you to send them " + amountToMoveToBank + "!"));
            target.playSound(target, Sound.BLOCK_BELL_USE, 1.0f, 1.0f);
            whoClicked.sendMessage(Component.text("§a§lSUCCESS: §7You have successfully requested §a§l$"
                    + amountToMoveToBank + " §7from " + target.getName() + "!"));
            whoClicked.closeInventory(Reason.PLUGIN);
                whoClicked.playSound(whoClicked, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
        if (clicked.getItemMeta().displayName().equals(confirmBouty().getItemMeta().displayName())) {
            if (amountToMoveToBank > 0) {
                PlayerConfigManager pcm = new PlayerConfigManager(target.getUniqueId().toString());
                if (pcm.hasBounty()) {
                    whoClicked.sendMessage(Component.text("§c§lERROR: " + target.getName() + " already has a bounty!"));
                    whoClicked.playSound(whoClicked, Sound.ENTITY_SKELETON_HURT, 1.0f, 1.0f);
                    whoClicked.closeInventory(Reason.PLUGIN);
                    return;
                }
                if (econ.getBalance(whoClicked) >= amountToMoveToBank) {
                    whoClicked.sendMessage("§a§lSUCCESS: §7You have successfully put a Bounty on " + target.getName());
                    Bukkit.broadcast(Component.text(whoClicked.getName() + " set a bounty of $" + amountToMoveToBank
                            + " on " + target.getName()));
                    bountyAmount.put(whoClicked, amountToMoveToBank);
                    playerBounty.put(target, bountyAmount);
                    pcm.setHasBounty(true, amountToMoveToBank);
                    whoClicked.closeInventory(Reason.PLUGIN);
                    whoClicked.playSound(whoClicked, Sound.ENTITY_POLAR_BEAR_WARNING, 1.0f, 1.0f);
                } else {
                    sendNotEnounghMoney(whoClicked);
                }
            } else {
                inVaildTransaction(whoClicked);
            }
        }
        if (clicked.getItemMeta().displayName().equals(exit().getItemMeta().displayName())) {
            whoClicked.closeInventory(Reason.PLUGIN);
            whoClicked.sendTitlePart(TitlePart.TITLE, Component.text("§c§oClosing Money Managment Terminal..."));
            whoClicked.sendTitlePart(TitlePart.SUBTITLE,
                    Component.text("§4§oHave a nice Day User: " + whoClicked.getUniqueId().toString()));
            whoClicked.playSound(whoClicked, Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);

        }
    }

    private ItemStack addOne() {
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+1"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }

    private ItemStack addTen() {
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+10"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }

    private ItemStack add50() {
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+50"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }

    private ItemStack add100() {
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+100"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }

    private ItemStack add1000() {
        ItemStack coal = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = coal.getItemMeta();
        meta.displayName(Component.text("§a§l+1000"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        coal.setItemMeta(meta);
        return coal;
    }

    private ItemStack remove1() {
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-1"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }

    private ItemStack remove10() {
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-10"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }

    private ItemStack remove50() {
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-50"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }

    private ItemStack remove100() {
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-100"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }

    private ItemStack remove1000() {
        ItemStack red = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = red.getItemMeta();
        meta.displayName(Component.text("§c§l-1000"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        red.setItemMeta(meta);
        return red;
    }

    private ItemStack confirmDeposit() {
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirm.getItemMeta();
        meta.displayName(Component.text("§a§lCONFIRM DEPOSIT"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Click to confirm deposit",NamedTextColor.GRAY,TextDecoration.ITALIC));
        meta.lore(lore);
        confirm.setItemMeta(meta);
        return confirm;
    }

    private ItemStack confirmWithdraw() {
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirm.getItemMeta();
        meta.displayName(Component.text("§a§lCONFIRM WITHDRAWAL"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Click to confirm withdrawal",NamedTextColor.GRAY,TextDecoration.ITALIC));
        meta.lore(lore);
        confirm.setItemMeta(meta);
        return confirm;
    }

    private ItemStack withdrawlAll() {
        ItemStack all = new ItemStack(Material.MAGENTA_WOOL);
        ItemMeta meta = all.getItemMeta();
        meta.displayName(Component.text("§d§lWITHDRAW ALL"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Click to withdraw all your money from the bank",NamedTextColor.GRAY,TextDecoration.ITALIC));
        meta.lore(lore);
        all.setItemMeta(meta);
        return all;
    }

    private ItemStack depoistAll() {
        ItemStack all = new ItemStack(Material.MAGENTA_WOOL);
        ItemMeta meta = all.getItemMeta();
        meta.displayName(Component.text("§d§lDEPOSIT ALL"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Click to deposit all your money into the bank",NamedTextColor.GRAY,TextDecoration.ITALIC));
        meta.lore(lore);
        all.setItemMeta(meta);
        return all;
    }

    private ItemStack payPlayer() {
        ItemStack conPay = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = conPay.getItemMeta();
        meta.displayName(Component.text("§6§lPAY PLAYER"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        conPay.setItemMeta(meta);
        return conPay;
    }

    private ItemStack reqPlayer() {
        ItemStack conPay = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = conPay.getItemMeta();
        meta.displayName(Component.text("§6§lREQUEST PAYMENT"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        conPay.setItemMeta(meta);
        return conPay;
    }

    private ItemStack confirmBouty() {
        ItemStack confirm = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirm.getItemMeta();
        meta.displayName(Component.text("§a§lCONFIRM BOUNTY"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        confirm.setItemMeta(meta);
        return confirm;
    }

    private ItemStack exit() {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.displayName(Component.text("§c§lExit"));
        barrier.setItemMeta(meta);
        return barrier;
    }

    public void setIsDespoisting(Boolean isDespoisting, AccountType accountType) {
        isDepositingv = isDespoisting;
        accountT = accountType;
    }

    private void sendNotEnounghMoney(Player whoToSendTo) {
        whoToSendTo.closeInventory(Reason.PLUGIN);
        whoToSendTo.sendMessage(error);
        whoToSendTo.playSound(whoToSendTo, Sound.ENTITY_PLAYER_DEATH, 1.0f, 1.0f);
    }

    private void inVaildTransaction(Player whoToSendTo) {
        whoToSendTo.sendMessage(
                Component.text("§c§lERROR: §7You cannot move a negative or 0 amount of money!"));
        whoToSendTo.playSound(whoToSendTo, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }

    public static HashMap<Player, HashMap<Player, Double>> getBountyList() {
        return playerBounty;
    }

    public static HashMap<Player, Double> getBountyAmount() {
        return bountyAmount;
    }

}
