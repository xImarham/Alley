package dev.revere.alley.game.bot;

import dev.revere.alley.game.bot.enums.EnumBotPreset;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@Getter
public class Bot {
    private final EnumBotPreset preset;
    private final NPC npc;

    /**
     * Constructor for the Bot class.
     *
     * @param preset        The preset for the bot.
     * @param spawnLocation The location to spawn the bot.
     * @param name          The name of the bot.
     */
    public Bot(EnumBotPreset preset, Location spawnLocation, String name) {
        this.preset = preset;
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        this.npc.setProtected(false);
        this.npc.setName(name);
        this.npc.spawn(spawnLocation);
        //this.npc.data().setPersistent("hmEmmy", true);
    }

    public void destroy() {
        if (this.npc.isSpawned()) {
            this.npc.despawn();
        }
        this.npc.destroy();
    }
}