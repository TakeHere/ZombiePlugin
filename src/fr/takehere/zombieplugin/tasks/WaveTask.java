package fr.takehere.zombieplugin.tasks;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.Zombies;
import fr.takehere.zombieplugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class WaveTask {
    private int task;
    private final int timer;
    private final int ticktime;
    private int nbZombies;
    private final List<Location> locs;

    public WaveTask(int ticktime, int nbZombies, List<Location> locs) {
        this.task = 0;
        this.timer = 3;
        this.ticktime = ticktime;
        this.nbZombies = nbZombies;
        this.locs = locs;
        run();
    }

    public void run(){
        Zombies zombies = PluginMain.getInstance().getZombies();
        zombies.setSpawningZombies(true);
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (nbZombies == 0) {
                    zombies.setSpawningZombies(false);
                    stop();
                }else {
                    zombies.spawnZombie(locs.get(new Random().nextInt(locs.size())), null, null, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack(), null);
                    nbZombies--;
                }
            }
        },0,ticktime);
    }

    public void stop(){
        Bukkit.getScheduler().cancelTask(task);
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

    public List<Location> getLocs() {
        return locs;
    }
}
