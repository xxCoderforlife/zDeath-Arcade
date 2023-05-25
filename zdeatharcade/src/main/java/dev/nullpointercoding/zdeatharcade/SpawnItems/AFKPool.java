package dev.nullpointercoding.zdeatharcade.SpawnItems;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

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
import dev.nullpointercoding.zdeatharcade.Utils.PlayerConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

public class AFKPool implements Listener {

    private ArrayList<Player> playersInPool = new ArrayList<Player>();
    private ArrayList<Player> leavingPool = new ArrayList<Player>();
    private HashMap<Player, Integer> playerAFKTime = new HashMap<Player, Integer>();
    private Double cashEarned = 0.0;
    private Double tokensEarned = 0.0;
    private BukkitTask akfTask;
    private Main plugin = Main.getInstance();
    private final Component afkPOOL = Component.text("AFK Pool", NamedTextColor.AQUA, TextDecoration.BOLD)
            .hoverEvent(Component.text("Earn passive income and other rewards while AFK!", NamedTextColor.GREEN,
                    TextDecoration.ITALIC));

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
            if (loc.getBlock().getType() == Material.WATER) {
                if (isPlayerinAFKPool(player)) {
                    if (!(playersInPool.contains(player))) {
                        playersInPool.add(player);
                        playerAFKTime.put(player, LocalTime.now().getSecond());
                        player.sendTitlePart(TitlePart.TITLE,
                                Component.text("You are now AFK", NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
                        player.sendTitlePart(TitlePart.SUBTITLE,
                                Component.text("Earn passive income and other rewards while AFK every minute!",
                                        NamedTextColor.GREEN, TextDecoration.ITALIC));
                        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(1),
                                Duration.ofSeconds(5), Duration.ofSeconds(1)));
                        Bukkit.broadcast(player.displayName().append(Component.text(" entered the ").append(afkPOOL)));
                        checkForPlayersInPool();
                    }
                }
            }
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
                    leavingPool.add(e.getPlayer());
                    whoClicked.sendActionBar(Component.text("Getting ready to leave..."));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            playersInPool.remove(e.getPlayer());
                            leavingPool.remove(e.getPlayer());
                            whoClicked.teleportAsync(whoClicked.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                            whoClicked.sendTitlePart(TitlePart.TITLE, Component.text("You are no longer AFK",
                                    NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC));
                            if (cashEarned + tokensEarned == 0) {
                                whoClicked.sendMessage(Component
                                        .text("You didn't earn anything while AFK!", NamedTextColor.RED,
                                                TextDecoration.BOLD)
                                        .hoverEvent(Component.text(
                                                "You must be in the AFK Pool for at least 1 minute to earn rewards!")));
                            } else {
                                whoClicked.sendMessage(Component.text("You earned " + cashEarned + " cash and "
                                        + tokensEarned + " tokens while AFK!"));
                                cashEarned = 0.0;
                                tokensEarned = 0.0;
                            }
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

    private void checkForPlayersInPool() {
        if (akfTask == null || akfTask.isCancelled()) {
            akfTask = new BukkitRunnable() {

                @Override
                public void run() {
                    if (playersInPool.isEmpty()) {
                        this.cancel();
                        Bukkit.getConsoleSender().sendMessage("No players in pool, stopping task.");
                    }
                    for (Player p : playersInPool) {
                        PlayerConfigManager pcm = new PlayerConfigManager(p.getUniqueId().toString());
                        pcm.addBalance(0.25);
                        pcm.addTokens(0.02);
                        p.sendMessage(Component.text("+ $0.25", NamedTextColor.GREEN,
                                TextDecoration.ITALIC)
                                .hoverEvent(Component.text("Cash can be used to buy most items.", NamedTextColor.GREEN,
                                        TextDecoration.ITALIC)));
                        p.sendMessage(Component.text("+ 0.02 Tokens", NamedTextColor.GOLD,
                                TextDecoration.ITALIC).hoverEvent(
                                        Component.text("Tokens can be used to buy items at the Black Martket Vendor.",
                                                NamedTextColor.GREEN, TextDecoration.ITALIC)));
                        cashEarned = cashEarned + 0.25;
                        tokensEarned = tokensEarned + 0.02;
                    }
                }

            }.runTaskTimer(plugin, (long) 20 * 60, (long) 20 * 60);
        }
    }

    public ArrayList<Player> getPlayersInPool() {
        return playersInPool;
    }
}
