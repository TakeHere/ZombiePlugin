package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {
    PluginMain main;
    public MoneyCommand(PluginMain pluginMain) {
        this.main = pluginMain;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.sendMessage(ChatColor.GOLD + "Vous avez: " + ChatColor.GREEN + main.getZombiePlayerManager().getZombiePlayer(player).getMoney() + "$");
        return false;
    }
}
