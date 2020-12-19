package fr.takehere.zombieplugin.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public interface GuiBuilder {
    public abstract String name();
    public abstract int getSize();
    public abstract void contents(Player player, Inventory inv);
    public abstract void onClick(Player player, Inventory inv, ItemStack current, int slot);
}