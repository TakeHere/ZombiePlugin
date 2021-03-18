package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.gui.ShopGui;
import fr.takehere.zombieplugin.gui.UpgradeGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            PluginMain.getInstance().getGuiManager().open((Player) commandSender, ShopGui.class);
        }
        return false;
    }
}
