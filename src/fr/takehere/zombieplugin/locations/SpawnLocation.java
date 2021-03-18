package fr.takehere.zombieplugin.locations;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnLocation {
    private List<Location> locations;
    private boolean enabled;
    private final String name;

    //not a good idea
    public SpawnLocation(List<Location> locations, boolean enabled, String name) {
        this.locations = locations;
        this.enabled = enabled;
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void addLocation(Location location){
        this.locations.add(location);
    }

    public void removeLocation(Location location){
        this.locations.remove(location);
    }

    public Location getRandomLocation(){
        return this.locations.get((new Random().nextInt(this.locations.size())));
    }

    public String getName() {
        return name;
    }
}
