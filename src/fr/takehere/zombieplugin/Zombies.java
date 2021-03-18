package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.locations.SpawnLocation;
import fr.takehere.zombieplugin.tasks.WaveTask;
import fr.takehere.zombieplugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Zombies {
    int wave = 0;
    int totalZombiesSpawned = 0;
    boolean spawningZombies = false;
    int zombiesToSpawn = PluginMain.getInstance().getConfig().getInt("baseNbZombies");

    public void wave(){
        PluginMain pluginMain = PluginMain.getInstance();
        FileConfiguration config = PluginMain.getInstance().getConfig();
        float zombiesMultiplier = (float) config.getDouble("zombiesMultiplier");
        int tickTime = config.getInt("zombieSpawnTickTime");
        for(SpawnLocation spawnLocation : pluginMain.getSpawnLocationManager().getSpawnLocations()){
            if (spawnLocation.isEnabled()){
                new WaveTask(tickTime, (int) (zombiesToSpawn * zombiesMultiplier), spawnLocation.getLocations());
            }
        }
        wave++;
        zombiesToSpawn = (int) (zombiesToSpawn *zombiesMultiplier);
    }

    public void spawnZombie(Location loc,  ItemStack boots, ItemStack legging, ItemStack chestplate, ItemStack helmet){
        int random = new Random().nextInt(10);
        String randomName = PluginMain.getInstance().mobsNames.get(new Random().nextInt(PluginMain.getInstance().mobsNames.size()));
        if (random == 0 || random == 1 || random == 2 || random == 3){
            Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            zombie.setHealth(20);
            zombie.setCustomName(randomName);
            zombie.setCustomNameVisible(true);
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            zombie.setBaby(false);
            equipStuff(zombie, boots, legging, chestplate, helmet);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity:zombie.getNearbyEntities(1000,50,1000)){
                        if (entity.getType().equals(EntityType.PLAYER)){
                            zombie.setTarget((LivingEntity) entity);
                            return;
                        }
                    }
                }
            },0,10);
        }else if(random == 4 || random == 5 || random == 6){
            PigZombie zombie = (PigZombie) loc.getWorld().spawnEntity(loc, EntityType.PIG_ZOMBIE);
            zombie.setBaby(false);
            zombie.setMaxHealth(30);
            zombie.setHealth(30);
            zombie.setCustomName(randomName);
            zombie.setCustomNameVisible(true);
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            zombie.setAngry(true);
            equipStuff(zombie, boots, legging, chestplate, helmet);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity:zombie.getNearbyEntities(1000,50,1000)){
                        if (entity.getType().equals(EntityType.PLAYER)){
                            zombie.setTarget((LivingEntity) entity);
                            return;
                        }
                    }
                }
            },0,10);
        }else if(random == 7 || random == 8){
            Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            zombie.setGlowing(true);
            zombie.setHealth(1);
            zombie.setCustomName(randomName);
            zombie.setCustomNameVisible(true);
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            zombie.setBaby(true);
            ItemStack skull  = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Gaygus").toItemStack();
            equipStuff(zombie, boots, legging, chestplate, skull);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity:zombie.getNearbyEntities(1000,50,1000)){
                        if (entity.getType().equals(EntityType.PLAYER)){
                            zombie.setTarget((LivingEntity) entity);
                            return;
                        }
                    }
                }
            },0,10);
        }else {
            WitherSkeleton zombie = (WitherSkeleton) loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
            zombie.setMaxHealth(40);
            zombie.setHealth(40);
            zombie.setGlowing(true);
            zombie.setCustomName(randomName);
            zombie.setCustomNameVisible(true);
            zombie.getEquipment().setHelmet(new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner("Gaygus").toItemStack());
            Bukkit.getScheduler().scheduleSyncRepeatingTask(PluginMain.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity:zombie.getNearbyEntities(1000,50,1000)){
                        if (entity.getType().equals(EntityType.PLAYER)){
                            zombie.setTarget((LivingEntity) entity);
                            return;
                        }
                    }
                }
            },0,10);
        }
    }

    public void equipStuff(Entity entity, ItemStack boots, ItemStack legging, ItemStack chestplate, ItemStack helmet){
        LivingEntity livingEntity = (LivingEntity) entity;
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (boots!=null) entityEquipment.setBoots(boots);
        if (legging!=null) entityEquipment.setLeggings(legging);
        if (chestplate!=null) entityEquipment.setChestplate(chestplate);
        if (helmet!=null) entityEquipment.setHelmet(helmet);
    }

    public int getWave() {
        return wave;
    }

    public boolean isSpawningZombies() {
        return spawningZombies;
    }

    public void setSpawningZombies(boolean spawningZombies) {
        this.spawningZombies = spawningZombies;
    }

    public int getTotalZombiesSpawned() {
        return totalZombiesSpawned;
    }

    public void setTotalZombiesSpawned(int totalZombiesSpawned) {
        this.totalZombiesSpawned = totalZombiesSpawned;
    }

    public void decrementTotalZombiesSpawned(){
        totalZombiesSpawned--;
    }

    public void incrementTotalZombiesSpawned(){
        totalZombiesSpawned++;
    }
}
