package fr.takehere.zombieplugin.weapons;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.SoundManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Weapon implements Listener{
    private ItemStack itemStack;
    boolean gravity, bounce;
    private int howMany, velocity, spreading, kb, damage, attackId, tickCooldown;
    List<String> lore;
    HashMap<Player, Long> cooldown = new HashMap<>();
    int upgradeCost = PluginMain.getInstance().getConfig().getInt("upgrades.cost");

    public Weapon(ItemStack itemStack, boolean gravity, boolean bounce, int howMany, int velocity, int spreading, int kb, int damage, int attackId, int tickCooldown) {
        this.itemStack = itemStack;
        this.gravity = gravity;
        this.bounce = bounce;
        this.howMany = howMany;
        this.velocity = velocity;
        this.spreading = spreading;
        this.kb = kb;
        this.damage = damage;
        this.attackId = attackId;
        this.tickCooldown = tickCooldown;
        this.lore = Arrays.asList(ChatColor.GOLD + "Vitesse: " + ChatColor.WHITE + new DecimalFormat("#.##").format((double)20/getTickCooldown()) + ChatColor.GOLD + "/tirs par secondes", ChatColor.GOLD + "Dégâts: " + ChatColor.WHITE + getDamage());
        updateLore();
    }

    @EventHandler
    public void shoot(PlayerInteractEvent e){
        if (e.getItem() != null && itemStack.hasItemMeta()){
            ItemStack itemStack = e.getItem();
            if (itemStack.getItemMeta().getDisplayName() == null)return;
            if (itemStack.getType().equals(this.itemStack.getType()) && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(this.itemStack.getItemMeta().getDisplayName())){
                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    if(cooldown.containsKey(e.getPlayer())) {
                        long secondsLeft = ((cooldown.get(e.getPlayer())/50)+this.tickCooldown) - (System.currentTimeMillis()/50);
                        if(secondsLeft>0) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    cooldown.put(e.getPlayer(), System.currentTimeMillis());
                    new SoundManager().shoot(e.getPlayer().getLocation());
                    spawnArrows(e.getPlayer(), gravity, howMany, velocity, spreading, bounce, kb, damage, attackId);
                    e.setCancelled(true);
                    return;
                 }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void projectileHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Projectile){
            if(e.getHitBlock() != null){
                //Ground Touched
                Projectile projectile = e.getEntity();
                if (!projectile.getName().equalsIgnoreCase("Arrow")){
                    String projectileName[] = projectile.getName().split("/");
                    int attackId = Integer.parseInt(projectileName[1]);
                    if (attackId == 1){
                        Location loc = e.getEntity().getLocation();
                        createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 10);
                    }else if(attackId == 2){
                        Location loc = e.getEntity().getLocation();
                        createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 10);
                    }
                }
                e.getEntity().remove();
                return;
            }
        }
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) e.getDamager();
            if (e.getEntity() instanceof Player){
                //Hit player
                projectile.remove();
                e.setCancelled(true);
                return;
            }else{
                //Hit mob
                new SoundManager().hit(e.getEntity().getLocation());
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
                        createExplosion(loc, (Entity) projectile.getShooter(), 5, 3, 100);
                        break;
                }

                projectile.remove();
                return;
            }
        }else if(e.getEntity().getType().equals(EntityType.PLAYER) && e.getDamager().getType().equals(EntityType.PLAYER)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityAttackEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            for (Weapon weapon : PluginMain.getInstance().getWeaponManager().getWeaponList()){
                if (weapon.getItemStack().isSimilar(((Player) e.getDamager()).getItemInHand())){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    public void spawnArrows(Player player, boolean gravity, int howMany, int velocity, int spreading, boolean bounce, int kb, int damage, int attackId){
        for (int i = 0 ; i < howMany ; i++){
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection();
            Arrow arrow = player.getWorld().spawnArrow(loc.add(direction.multiply(1)), direction.normalize(), velocity, spreading);

            arrow.setKnockbackStrength(kb);
            arrow.setGravity(gravity);
            arrow.setBounce(bounce);
            arrow.setShooter(player);
            arrow.setCustomName(damage + "/" + attackId);
            arrow.setCustomNameVisible(false);
            arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);

            player.spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), 0, 0.25f,0.25f,0.25f);
        }
    }

    public void createExplosion(Location loc, Entity responsible, int reachSize, int particleSize, double damage){
        World world = loc.getWorld();
        for (Entity entity : world.getNearbyEntities(loc, reachSize, reachSize, reachSize)){
            if (entity != null && entity instanceof Damageable && !entity.getType().equals(EntityType.PLAYER)){
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(damage, responsible);
                new SoundManager().hit(entity.getLocation());
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

    public void updateLore(){
        List<String> lore = Arrays.asList(ChatColor.GOLD + "Vitesse: " + ChatColor.WHITE + (float)tickCooldown/20 + ChatColor.GOLD + "/tirs par secondes", ChatColor.GOLD + "Dégâts: " + ChatColor.WHITE + damage);
        PluginMain.setLore(itemStack, lore);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isGravity() {
        return gravity;
    }

    public boolean isBounce() {
        return bounce;
    }

    public int getHowMany() {
        return howMany;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getSpreading() {
        return spreading;
    }

    public int getKb() {
        return kb;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackId() {
        return attackId;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setTickCooldown(int tickCooldown) {
        this.tickCooldown = tickCooldown;
    }

    public int getTickCooldown() {
        return tickCooldown;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }
}
