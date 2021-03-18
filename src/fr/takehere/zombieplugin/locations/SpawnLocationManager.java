package fr.takehere.zombieplugin.locations;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class SpawnLocationManager {
    private final List<SpawnLocation> spawnLocations;
    public SpawnLocationManager(){
        this.spawnLocations = new ArrayList<>();
    }

    public List<SpawnLocation> getSpawnLocations() {
        return spawnLocations;
    }

    public void registerLocation(List<Location> locations, String name, boolean enabled){
        SpawnLocation spawnLocation = new SpawnLocation(locations, enabled, name);
        spawnLocations.add(spawnLocation);
    }

    public SpawnLocation getSpawnLocationByName(String name){
        for (SpawnLocation spawnLocation : spawnLocations){
            if (spawnLocation.getName().equalsIgnoreCase(name)){
                return spawnLocation;
            }
        }
        throw new NullPointerException("Aucun spawn location trouvé avec: " + name);
    }
}
