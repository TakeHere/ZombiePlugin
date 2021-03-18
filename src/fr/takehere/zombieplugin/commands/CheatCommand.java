package fr.takehere.zombieplugin.commands;

import fr.takehere.zombieplugin.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            ItemStack itemStack = new ItemBuilder(Material.DIAMOND_SWORD).setName("Cheat").setLore("Admin only weapon").toItemStack();
            player.getInventory().addItem(itemStack);
            return false;
        }
        return false;
    }
}
