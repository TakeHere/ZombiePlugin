package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Zombies {
    int wave = 0;
    public static void wave(Location loc, int nbZombies){
        int id = 0;
        for (int i = 0; i < nbZombies; i++) {
            ItemStack chestplate = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack();
            spawnZombie(loc, 20, false, null, null, chestplate, null);
        }
    }
    public static void spawnZombie(Location loc, double health, boolean baby,  ItemStack boots, ItemStack legging, ItemStack chestplate, ItemStack helmet){
        Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        zombie.setHealth(health);
        zombie.setBaby(baby);
        EntityEquipment entityEquipment = zombie.getEquipment();
        if (boots!=null){
            entityEquipment.setBoots(boots);
        }
        if (legging!=null){
            entityEquipment.setLeggings(legging);
        }
        if (chestplate!=null){
            entityEquipment.setChestplate(chestplate);
        }
        if (helmet!=null){
            entityEquipment.setHelmet(helmet);
        }
    }
}
