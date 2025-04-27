package dev.revere.alley.feature.queue.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.game.bot.enums.EnumBotPreset;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@AllArgsConstructor
public class BotButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(kit.getIcon())
                .name(kit.getName())
                //TODO: This is just temporary, we're going to make this dynamic for all type of bots, not only a statically defined one. FOR TESTING PURPOSES ONLY.
                .lore(
                        "&7Click to fight a bot!",
                        "",
                        "&7Difficulty: &b" + EnumBotPreset.EASY.getName(),
                        "&7Kit: &b" + kit.getName(),
                        "",
                        "&7Click to start the fight!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        AbstractArena arena = Alley.getInstance().getArenaService().getRandomStandAloneArena();
        if (arena == null) {
            player.sendMessage("No arena available.");
            return;
        }

        Alley.getInstance().getBotFightRepository().createMatch(player, arena, this.kit);
    }
}