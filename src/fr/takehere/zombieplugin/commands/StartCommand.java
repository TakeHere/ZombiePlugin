package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCommand implements CommandExecutor {
    int task;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        FileConfiguration config = PluginMain.getInstance().getConfig();
        if (!PluginMain.getInstance().getIsStarted()){
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new Runnable() {
                int timer = 3;
                @Override
                public void run() {
                    if (timer == 0){
                        Bukkit.getScheduler().cancelTask(task);
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "Lancement...", ""));
                        PluginMain.getInstance().setIsStarted(true);
                        PluginMain.getInstance().getZombies().wave();
                        for (Entity entity : Bukkit.getWorld(config.getString("locations.world")).getEntities()){
                            if (PluginMain.getInstance().getMobsType().contains(entity.getType())) entity.remove();
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.getInstance(), new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Entity entity : Bukkit.getWorld(config.getString("locations.world")).getEntities()){
                                    entity.setGlowing(true);
                                }
                                Bukkit.broadcastMessage(ChatColor.GOLD + "Les zombies se montrent !");
                                SoundManager soundManager = new SoundManager();
                                Bukkit.getOnlinePlayers().forEach(player -> soundManager.alert(player.getLocation()));
                            }
                        },30*20);
                        return;
                    }
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "Lancement du jeu dans:", ChatColor.YELLOW + "" + timer));
                    timer--;
                }
            },0,20);
        }
        return false;
    }
}
