package fr.takehere.zombieplugin.zombieplayer;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ZombiePlayerManager {
    List<ZombiePlayer> ZombiePlayersArray;
    public ZombiePlayerManager(){
        ZombiePlayersArray = new ArrayList<>();
    }
    public boolean doesPlayerExist(Player player){
        return getZombiePlayerFromUUID(player.getUniqueId()) != null;
    }

    public List<ZombiePlayer> getZombiePlayersArray() {
        return ZombiePlayersArray;
    }
    public ZombiePlayer getZombiePlayerFromUUID(UUID uuid){
        for(ZombiePlayer zombiePlayer : ZombiePlayersArray){
            if(zombiePlayer.getUuid().equals(uuid)) {
                return zombiePlayer;
            }
        }
        return null;
    }
    public ZombiePlayer getZombiePlayer(Player player){
        for(ZombiePlayer zombiePlayer : ZombiePlayersArray){
            if(zombiePlayer.getUuid().equals(player.getUniqueId())) {
                return zombiePlayer;
            }
        }
        return null;
    }
    public ZombiePlayer newZombiePlayer(Player player){
        ZombiePlayer newPlayer = new ZombiePlayer(player);
        getZombiePlayersArray().add(newPlayer);
        return newPlayer;
    }
    public void removeZombiePlayer(ZombiePlayer zombiePlayer){
        getZombiePlayersArray().remove(zombiePlayer);
    }
}
