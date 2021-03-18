package fr.takehere.zombieplugin.zombieplayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ZombiePlayer {
    private final Player player;
    private final UUID uuid;
    private final String name;
    private int Money = 0;
    private int kill = 0;

     @Deprecated
     /* @deprecated If you pass by this function, you will not add the player to the list
      */
    public ZombiePlayer(Player player){
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public Player getPlayer() {
        return player;
    }
    public UUID getUuid() {
        return uuid;
    }
    public String getName() {
        return name;
    }
    public Integer getMoney() {
        return Money;
    }
    public void setMoney(Integer money) {
        Money = money;
    }
    public void addMoney(Integer moneyToAdd){
        setMoney(getMoney() + moneyToAdd);
    }
    public void removeMoney(Integer moneyToRemove){
        setMoney(getMoney() - moneyToRemove);
    }
    public int getKill() {
        return kill;
    }
    public void setKill(int kill) {
        this.kill = kill;
    }
    public void incrementKill(){
        kill++;
    }
}
