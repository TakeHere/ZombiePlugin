package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.util.CooldownManager;
import fr.takehere.zombieplugin.util.ItemBuilder;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayer;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class PluginListener implements Listener {
    private PluginMain main;
    public HashMap<String, Long> cooldowns = new HashMap<>();
    private final CooldownManager cooldownManager = new CooldownManager();

    public PluginListener(PluginMain pluginMain) {
        this.main = pluginMain;
    }

    public static void updateScoreBoard(Player player) {
        ZombiePlayer zombiePlayer = PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player);
        Scoreboard board = player.getScoreboard();
        board.getTeam("kills2").setPrefix("§f" + zombiePlayer.getKill());
        board.getTeam("money2").setPrefix("§f" + zombiePlayer.getMoney());
        player.setScoreboard(board);
    }
    public static void makeScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        ZombiePlayer zombiePlayer = PluginMain.getInstance().getZombiePlayerManager().getZombiePlayer(player);
        Objective obj = board.registerNewObjective("  §6§lZOMBIES   ", "dummy");
        //Kills
        Team kills = board.registerNewTeam("kills");
        kills.addEntry(ChatColor.DARK_RED + "" + ChatColor.DARK_RED);
        kills.setPrefix("§eKill(s):");
        obj.getScore(ChatColor.DARK_RED + "" + ChatColor.DARK_RED).setScore(4);

        //Kills2
        Team kills2 = board.registerNewTeam("kills2");
        kills2.addEntry(ChatColor.DARK_PURPLE + "" + ChatColor.DARK_PURPLE);
        kills2.setPrefix("§f" + zombiePlayer.getKill());
        obj.getScore(ChatColor.DARK_PURPLE + "" + ChatColor.DARK_PURPLE).setScore(3);

        //Money
        Team money = board.registerNewTeam("money");
        money.addEntry(ChatColor.GRAY + "" + ChatColor.GRAY);
        money.setPrefix("§eArgent:");
        obj.getScore(ChatColor.GRAY + "" + ChatColor.GRAY).setScore(2);

        //Money2
        Team money2 = board.registerNewTeam("money2");
        money2.addEntry(ChatColor.RED + "" + ChatColor.RED);
        money2.setPrefix("§f" + zombiePlayer.getMoney());
        obj.getScore(ChatColor.RED + "" + ChatColor.RED).setScore(1);

        //Null (Vide)
        Team vide = board.registerNewTeam("vide");
        vide.addEntry(ChatColor.GREEN + "" + ChatColor.GREEN);
        vide.setPrefix("  ");
        obj.getScore(ChatColor.GREEN + "" + ChatColor.GREEN).setScore(5);

        //Line (line)
        Team line = board.registerNewTeam("line");
        line.addEntry(ChatColor.BLUE + "" + ChatColor.BLUE);
        line.setPrefix("§c§m------------");
        obj.getScore(ChatColor.BLUE + "" + ChatColor.BLUE).setScore(6);

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
    }
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        if(main.getIsStarted()) {
            e.disallow(e.getResult().KICK_OTHER, ChatColor.RED + "Le jeu a déjà commencé !");
            return;
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.setJoinMessage(ChatColor.GOLD + "Bienvenue a " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " sur le serveur !");
        //
        main.getZombiePlayerManager().newZombiePlayer(player);
        makeScoreboard(player);
        updateScoreBoard(player);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " a quitté la partie");
        //
        main.getZombiePlayerManager().removeZombiePlayer(main.getZombiePlayerManager().getZombiePlayer(player));
    }
    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){
        if (e.getEntity() != null && e.getEntity().getKiller() != null){
            if (main.mobsType.contains(e.getEntity().getType()) && e.getEntity().getKiller().getType().equals(EntityType.PLAYER)){
                ZombiePlayer zombiePlayer = main.getZombiePlayerManager().getZombiePlayer(e.getEntity().getKiller());
                int moneyToAdd = main.getConfig().getInt("moneyPerKills");
                zombiePlayer.addMoney(moneyToAdd);
                e.getEntity().getKiller().sendMessage(ChatColor.GOLD + "Vous venez de gagner: " + ChatColor.GREEN + moneyToAdd + "$" + "§6 (§2" + zombiePlayer.getMoney() + "$§6)");
                zombiePlayer.incrementKill();
                return;
            }
        }
        return;
    }
}
