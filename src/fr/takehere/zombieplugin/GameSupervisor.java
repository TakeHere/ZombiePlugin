package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.locations.SpawnLocationUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameSupervisor implements Listener {
    int counterTask;
    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){
        if (PluginMain.getInstance().isStarted){
            if (PluginMain.getInstance().getMobsType().contains(e.getEntityType())){
                PluginMain.getInstance().getZombies().decrementTotalZombiesSpawned();
                if (!PluginMain.getInstance().getZombies().isSpawningZombies()){
                    if (PluginMain.getInstance().getZombies().totalZombiesSpawned == 0){
                        intermissionAndStart();
                        return;
                    }
                }
            }
        }
    }

    public void intermissionAndStart(){
        for (Player player : Bukkit.getOnlinePlayers()){
            player.setHealth(20);
            player.setFoodLevel(20);

            player.sendTitle(ChatColor.GOLD + "Fin de la manche n°" + PluginMain.getInstance().getZombies().getWave() + " !",ChatColor.GOLD + "Tout le monde gagne: " + PluginMain.getInstance().getConfig().getInt("moneyPerWave"));
            PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player).addMoney(PluginMain.getInstance().getConfig().getInt("moneyPerWave"));
            new SoundManager().waveFinished(player.getLocation());

            if (player.getGameMode().equals(GameMode.SPECTATOR)){
                player.setGameMode(GameMode.ADVENTURE);
                player.teleport(SpawnLocationUtil.getLocationFromString(PluginMain.getInstance().getConfig().getString("locations.spawn"), Bukkit.getWorld(PluginMain.getInstance().getConfig().getString("locations.world"))));
            }
        }
        for (Entity entity : Bukkit.getWorld(PluginMain.getInstance().getConfig().getString("locations.world")).getEntities()){
            if (entity.getType().equals(EntityType.ARROW)){
                entity.remove();
            }
        }
        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "Fin de la manche n°" + PluginMain.getInstance().getZombies().getWave() + " !",ChatColor.GOLD + "Tout le monde gagne: " + PluginMain.getInstance().getConfig().getInt("moneyPerWave")));
        Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                counterTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
                    int timer = 3;
                    @Override
                    public void run() {
                        if (timer == 0){
                            Bukkit.getScheduler().cancelTask(counterTask);
                            for (Player player : Bukkit.getOnlinePlayers()){
                                player.setHealth(20);
                                player.setFoodLevel(20);
                                player.sendTitle(ChatColor.GOLD + "Lancement de la vague...", "");
                                Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.getInstance(), new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Entity entity : Bukkit.getWorld(PluginMain.getInstance().getConfig().getString("locations.world")).getEntities()){
                                            entity.setGlowing(true);
                                        }
                                        Bukkit.broadcastMessage(ChatColor.GOLD + "Les zombies se montrent !");
                                        SoundManager soundManager = new SoundManager();
                                        Bukkit.getOnlinePlayers().forEach(player -> soundManager.alert(player.getLocation()));
                                    }
                                },30*20);
                            }
                            PluginMain.getInstance().getZombies().wave();
                            return;
                        }
                        //
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "Debut de la vague dans:", ChatColor.YELLOW + "" + timer ));
                        timer--;
                    }
                },0,20);
                return;
            }
        },PluginMain.getInstance().getConfig().getInt("betweenWaveTime") * 20);
    }
}
