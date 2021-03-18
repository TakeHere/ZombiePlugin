package fr.takehere.zombieplugin.weapons;

import fr.takehere.zombieplugin.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeaponManager {
    List<Weapon> weaponList;
    public WeaponManager() {
        weaponList = new ArrayList<>();
    }

    public void registerWeapon(ItemStack itemStack, boolean gravity, boolean bounce, int howMany, int velocity, int spreading, int kb, int damage, int attackId, int tickCooldown){
        Weapon weapon = new Weapon(itemStack, gravity, bounce, howMany, velocity, spreading, kb, damage, attackId, tickCooldown);
        weaponList.add(weapon);
        Bukkit.getServer().getPluginManager().registerEvents(weapon, PluginMain.getInstance());
    }

    public Weapon getWeaponByName(String name){
        for(Weapon weapon : weaponList){
            if (weapon.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(name)){
                return weapon;
            }
        }
        throw new NullPointerException("Aucune arme n'a été trouvé avec le nom: " + name);
    }

    public Weapon getWeaponByItemStack(ItemStack itemStack){
        System.out.println("get " + itemStack);
        for (Weapon weapon : weaponList){
            if (weapon.getItemStack().getType().equals(itemStack.getType())){
                return weapon;
            }
        }
        throw new NullPointerException("Aucune arme n'a été trouvé avec l'itemstack: " + itemStack);
    }

    public List<Weapon> getWeaponList() {
        return weaponList;
    }
}
