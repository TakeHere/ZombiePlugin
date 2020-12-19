package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.commands.*;
import fr.takehere.zombieplugin.gui.ShopGui;
import fr.takehere.zombieplugin.util.GuiBuilder;
import fr.takehere.zombieplugin.util.GuiManager;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PluginMain extends JavaPlugin {
    ZombiePlayerManager manager;
    List<EntityType> mobsType = Arrays.asList(EntityType.ZOMBIE, EntityType.PIG_ZOMBIE);
    int updateTask;
    private static PluginMain instance;
    private Map<Class<? extends GuiBuilder>, GuiBuilder> registeredMenus;
    private GuiManager guiManager;

    boolean isStarted = false;
    @Override
    public void onEnable() {
        instance = this;
        registeredMenus = new HashMap<>();
        guiManager = new GuiManager();
        guiManager.addMenu(new ShopGui());
        System.out.println("\u001B[32m" +"ZombiePlugin est lance !" + "\u001B[0m");
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);
        getServer().getPluginManager().registerEvents(new WeaponListener(), this);
        getServer().getPluginManager().registerEvents(new GuiManager(), this);
        this.getCommand("start").setExecutor(new StartCommand());
        this.getCommand("shop").setExecutor(new ShopCommand());
        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("cheat").setExecutor(new CheatCommand());
        this.getCommand("setMoney").setExecutor(new setMoneyCommand());
        this.manager = new ZombiePlayerManager();
        Bukkit.getOnlinePlayers().forEach(player -> manager.newZombiePlayer(player));
        tasks();
    }

    @Override
    public void onDisable() {

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
    public void tasks() {
        updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player: Bukkit.getOnlinePlayers()) {
                    PluginListener.updateScoreBoard(player);
                }
            }
        }, 0, 2);
    }
    public Map<Class<? extends GuiBuilder>, GuiBuilder> getRegisteredMenus() {
        return registeredMenus;
    }
    public GuiManager getGuiManager() {
        return guiManager;
    }
}
