package dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool;

import java.util.ArrayList;
//import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import dev.nullpointercoding.zdeatharcade.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;

public class AFKPool implements Listener {

    // TODO: Find a faster way to check and get the PlayerRewards, I hate this
    // method.
    private static ArrayList<Player> playersInPool = new ArrayList<Player>();
    private ArrayList<Player> leavingPool = new ArrayList<Player>();
    //private HashMap<Player, Integer> playerAFKTime = new HashMap<Player, Integer>();
    private AFKPlayerStorage afkPlayerStorage = new AFKPlayerStorage();
    private BukkitTask akfTask;
    private Main plugin = Main.getInstance();


    public AFKPool() {

    }

    @EventHandler
    public void onPlayerEnterPool(PlayerMoveEvent e) {
        Player player = (Player) e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ()) {
            /*
             * Check if the player is in water
             * If the player is in water, check if the player is in the AFK Pool
             */
            Location loc = player.getLocation().subtract(e.getTo(), 0, 1, 0);
            if (loc.getBlock().getType() == Material.WATER && isPlayerinAFKPool(player)) {
                addPlayerToPool(player);
            }
        }
    }

    private void addPlayerToPool(Player player){
        if(!(playersInPool.contains(player))){
            AFKPlayer afkPlayer = new AFKPlayer(player,0.0,0.0);
            afkPlayerStorage.addAFKPlayer(afkPlayer);
            playersInPool.add(afkPlayer.getPlayer());
            checkForPlayersInPool(afkPlayer);
            player.sendMessage("You are now AFK");
        }
    }

    private boolean isPlayerinAFKPool(Player p) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for (ProtectedRegion pr : set)
            if (pr.getId().equalsIgnoreCase("afkpool"))
                return true;
        return false;
    }

    @EventHandler
    public void onRightClickEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }

        if (e.getClickedBlock().getType().toString().contains("SIGN")) {
            Player whoClicked = (Player) e.getPlayer();
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.line(0).equals(Component.text("AFK POOL", NamedTextColor.AQUA, TextDecoration.BOLD))) {
                if (!(leavingPool.contains(e.getPlayer()))) {
                    AFKPlayer afkPlayer = afkPlayerStorage.getAFKPlayer(whoClicked.getName());
                    leavingPool.add(e.getPlayer());
                    whoClicked.sendActionBar(Component.text("Getting ready to leave..."));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            afkPlayerStorage.removeAFKPlayer(afkPlayer);
                            playersInPool.remove(e.getPlayer());
                            leavingPool.remove(e.getPlayer());
                            whoClicked.teleportAsync(whoClicked.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                            whoClicked.sendTitlePart(TitlePart.TITLE, Component.text("You are no longer AFK",
                                    NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
                            whoClicked.sendMessage("You earned " + afkPlayer.getCash() + " cash and " + afkPlayer.getTokens() + " tokens for being AFK!");
                        }

                    }.runTaskLater(plugin, 20 * 5);
                } else {
                    whoClicked.sendMessage("You are already leaving the pool!");
                }
            }
        }
    }

    @EventHandler
    public void onLeaveSignCreate(SignChangeEvent e) {
        Player whoCreated = (Player) e.getPlayer();

        if (e.line(0).equals(Component.text("[afk]"))) {
            e.line(0, Component.text("AFK POOL", NamedTextColor.AQUA, TextDecoration.BOLD));
            e.line(2, Component.text("EXIT", NamedTextColor.DARK_RED, TextDecoration.ITALIC));
            whoCreated.sendMessage(
                    Component.text("AFK Pool Exit Sign Created!", NamedTextColor.GREEN, TextDecoration.BOLD));
        }
    }

    private void checkForPlayersInPool(AFKPlayer afkplayer) {
        if (akfTask == null || akfTask.isCancelled()) {

            akfTask = new BukkitRunnable() {

                @Override
                public void run() {

                    if (playersInPool.isEmpty()) {
                        this.cancel();
                        Bukkit.getConsoleSender().sendMessage("No players in pool, stopping task.");
                    }
                    for (Player p : playersInPool) {
                        afkplayer.setCash(afkplayer.getCash() + 0.25);
                        afkplayer.setTokens(afkplayer.getTokens() + 0.02);
                        p.sendMessage(Component.text("+ $0.25", NamedTextColor.GREEN,
                                TextDecoration.ITALIC)
                                .hoverEvent(Component.text("Cash can be used to buy most items.", NamedTextColor.GREEN,
                                        TextDecoration.ITALIC)));
                        p.sendMessage(Component.text("+ 0.02 Tokens", NamedTextColor.GOLD,
                                TextDecoration.ITALIC).hoverEvent(
                                        Component.text("Tokens can be used to buy items at the Black Martket Vendor.",
                                                NamedTextColor.GREEN, TextDecoration.ITALIC)));

                    }
                }

            }.runTaskTimer(plugin, (long) 20 * 60, (long) 20 * 60);
        }
    }

    public static ArrayList<Player> getPlayersInPool() {
        return playersInPool;
    }


}
