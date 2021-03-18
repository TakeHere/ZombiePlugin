package fr.takehere.zombieplugin.gui;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.weapons.Weapon;
import fr.takehere.zombieplugin.weapons.WeaponManager;
import fr.takehere.zombieplugin.util.GuiBuilder;
import fr.takehere.zombieplugin.util.ItemBuilder;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class UpgradeGui implements GuiBuilder {
    int newDamage, newTickCooldown;

    @Override
    public String name() {
        return "TestGui";
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public void contents(Player player, Inventory inv) {
        ItemStack pane = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.WHITE).setName("").toItemStack();
        for (int i = 0; i < getSize(); i++) {
            inv.setItem(i, pane);
        }

        ItemStack playerItem = player.getItemInHand();
        WeaponManager weaponManager = PluginMain.getInstance().getWeaponManager();
        Weapon weapon = weaponManager.getWeaponByItemStack(playerItem);

        newDamage = (int) (weapon.getDamage() * PluginMain.getInstance().getConfig().getDouble("upgrades.damageMultiplier"));
        newTickCooldown = (int) (weapon.getTickCooldown() / PluginMain.getInstance().getConfig().getDouble("upgrades.cooldownDivider"));

        ItemStack finalItem = new ItemBuilder(playerItem.getType()).setName(playerItem.getItemMeta().getDisplayName()).setLore(
                playerItem.getItemMeta().getLore().get(0) + "§2 → " + new DecimalFormat("#.##").format((double)20/newTickCooldown)
                , playerItem.getItemMeta().getLore().get(1) + "§2 → " + newDamage
                , ""
                , "§f" + weapon.getUpgradeCost() + "§2$")
                .toItemStack();

        inv.setItem(13, finalItem);
        inv.setItem(14, new ItemBuilder(Material.STAINED_GLASS).setWoolColor(DyeColor.GREEN).setName(ChatColor.GREEN + "Oui").toItemStack());
        inv.setItem(12, new ItemBuilder(Material.STAINED_GLASS).setWoolColor(DyeColor.RED).setName(ChatColor.RED + "Non").toItemStack());
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
        WeaponManager weaponManager = PluginMain.getInstance().getWeaponManager();
        Weapon weapon = weaponManager.getWeaponByItemStack(player.getItemInHand());
        System.out.println("d " + player.getItemInHand());
        ZombiePlayer zombiePlayer = PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player);
        if (!current.hasItemMeta()) return;
        System.out.println(current.getItemMeta().getDisplayName());
        if (current.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Oui")){
            if (zombiePlayer.getMoney() >= weapon.getUpgradeCost()){
                zombiePlayer.removeMoney(weapon.getUpgradeCost());
                weapon.setDamage(newDamage);
                weapon.setTickCooldown(newTickCooldown);
                weapon.setUpgradeCost(weapon.getUpgradeCost() * PluginMain.getInstance().getConfig().getInt("upgrades.moneyMultiplier"));
                List<String> lore = Arrays.asList(ChatColor.GOLD + "Vitesse: " + ChatColor.WHITE + new DecimalFormat("#.##").format((double)20/weapon.getTickCooldown()) + ChatColor.GOLD + "/tirs par secondes", ChatColor.GOLD + "Dégâts: " + ChatColor.WHITE + weapon.getDamage());
                PluginMain.setLore(player.getItemInHand(), lore);
                player.closeInventory();
                return;
            }
            player.sendMessage("pas money");
        }else if (current.getItemMeta().getDisplayName().equals(ChatColor.RED + "Non")){
            player.closeInventory();
            player.sendMessage("miam");
        }
    }
}
