package fr.takehere.zombieplugin.util;

import fr.takehere.zombieplugin.PluginMain;
import fr.takehere.zombieplugin.util.GuiBuilder;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GuiManager implements Listener {
    PluginMain main = PluginMain.getInstance();
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack current = event.getCurrentItem();

        if(event.getCurrentItem() == null) return;

        main.getRegisteredMenus().values().stream().filter(menu -> inv.getTitle().equalsIgnoreCase(menu.name())).forEach(menu -> {menu.onClick(player, inv, current, event.getSlot());event.setCancelled(true); });

    }

    public void addMenu(GuiBuilder m){
        main.getRegisteredMenus().put(m.getClass(), m);
    }

    public void open(Player player, Class<? extends GuiBuilder> gClass){

        if(!main.getRegisteredMenus().containsKey(gClass)) return;

        GuiBuilder menu = main.getRegisteredMenus().get(gClass);
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), menu.name());
        menu.contents(player, inv);

        new BukkitRunnable() {

            @Override
            public void run() {
                player.openInventory(inv);
            }

        }.runTaskLater(main, 1);

    }
}