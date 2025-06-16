package dev.revere.alley.game.party.menu.event.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.party.menu.event.impl.button.PartyEventSplitArenaSelectorButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2024 - 09:37
 */
@AllArgsConstructor
public class PartyEventSplitArenaSelectorMenu extends Menu {
    private Kit kit;

    @Override
    public String getTitle(Player player) {
        return "&b&lSelect an arena";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (AbstractArena arena : Alley.getInstance().getArenaService().getArenas()) {
            if (arena.getKits().contains(kit.getName()) && arena.isEnabled() &&
                    (!(arena instanceof StandAloneArena) || !((StandAloneArena) arena).isActive())) {
                buttons.put(buttons.size(), new PartyEventSplitArenaSelectorButton(kit, arena));
            }
        }

        return buttons;
    }
}