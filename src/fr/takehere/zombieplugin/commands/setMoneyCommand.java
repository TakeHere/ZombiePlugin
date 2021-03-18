package fr.takehere.zombieplugin.commands;

import com.mysql.jdbc.StringUtils;
import fr.takehere.zombieplugin.PluginMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class setMoneyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        try{
            int num = Integer.parseInt(strings[0]);
            PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player).setMoney(Integer.parseInt(strings[0]));
            player.sendMessage("!");
        } catch (NumberFormatException e) {
            player.sendMessage("§cErreur: Veuillez spécifiez un nombre correct.");
        }

        return false;
    }
}