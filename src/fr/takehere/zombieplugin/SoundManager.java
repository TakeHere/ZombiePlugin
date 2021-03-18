package fr.takehere.zombieplugin;

import org.bukkit.Location;
import org.bukkit.Sound;

public class SoundManager {
    public SoundManager() {

    }
    public void hit(Location location){
        location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 0.8f);
    }

    public void shoot(Location location){
        location.getWorld().playSound(location, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 0.3f, 1f);
    }

    public void waveFinished(Location location){
        location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 0.8f);
    }

    public void alert(Location location){
        location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.2f);
    }
}
