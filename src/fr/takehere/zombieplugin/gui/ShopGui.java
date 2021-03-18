package fr.takehere.zombieplugin.gui;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.weapons.WeaponManager;
import fr.takehere.zombieplugin.util.GuiBuilder;
import fr.takehere.zombieplugin.util.ItemBuilder;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        WeaponManager weaponManager = PluginMain.getInstance().getWeaponManager();

        int moneyPump = config.getInt("price.pump");
        int moneySniper = config.getInt("price.sniper");
        int moneyBazooka = config.getInt("price.bazooka");
        int moneySword = config.getInt("price.sword");

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).setName("§c§lAssasinator").setLore("§ePrix: §f" +  moneySword + "§e$").toItemStack();
        ItemStack axe = weaponManager.getWeaponByName("Fusil a pompe").getItemStack();
        ItemStack pickaxe = weaponManager.getWeaponByName("Sniper").getItemStack();
        ItemStack shovel = weaponManager.getWeaponByName("Bazooka").getItemStack();

        items.put(axe, moneyPump);
        items.put(pickaxe, moneySniper);
        items.put(shovel, moneyBazooka);
        items.keySet().forEach(itemStack -> PluginMain.setLore(itemStack, weaponManager.getWeaponByItemStack(itemStack).getLore()));
        items.put(sword, moneySword);

        ItemStack money = new ItemBuilder(Material.GOLD_INGOT).setName("Money").toItemStack();

        inv.setItem(10,sword);
        inv.setItem(14,axe);
        inv.setItem(12,pickaxe);
        inv.setItem(16,shovel);

        inv.setItem(19, PluginMain.setLore(money, moneySword + "$"));
        inv.setItem(21, PluginMain.setLore(money, moneySniper + "$"));
        inv.setItem(23, PluginMain.setLore(money, moneyPump + "$"));
        inv.setItem(25, PluginMain.setLore(money, moneyBazooka + "$"));

    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
        if (player.getInventory().contains(current)){
            player.sendMessage(ChatColor.RED + "Vous avez déjà acheté cet arme !");
        }else if (items.containsKey(current)){
            System.out.println("buy");
            System.out.println("buy");
            System.out.println("buy");
            System.out.println("buy");
            System.out.println("buy");
            buyItem(player, current, items.get(current));
        }
        player.closeInventory();
    }

    public void buyItem(Player player, ItemStack itemStack, int money){
        ZombiePlayer zombiePlayer = PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player);
        if (zombiePlayer.getMoney() >= money){
            player.getInventory().addItem(itemStack);
            zombiePlayer.removeMoney(money);
            player.sendMessage("§2[§aSuccès§2] SaVous venez d'acheter §2" + itemStack.getItemMeta().getDisplayName() + " à §2" + money + "$");
        }else {
            player.sendMessage("§cErreur: Vous n'avez pas assez d'argent !");
        }
    }
}
