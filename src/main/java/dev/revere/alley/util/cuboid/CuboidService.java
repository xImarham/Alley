package dev.revere.alley.util.cuboid;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
@Getter
public abstract class CuboidService {

    public CuboidService() {
        this.loadCuboid();
    }

    public abstract void loadCuboid();
    public abstract void updateCuboid();
}