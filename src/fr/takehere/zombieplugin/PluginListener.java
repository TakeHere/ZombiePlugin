package fr.takehere.zombieplugin;

import fr.takehere.zombieplugin.locations.SpawnLocationUtil;
import fr.takehere.zombieplugin.util.CooldownManager;
import fr.takehere.zombieplugin.util.ItemBuilder;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayer;
import fr.takehere.zombieplugin.zombieplayer.ZombiePlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        board.getTeam("vague").setPrefix("§eVague: " + ChatColor.WHITE + PluginMain.getInstance().getZombies().getWave());
        board.getTeam("zombiesRestants").setPrefix("§eZombies: ");
        board.getTeam("zombiesRestants").setSuffix(ChatColor.WHITE + String.valueOf(PluginMain.getInstance().getZombies().getTotalZombiesSpawned()));
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
        obj.getScore(ChatColor.DARK_RED + "" + ChatColor.DARK_RED).setScore(6);

        //Kills2
        Team kills2 = board.registerNewTeam("kills2");
        kills2.addEntry(ChatColor.DARK_PURPLE + "" + ChatColor.DARK_PURPLE);
        kills2.setPrefix("§f" + zombiePlayer.getKill());
        obj.getScore(ChatColor.DARK_PURPLE + "" + ChatColor.DARK_PURPLE).setScore(5);

        //Money
        Team money = board.registerNewTeam("money");
        money.addEntry(ChatColor.GRAY + "" + ChatColor.GRAY);
        money.setPrefix("§eArgent:");
        obj.getScore(ChatColor.GRAY + "" + ChatColor.GRAY).setScore(4);

        //Money2
        Team money2 = board.registerNewTeam("money2");
        money2.addEntry(ChatColor.RED + "" + ChatColor.RED);
        money2.setPrefix("§f" + zombiePlayer.getMoney());
        obj.getScore(ChatColor.RED + "" + ChatColor.RED).setScore(3);

        //Null (Vide)
        Team vide = board.registerNewTeam("vide");
        vide.addEntry(ChatColor.GREEN + "" + ChatColor.GREEN);
        vide.setPrefix("  ");
        obj.getScore(ChatColor.GREEN + "" + ChatColor.GREEN).setScore(7);

        //Null2 (Vide2)
        Team vide2 = board.registerNewTeam("vide2");
        vide2.addEntry(ChatColor.GOLD + "" + ChatColor.GREEN);
        vide2.setPrefix("  ");
        obj.getScore(ChatColor.GOLD + "" + ChatColor.GREEN).setScore(2);

        //Line (line)
        Team line = board.registerNewTeam("line");
        line.addEntry(ChatColor.BLUE + "" + ChatColor.BLUE);
        line.setPrefix("§c§m------------");
        obj.getScore(ChatColor.BLUE + "" + ChatColor.BLUE).setScore(10);

        //Vague
        Team vague = board.registerNewTeam("vague");
        vague.addEntry(ChatColor.BLUE + "" + ChatColor.RED);
        vague.setPrefix("§eVague: " + ChatColor.WHITE + PluginMain.getInstance().getZombies().getWave());
        obj.getScore(ChatColor.BLUE + "" + ChatColor.RED).setScore(9);

        //HighestScore
        Team bestScore = board.registerNewTeam("bestScore");
        bestScore.addEntry(ChatColor.DARK_RED + "" + ChatColor.RED);
        bestScore.setPrefix("§eBestScore: ");
        bestScore.setSuffix(ChatColor.WHITE + String.valueOf(PluginMain.getInstance().getHighestScore()));
        obj.getScore(ChatColor.DARK_RED + "" + ChatColor.RED).setScore(1);

        //ZombiesRestants
        Team zombiesRestants = board.registerNewTeam("zombiesRestants");
        zombiesRestants.addEntry(ChatColor.BLUE + "" + ChatColor.GRAY);
        zombiesRestants.setPrefix("§eZombies: ");
        zombiesRestants.setSuffix(ChatColor.WHITE + String.valueOf(PluginMain.getInstance().getZombies().getTotalZombiesSpawned()));
        obj.getScore(ChatColor.BLUE + "" + ChatColor.GRAY).setScore(8);

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);

        e.setJoinMessage(ChatColor.GOLD + "Bienvenue a " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " sur le serveur !");
        ItemStack assaut = PluginMain.getInstance().getWeaponManager().getWeaponByName("Fusil d'assaut").getItemStack();
        if (!player.getInventory().contains(assaut.getType())){
            PluginMain.setLore(assaut, PluginMain.getInstance().getWeaponManager().getWeaponByName("Fusil d'assaut").getLore());
            player.getInventory().addItem(assaut);
        }

        if (PluginMain.getInstance().getIsStarted()){
            player.setGameMode(GameMode.SPECTATOR);
        }else player.setGameMode(GameMode.ADVENTURE);
        player.teleport(main.spawn);
        if (!main.getZombiePlayerManager().doesPlayerExist(player)){
            main.getZombiePlayerManager().newZombiePlayer(player);
        }
        makeScoreboard(player);
        updateScoreBoard(player);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        e.setQuitMessage(ChatColor.YELLOW + e.getPlayer().getName() + ChatColor.GOLD + " a quitté la partie");
    }
    @EventHandler
    public void onEntityKilled(EntityDeathEvent e){
        if (e.getEntity() != null && e.getEntity().getKiller() != null){
            if (main.getMobsType().contains(e.getEntity().getType()) && e.getEntity().getKiller().getType().equals(EntityType.PLAYER)){
                ZombiePlayer zombiePlayer = main.getZombiePlayerManager().getZombiePlayer(e.getEntity().getKiller());
                int moneyToAdd = main.getConfig().getInt("moneyPerKills");
                zombiePlayer.addMoney(moneyToAdd);
                //e.getEntity().getKiller().sendMessage(ChatColor.GOLD + "Vous venez de gagner: " + ChatColor.GREEN + moneyToAdd + "$" + "§6 (§2" + zombiePlayer.getMoney() + "$§6)");
                e.getEntity().getKiller().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "Vous venez de gagner: " + ChatColor.GREEN + moneyToAdd + "$" + "§6 (§2" + zombiePlayer.getMoney() + "$§6)"));
                zombiePlayer.incrementKill();
                return;
            }
        }
        return;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        e.setDeathMessage(ChatColor.RED + e.getEntity().getDisplayName() + " n'a pas fait long feu...");
        if (PluginMain.getInstance().getIsStarted()) e.getEntity().setGameMode(GameMode.SPECTATOR);
        int nbPlayersSpectator = 0;
        for (Player player:Bukkit.getOnlinePlayers()){
            if (player.getGameMode().equals(GameMode.SPECTATOR))nbPlayersSpectator++;
        }
        System.out.println(nbPlayersSpectator);
        if (nbPlayersSpectator == Bukkit.getOnlinePlayers().size()){
            Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.RED + "Fin de la partie", "Personne n'a survécu..."));
            if (main.getHighestScore() < main.getZombies().getWave()) PluginMain.getInstance().getConfig().set("highestScore", PluginMain.getInstance().getZombies().getWave());
            main.saveConfig();
            Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getServer().shutdown();
                }
            },20*5);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        ItemStack assaut = PluginMain.getInstance().getWeaponManager().getWeaponByName("Fusil d'assaut").getItemStack();
        if (!e.getPlayer().getInventory().contains(assaut)) e.getPlayer().getInventory().addItem(assaut);
        e.getPlayer().teleport(main.spawn);
        e.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void onLoot(EntityDeathEvent e){
        e.getDrops().clear();
        e.setDroppedExp(0);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e){
        if (PluginMain.getInstance().getMobsType().contains(e.getEntity().getType())){
            PluginMain.getInstance().getZombies().incrementTotalZombiesSpawned();
        }
    }
}
