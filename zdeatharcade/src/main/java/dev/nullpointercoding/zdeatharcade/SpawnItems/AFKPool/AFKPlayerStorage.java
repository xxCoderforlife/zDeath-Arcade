package dev.nullpointercoding.zdeatharcade.SpawnItems.AFKPool;

import java.util.HashMap;
import java.util.Map;

public class AFKPlayerStorage {
    private Map<String, AFKPlayer> afkPlayerMap;

    public AFKPlayerStorage() {
        afkPlayerMap = new HashMap<>();
    }

    public void addAFKPlayer(AFKPlayer afkPlayer) {
        afkPlayerMap.put(afkPlayer.getPlayer().getName(), afkPlayer);
    }

    public void removeAFKPlayer(AFKPlayer playerName) {
        afkPlayerMap.remove(playerName.getPlayer().getName());
    }

    public AFKPlayer getAFKPlayer(String playerName) {
        return afkPlayerMap.get(playerName);
    }

    public boolean hasAFKPlayer(String playerName) {
        return afkPlayerMap.containsKey(playerName);
    }
}
