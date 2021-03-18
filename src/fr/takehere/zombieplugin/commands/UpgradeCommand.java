package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.weapons.Weapon;
import fr.takehere.zombieplugin.gui.UpgradeGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpgradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            for (Weapon weapon : PluginMain.getInstance().getWeaponManager().getWeaponList()){
                if (weapon.getItemStack().getType().equals(player.getItemInHand().getType())){
                    System.out.println("oui");
                    PluginMain.getInstance().getGuiManager().open(player, UpgradeGui.class);
                    return false;
                }
            }
        }
        return false;
    }
}
