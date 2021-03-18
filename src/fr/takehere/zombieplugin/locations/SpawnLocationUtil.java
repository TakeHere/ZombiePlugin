package fr.takehere.zombieplugin.locations;

import fr.takehere.zombieplugin.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpawnLocationUtil {
    SpawnLocation spawnLocation;
    public static void createZone(){
        FileConfiguration config = PluginMain.getInstance().getConfig();
        World world = Bukkit.getWorld(config.getString("locations.world"));
        allocateZone(getLocationFromString(config.getString("locations.start1"), world), "start1", true);
        allocateZone(getLocationFromString(config.getString("locations.parc"), world), "parc", true);
        allocateZone(getLocationFromString(config.getString("locations.start2"), world), "start2", true);
        allocateZone(getLocationFromString(config.getString("locations.tunnel"), world), "tunnel", true);
    }

    public static void allocateZone(Location loc, String name, Boolean enabled){
        World world = loc.getWorld();
        List<Location> locs = new ArrayList<>();
        locs.add(loc);
        locs.add(new Location(world, loc.getX()+0, loc.getY()+0, loc.getZ()+0.5));
        locs.add(new Location(world, loc.getX()+0, loc.getY()+0, loc.getZ()-0.5));
        locs.add(new Location(world, loc.getX()+0.5, loc.getY()+0, loc.getZ()+0));
        locs.add(new Location(world, loc.getX()-0.5, loc.getY()+0, loc.getZ()+0));
        locs.add(new Location(world, loc.getX()+0.5, loc.getY()+0, loc.getZ()+0.5));
        locs.add(new Location(world, loc.getX()-0.5, loc.getY()+0, loc.getZ()-0.5));
        locs.add(new Location(world, loc.getX()+0.5, loc.getY()+0, loc.getZ()-0.5));
        locs.add(new Location(world, loc.getX()-0.5, loc.getY()+0, loc.getZ()+0.5));
        PluginMain.getInstance().getSpawnLocationManager().registerLocation(locs, name, enabled);
    }
    public static Location getLocationFromString(String str, World world){
        String[] coordinates = str.split(",");
        return new Location(world, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));
    }
}
