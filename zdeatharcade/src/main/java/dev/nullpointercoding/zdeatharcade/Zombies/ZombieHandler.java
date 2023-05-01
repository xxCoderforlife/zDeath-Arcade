package dev.nullpointercoding.zdeatharcade.Zombies;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import dev.nullpointercoding.zdeatharcade.Main;

public class ZombieHandler implements Listener{

    private Main plugin = Main.getInstance();
    private File ZSLM = plugin.getZombieSpawnLocationFolder();
    private FileConfiguration config = new YamlConfiguration();
    private HashMap<File,Integer> zombieSpawnLocations = new HashMap<File,Integer>();
    private static Random r = Main.getRandom();
    private static Random spawnChance = Main.getSpawnChance();

    //Zombies
    private ZombieLevel1 zl1 = new ZombieLevel1();
    private ZombieLevel2 zl2 = new ZombieLevel2();

    private static int randoInt(int min,int max){
        int randoNum = r.nextInt((max - min) + 1) + min;
        return randoNum;
    }

    @EventHandler
    public void disableAllOtherMobSpawns(CreatureSpawnEvent e){
        LivingEntity c = e.getEntity();
        if(c.getType() == EntityType.ZOMBIE){
            Zombie z = (Zombie) c;
            if(ZSLM.listFiles().length == 0){
                Bukkit.getConsoleSender().sendMessage("No zombie spawn locations found! Please create some in the ZombieSpawnLocations folder!");
                return;
            }
            for(File f : ZSLM.listFiles()){
                zombieSpawnLocations.put(f, randoInt(1, 1000));
            }
            List<Integer> nums = new ArrayList<Integer>(zombieSpawnLocations.values());
            for(File f : ZSLM.listFiles()){
                if(zombieSpawnLocations.get(f) == Collections.max(nums)){
                    try{
                        config.load(f);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    Location locToTP = new Location(z.getWorld(), config.getDouble("X"), config.getDouble("Y") + 1, config.getDouble("Z"));
                    if(spawnChance.nextInt(100) > 50){
                        zl1.convertToLevel1Zombie(z);
                        z.teleportAsync(locToTP);
                        zombieSpawnLocations.clear();
                        return;
                    }
                    if(spawnChance.nextInt(100) > 40){
                        zl2.convertToLevel1Zombie(z);
                        z.teleportAsync(locToTP);
                        zombieSpawnLocations.clear();
                        return;
                    }
                }
            }

        }
        if(c.getType() == EntityType.SPIDER){
            Spider s = (Spider) c;
            s.getWorld().spawnEntity(s.getLocation(), EntityType.ZOMBIE); 
            s.remove();
            
        }
        if(c.getType() == EntityType.SKELETON){
            Skeleton sk = (Skeleton) c;
            sk.getWorld().spawnEntity(sk.getLocation(), EntityType.ZOMBIE);
            sk.remove();
        }
        if(c.getType() == EntityType.CREEPER){
            Creeper cp = (Creeper) c;
            cp.getWorld().spawnEntity(cp.getLocation(), EntityType.ZOMBIE);
            cp.remove();
        }
        if(c.getType() == EntityType.SLIME){
            Slime sl = (Slime) c;
            sl.getWorld().spawnEntity(sl.getLocation(), EntityType.ZOMBIE);
            sl.remove();
        }

    }

    @EventHandler
    public void onZombieTarget(EntityTargetLivingEntityEvent e){
        if(e.getEntity() instanceof Zombie){
            Zombie z = (Zombie) e.getEntity();
            if(z.getTarget() instanceof Player){
                if(!z.isCustomNameVisible()){
                    zl1.convertToLevel1Zombie(z);
                }
            }
        }
    }
}
