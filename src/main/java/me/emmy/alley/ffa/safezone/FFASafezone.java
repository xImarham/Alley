package me.emmy.alley.ffa.safezone;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.ArenaType;
import org.bukkit.Location;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 14:38
 */

@Getter
@Setter
public class FFASafezone {
    private ArenaType arenaType = ArenaType.FFA;
    private Location safezoneMin;
    private Location safezoneMax;

    FFASafezone(ArenaType arenaType, Location safezoneMin, Location safezoneMax) {
        this.arenaType = arenaType;

        this.safezoneMin = safezoneMin;
        this.safezoneMax = safezoneMax;
    }

    public void leaveSafezone() {

    }

    public void teleportToSafezone() {

    }

    public void enterSafezone() {

    }
}
