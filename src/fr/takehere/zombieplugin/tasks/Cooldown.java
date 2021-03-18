package fr.takehere.zombieplugin.tasks;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.Zombies;
import fr.takehere.zombieplugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class Cooldown {
    private int task, timer, ticktime;
    private boolean isOnCooldown;
    public Cooldown(int ticktime) {
        this.task = 0;
        this.ticktime = ticktime;
        this.isOnCooldown = false;
    }

    public void run(){
        if (isOnCooldown = false){
            timer = ticktime;
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    if (timer == 0){
                        isOnCooldown = false;
                        timer = ticktime;
                        stop();
                    }
                    timer--;
                    isOnCooldown = true;
                }
            },1 , 1);
        }
    }

    public void stop(){
        Bukkit.getScheduler().cancelTask(task);
        isOnCooldown = false;
    }

    public boolean getIsOnCooldown() {
        return isOnCooldown;
    }

    public int getTask() {
        return task;
    }

    public int getTimer() {
        return timer;
    }

    public int getTicktime() {
        return ticktime;
    }
}
