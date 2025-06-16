package dev.revere.alley.game.party.menu.event.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.party.menu.event.impl.button.PartyEventSplitButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:38
 */
public class PartyEventSplitMenu extends Menu {
    protected final Alley plugin = Alley.getInstance();

    @Override
    public String getTitle(Player player) {
        return "&c&lStill in development";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        this.plugin.getKitService().getKits()
                .stream()
                .filter(Kit::isEnabled)
                .forEach(kit -> buttons.put(buttons.size(), new PartyEventSplitButton(kit)))
        ;

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}