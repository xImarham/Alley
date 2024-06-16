package me.emmy.alley.match.snapshot.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.match.snapshot.Snapshot;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.button.CommandButton;
import me.emmy.alley.utils.menu.button.DisplayButton;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

@AllArgsConstructor
public class InventorySnapshotMenu extends Menu {

    private final Player target;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&e" + target.getName() + "'s Inventory");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (player == null) {
            return buttons;
        }

        Snapshot snapshot = Alley.getInstance().getSnapshotRepository().getSnapshot(target.getUniqueId());

        if (snapshot == null) {
            return buttons;
        }

        ItemStack[] fixedContents = snapshot.getInventory();
        for (int i = 0; i < fixedContents.length; ++i) {
            ItemStack itemStack = fixedContents[i];

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                buttons.put(i, new DisplayButton(itemStack, true));
            }
        }

        ItemStack[] armorContents = snapshot.getArmor();
        for (int i = 0; i < armorContents.length; ++i) {
            ItemStack itemStack = armorContents[i];

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                buttons.put(39 - i, new DisplayButton(itemStack, true));
            }
        }

        int pos = 45;
        buttons.put(pos++, new HealthButton(snapshot.getHealth() == 0.0 ? 0 : (int) Math.round(snapshot.getHealth() / 2.0)));
        buttons.put(pos++, new HungerButton(snapshot.getFoodLevel()));
        buttons.put(pos, new EffectsButton(target.getActivePotionEffects()));

        buttons.put(53, new CommandButton("inventory " + Bukkit.getPlayer(snapshot.getOpponent()), new ItemStack(Material.BOOK), Arrays.asList(
                "",
                "&eClick here to view " + snapshot.getOpponent() + "'s inventory.",
                ""
        )));

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @AllArgsConstructor
    private static class HealthButton extends Button {

        private int health;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.MELON)
                    .name("&eHealth: &d" + health + "/10 " + StringEscapeUtils.unescapeJava("\u2764"))
                    .amount(health == 0 ? 1 : health)
                    .build();
        }

    }

    @AllArgsConstructor
    private static class HungerButton extends Button {

        private int hunger;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.COOKED_BEEF)
                    .name("&eHunger: &d" + hunger + "/20")
                    .build();
        }

    }

    @AllArgsConstructor
    private static class EffectsButton extends Button {

        private Collection<PotionEffect> effects;

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(Material.POTION).name("&ePotion Effects");

            if (effects.isEmpty()) {
                builder.lore(CC.translate("&cNo effects"));
            } else {
                List<String> lore = new ArrayList<>();

                effects.forEach(effect -> {
                    String name = effect.getType().getName() + " " + (effect.getAmplifier() + 1);
                    String duration = " (" + effect.getDuration() / 20 + "s)";
                    lore.add(CC.translate(name + duration));
                });

                builder.lore(lore);
            }

            return builder.build();
        }

    }

}
