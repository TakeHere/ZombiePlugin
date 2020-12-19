package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.PluginMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setMoneyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player).setMoney(Integer.parseInt(strings[0]));
        return false;
    }
}
