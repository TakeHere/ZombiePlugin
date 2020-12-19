package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.Zombies;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {
    int task;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (PluginMain.getInstance().getIsStarted() == false){
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new Runnable() {
                int timer = 1 + 3; //1 + number
                @Override
                public void run() {
                    timer--;
                    if (timer == 0){
                        Bukkit.getScheduler().cancelTask(task);
                        for (Player player : Bukkit.getOnlinePlayers()){
                            Bukkit.getOnlinePlayers().forEach(player1 -> player.sendTitle(ChatColor.GOLD + "Lancement...", ""));
                        }
                        PluginMain.getInstance().setIsStarted(true);
                        Zombies.wave(new Location(Bukkit.getWorld("world"), 0,80,0), 20);
                        return;
                        //Game lancée
                    }
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.GOLD + "Lancement du jeu dans:", ChatColor.YELLOW + "" + timer));
                }
            },0,20);
        }
        return false;
    }
}
