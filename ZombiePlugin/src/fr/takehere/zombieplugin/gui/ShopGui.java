package fr.takehere.zombieplugin.gui;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.util.GuiBuilder;
import fr.takehere.zombieplugin.util.ItemBuilder;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShopGui implements GuiBuilder {
    HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();

    @Override
    public String name() {
        return "Shop";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public void contents(Player player, Inventory inv) {
        FileConfiguration config = PluginMain.getInstance().getConfig();
        int moneyRiffle = config.getInt("price.riffle");
        int moneyPump = config.getInt("price.bazooka");
        int moneySniper = config.getInt("price.sniper");
        int moneyBazooka = config.getInt("price.pump");
        ItemStack hoe = new ItemBuilder(Material.IRON_HOE).setName("Fusil d'assaut").toItemStack();
        ItemStack axe = new ItemBuilder(Material.IRON_AXE).setName("Fusil a pompe").toItemStack();
        ItemStack pickaxe = new ItemBuilder(Material.IRON_PICKAXE).setName("Sniper").toItemStack();
        ItemStack shovel = new ItemBuilder(Material.IRON_SPADE).setName("Bazooka").toItemStack();

        items.put(hoe, moneyRiffle);
        items.put(axe, moneyPump);
        items.put(pickaxe, moneySniper);
        items.put(shovel, moneyBazooka);

        ItemStack money = new ItemBuilder(Material.GOLD_INGOT).setName("Money").toItemStack();

        inv.setItem(10,hoe);
        inv.setItem(14,axe);
        inv.setItem(12,pickaxe);
        inv.setItem(16,shovel);

        inv.setItem(19, setLore(money, moneyRiffle + "$"));
        inv.setItem(23, setLore(money, moneyPump + "$"));
        inv.setItem(21, setLore(money, moneySniper + "$"));
        inv.setItem(25, setLore(money, moneyBazooka + "$"));

    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
        if (player.getInventory().contains(current)){
            player.sendMessage(ChatColor.RED + "Vous avez déjà acheté cet arme !");
        }else if (items.containsKey(current)){
            buyItem(player, current, items.get(current));
        }
        player.closeInventory();
    }

    public static ItemStack setLore(ItemStack is, String lore){
        if (!is.hasItemMeta()){
            return null;
        }
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return is;

    }
    public void buyItem(Player player, ItemStack itemStack, int money){
        ZombiePlayer zombiePlayer = PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player);
        int pmoney = zombiePlayer.getMoney();
        if (pmoney >= money){
            player.getInventory().addItem(itemStack);
            zombiePlayer.removeMoney(money);
            player.sendMessage(ChatColor.GREEN + "Vous venez d'acheter une arme à " + money + "$");
        }else {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent !");
        }
    }
}
