package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.gui.ShopGui;
import fr.takehere.zombieplugin.util.CooldownManager;
import net.minecraft.server.v1_12_R1.Explosion;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WeaponListener implements Listener {
    PluginMain main = PluginMain.getInstance();
    CooldownManager cooldownManager = new CooldownManager();
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (e.getItem() != null && e.getItem().hasItemMeta()){
                ItemStack item = e.getItem();
                switch(item.getType()) {
                    case IRON_HOE:
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Fusil d'assaut")){
                            int cooldown = (int) (20/4f);
                            System.out.println(cooldown);
                            int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                            if(timeLeft == 0){
                                spawnArrows(player, true, 1, 3, 0, false, 0,7,0);
                                e.setCancelled(true);
                                cooldownManager.setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                                        cooldownManager.setCooldown(player.getUniqueId(), --timeLeft);
                                        if(timeLeft == 0){
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(main, cooldown, cooldown);
                            }else{
                                e.setCancelled(true);
                                return;
                            }
                        }
                        break;
                    case IRON_AXE:
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Fusil a pompe")){
                            int cooldown = (int) (20/1.5f);
                            System.out.println(cooldown);
                            int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                            if(timeLeft == 0){
                                spawnArrows(player, true,10, 1, 10, false, 1,10,0);
                                e.setCancelled(true);
                                cooldownManager.setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                                        cooldownManager.setCooldown(player.getUniqueId(), --timeLeft);
                                        if(timeLeft == 0){
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(main, cooldown, cooldown);
                            }else{
                                e.setCancelled(true);
                                return;
                            }
                        }
                        break;
                    case IRON_PICKAXE:
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Sniper")){
                            int cooldown = (int) (20/1f);
                            System.out.println(cooldown);
                            int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                            if(timeLeft == 0){
                                spawnArrows(player, false,1, 3, 0, false, 3,30,0);
                                e.setCancelled(true);
                                cooldownManager.setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                                        cooldownManager.setCooldown(player.getUniqueId(), --timeLeft);
                                        if(timeLeft == 0){
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(main, cooldown, cooldown);
                            }else{
                                e.setCancelled(true);
                                return;
                            }
                        }
                        break;
                    case IRON_SPADE:
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Bazooka")){
                            int cooldown = (int) (20/0.7f);
                            System.out.println(cooldown);
                            int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                            if(timeLeft == 0){
                                spawnArrows(player, true,1, 1, 2, false, 3,0,1);
                                e.setCancelled(true);
                                cooldownManager.setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        int timeLeft = cooldownManager.getCooldown(player.getUniqueId());
                                        cooldownManager.setCooldown(player.getUniqueId(), --timeLeft);
                                        if(timeLeft == 0){
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(main, cooldown, cooldown);
                            }else{
                                e.setCancelled(true);
                                return;
                            }
                        }
                        break;
                    case DIAMOND_SWORD:
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Cheat")){
                            spawnArrows(player, false,1, 4, 0, false, 3,50,2);
                            e.setCancelled(true);
                        }
                        break;
                }
            }
        }
    }

    @EventHandler
    public void projectileHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Projectile){
            if(e.getHitBlock() != null){
                //Ground Touched
                Projectile projectile = e.getEntity();
                String projectileName[] = projectile.getName().split("/");
                int attackId = Integer.parseInt(projectileName[1]);
                if (attackId == 1){
                    Location loc = e.getEntity().getLocation();
                    createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 10);
                }else if(attackId == 2){
                    Location loc = e.getEntity().getLocation();
                    createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 10);
                }
                e.getEntity().remove();
                return;
            }
        }
    }
    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) e.getDamager();
            if (e.getEntity() instanceof Player){
                //Hit player
                projectile.remove();
                e.setCancelled(true);
                return;
            }else{
                //Hit mob
                String projectileName[] = projectile.getName().split("/");
                int damage = Integer.parseInt(projectileName[0]);
                int attackId = Integer.parseInt(projectileName[1]);
                e.setDamage(damage);
                Location loc = e.getEntity().getLocation();
                switch(attackId) {
                case 1:
                    e.getEntity().getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 1f, false, false);
                    createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 10);
                    break;
                case 2:
                    e.getEntity().getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 1f, false, false);
                    createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 10);
                    break;
                }

                projectile.remove();
                return;
            }
        }
    }

    @EventHandler
    public void EntityDamage(EntityDamageEvent e){
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        if (damageCause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){

        }
    }

    public void spawnArrows(Player player, boolean gravity, int howMany, int velocity, int spreading, boolean bounce, int kb, int damage, int attackId){
        for (int i = 0 ; i < howMany ; i++){
            Location loc = player.getEyeLocation().add(player.getEyeLocation().getDirection());
            Vector vector = loc.getDirection();
            Arrow arrow = player.getWorld().spawnArrow(loc.add(vector), vector.normalize(), velocity, spreading);
            arrow.setKnockbackStrength(kb);
            arrow.setGravity(gravity);
            arrow.setBounce(bounce);
            arrow.setShooter(player);
            arrow.setCustomName(damage + "/" + attackId);
            arrow.setCustomNameVisible(false);
            arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
        }
    }

    public void createExplosion(Location loc, Entity responsible, int reachSize, int particleSize, double damage){
        World world = loc.getWorld();
        for (Entity entity : world.getNearbyEntities(loc, reachSize, reachSize, reachSize)){
            if (entity != null && entity instanceof Damageable){
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(damage, responsible);
            }
        }
        if (particleSize == 1){
            world.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 4);
        }else if (particleSize == 2){
            world.spawnParticle(Particle.EXPLOSION_LARGE, loc, 4);
        }else if (particleSize >= 3){
            world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 4);
        }
         return;
    }
}
