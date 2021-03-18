package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.commands.*;
import fr.takehere.zombieplugin.gui.ShopGui;
import fr.takehere.zombieplugin.gui.UpgradeGui;
import fr.takehere.zombieplugin.locations.SpawnLocationManager;
import fr.takehere.zombieplugin.locations.SpawnLocationUtil;
import fr.takehere.zombieplugin.util.*;
import fr.takehere.zombieplugin.weapons.WeaponManager;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayerManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;



public class PluginMain extends JavaPlugin {
    ZombiePlayerManager manager;
    List<EntityType> mobsType = Arrays.asList(EntityType.WITHER_SKELETON, EntityType.PIG_ZOMBIE, EntityType.ZOMBIE);
    List<String> mobsNames;
    World world;
    Location spawn;
    int updateTask, highestScore;
    private static PluginMain instance;
    private Map<Class<? extends GuiBuilder>, GuiBuilder> registeredMenus;
    private GuiManager guiManager;
    boolean isStarted = false;

    private Zombies zombies;
    private SpawnLocationManager spawnLocationManager;
    WeaponManager weaponManager = new WeaponManager();

    @Override
    public void onEnable() {
        instance = this;
        world = Bukkit.getWorld(getConfig().getString("locations.world"));
        spawn = SpawnLocationUtil.getLocationFromString(getConfig().getString("locations.spawn"), world);
        zombies = new Zombies();
        spawnLocationManager = new SpawnLocationManager();
        registeredMenus = new HashMap<>();
        guiManager = new GuiManager();
        guiManager.addMenu(new ShopGui());
        guiManager.addMenu(new UpgradeGui());
        guiManager.addMenu(new UpgradeGui());
        mobsNames = Arrays.asList(getConfig().getString("mobsNames").split(","));
        highestScore = getConfig().getInt("highestScore");
        System.out.println("\u001B[32m" +"ZombiePlugin est lance !" + "\u001B[0m");
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiManager(), this);
        getServer().getPluginManager().registerEvents(new GameSupervisor(), this);
        this.getCommand("start").setExecutor(new StartCommand());
        this.getCommand("shop").setExecutor(new ShopCommand());
        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("cheat").setExecutor(new CheatCommand());
        this.getCommand("setMoney").setExecutor(new setMoneyCommand());
        this.getCommand("upgrade").setExecutor(new UpgradeCommand());
        this.getCommand("gamestuck").setExecutor(new GameStuckCommand());
        this.manager = new ZombiePlayerManager();
        registerWeapons();
        for(Player player : Bukkit.getOnlinePlayers()){
            player.setHealth(20);
            player.setFoodLevel(20);

            player.teleport(spawn);
            manager.newZombiePlayer(player);
            ItemStack assaut = weaponManager.getWeaponByName("Fusil d'assaut").getItemStack();
            if (!player.getInventory().contains(assaut.getType())){
                setLore(assaut, weaponManager.getWeaponByName("Fusil d'assaut").getLore());
                player.getInventory().addItem(assaut);
            }
            if (player.getGameMode().equals(GameMode.SPECTATOR))player.setGameMode(GameMode.ADVENTURE);
        }
        for (Entity entity : Bukkit.getWorld(getConfig().getString("locations.world")).getEntities()){
            if (getMobsType().contains(entity.getType())){
                entity.remove();
            }
        }
        SpawnLocationUtil.createZone();
        tasks();
    }

    @Override
    public void onDisable() {

    }

    public void tasks() {
        updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player: Bukkit.getOnlinePlayers()) {
                    PluginListener.updateScoreBoard(player);
                }
            }
        }, 0, 1);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Bukkit.getWorld(getConfig().getString("locations.world")).getEntities()){
                    if (entity.getType().equals(EntityType.ARROW)){
                        entity.getWorld().spawnParticle(Particle.DRIP_WATER, entity.getLocation(), 4);
                    }
                }
            }
        },0,1);


    }

    public void registerWeapons(){
        weaponManager.registerWeapon(new ItemBuilder(Material.IRON_HOE).setName("Fusil d'assaut").toItemStack(), true, false, 1, 3, 1, 0, 10, 0, 5);
        weaponManager.registerWeapon(new ItemBuilder((Material.DIAMOND_SWORD)).setName("Cheat").toItemStack(), false, false, 1, 3, 0, 2, 40, 1, 10);
        weaponManager.registerWeapon(new ItemBuilder(Material.IRON_AXE).setName("Fusil a pompe").toItemStack(), true, false, 10, 1, 10, 1, 10, 0, 15);
        weaponManager.registerWeapon(new ItemBuilder(Material.IRON_PICKAXE).setName("Sniper").toItemStack(), false, false, 1, 3, 0, 3, 30, 0, 20);
        weaponManager.registerWeapon(new ItemBuilder(Material.IRON_SPADE).setName("Bazooka").toItemStack(), true, false, 1, 1, 2, 3, 0, 1, 30);

    }

    public ZombiePlayerManager getZombiePlayerManager() {
        return manager;
    }

    public boolean getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean started) {
        isStarted = started;
    }

    public static PluginMain getInstance() {
        return instance;
    }

    public Map<Class<? extends GuiBuilder>, GuiBuilder> getRegisteredMenus() {
        return registeredMenus;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public Zombies getZombies() {
        return zombies;
    }

    public List<EntityType> getMobsType() {
        return mobsType;
    }

    public SpawnLocationManager getSpawnLocationManager() {
        return spawnLocationManager;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public List<String> getMobsNames() {
        return mobsNames;
    }

    public int getHighestScore() {
        return highestScore;
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

    public static ItemStack setLore(ItemStack is, List<String> lore){
        if (!is.hasItemMeta()){
            return null;
        }
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
}
