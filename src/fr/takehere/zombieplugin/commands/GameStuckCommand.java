package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.GameSupervisor;
import fr.takehere.zombieplugin.PluginMain;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class GameStuckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (Entity entity : Bukkit.getWorld(PluginMain.getInstance().getConfig().getString("locations.world")).getEntities()){
            if (PluginMain.getInstance().getMobsType().contains(entity.getType())){
                entity.remove();
                PluginMain.getInstance().getZombies().decrementTotalZombiesSpawned();
            }
        }
        new GameSupervisor().intermissionAndStart();
        return false;
    }
}
