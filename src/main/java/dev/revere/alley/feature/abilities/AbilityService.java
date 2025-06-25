package dev.revere.alley.feature.abilities;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.abilities.impl.*;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.TaskUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

@Getter
public class AbilityService {

    private final GuardianAngel guardianAngel;

    private final Combo combo;
    private final EffectDisabler effectDisabler;
    private final NinjaStar ninjaStar;
    private final PocketBard pocketBard;
    private final AntiTrapper antitrapper;

    private final Scrambler scrambler;
    private final Strength strength;
    private final SwapperAxe swapperAxe;
    private final Switcher switcher;

    private final TankIngot tankIngot;
    private final Cookie cookie;
    private final Rocket rocket;
    private final TimeWarp timeWarp;
    private final LuckyIngot luckyIngot;

    public AbilityService() {
        this.guardianAngel = new GuardianAngel();
        this.combo = new Combo();
        this.antitrapper = new AntiTrapper();
        this.effectDisabler = new EffectDisabler();
        this.ninjaStar = new NinjaStar();
        this.pocketBard = new PocketBard();
        this.scrambler = new Scrambler();
        this.strength = new Strength();
        this.swapperAxe = new SwapperAxe();
        this.switcher = new Switcher();
        this.tankIngot = new TankIngot();
        this.rocket = new Rocket();
        this.cookie = new Cookie();
        this.timeWarp = new TimeWarp();
        this.luckyIngot = new LuckyIngot();

        AbstractAbility.getAbilities().forEach(AbstractAbility::register);
    }

    public ItemStack getAbility(String ability, int amount) {
        return new ItemBuilder(getMaterial(ability))
                .amount(amount)
                .durability(getData(ability))
                .name(getDisplayName(ability))
                .lore(getDescription(ability))
                .build();
    }

    public String getDisplayName(String ability) {
        return Alley.getInstance().getConfigService().getAbilityConfig().getString( ability + ".ICON.DISPLAYNAME");
    }

    public List<String> getDescription(String ability) {
        return Alley.getInstance().getConfigService().getAbilityConfig().getStringList( ability + ".ICON.DESCRIPTION");
    }

    public Material getMaterial(String ability) {
        return Material.valueOf(Alley.getInstance().getConfigService().getAbilityConfig().getString(ability + ".ICON.MATERIAL"));
    }

    public int getData(String ability) {
        return Alley.getInstance().getConfigService().getAbilityConfig().getInt(ability + ".ICON.DATA");
    }

    public int getCooldown(String ability) {
        return Alley.getInstance().getConfigService().getAbilityConfig().getInt(ability + ".COOLDOWN");
    }

    public Set<String> getAbilities() {
        return Alley.getInstance().getConfigService().getAbilityConfig().getConfigurationSection("").getKeys(false);
    }

    public void giveAbility(CommandSender sender, Player player, String key, String abilityName, int amount) {
        player.getInventory().addItem(this.getAbility(key, amount));
        if (player == sender) {
            player.sendMessage(CC.translate(Alley.getInstance().getConfigService().getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))));
        }
        else {
            player.sendMessage(CC.translate(Alley.getInstance().getConfigService().getAbilityConfig().getString("RECEIVED_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))));
            sender.sendMessage(CC.translate(Alley.getInstance().getConfigService().getAbilityConfig().getString("GIVE_ABILITY")
                    .replace("%ABILITY%", abilityName)
                    .replace("%AMOUNT%", String.valueOf(amount))
                    .replace("%PLAYER%", player.getName())));
        }
    }
    public void playerMessage(Player player, String ability) {
        String displayName = getDisplayName(ability);
        String cooldown = String.valueOf(getCooldown(ability));

        Alley.getInstance().getConfigService().getAbilityConfig().getStringList(ability + ".MESSAGE.PLAYER").forEach(
                message -> CC.message(player, message
                        .replace("%ABILITY%", displayName)
                        .replace("%COOLDOWN%", cooldown)));
    }

    public void targetMessage(Player target, Player player, String ability) {
        String displayName = getDisplayName(ability);

        Alley.getInstance().getConfigService().getAbilityConfig().getStringList(ability + ".MESSAGE.TARGET").forEach(
                message -> CC.message(target, message
                        .replace("%ABILITY%", displayName)
                        .replace("%PLAYER%", player.getName())
                        .replace("%TARGET%", target.getName())));
    }

    public void cooldown(Player player, String abilityName, String cooldown) {
        CC.message(player, Alley.getInstance().getConfigService().getAbilityConfig().getString("STILL_ON_COOLDOWN")
                .replace("%ABILITY%", abilityName)
                .replace("%COOLDOWN%", cooldown));
    }


    public void cooldownExpired(Player player, String abilityName, String ability) {
        TaskUtil.runLaterAsync(() ->
                CC.message(player, Alley.getInstance().getConfigService().getAbilityConfig().getString("COOLDOWN_EXPIRED")
                        .replace("%ABILITY%", abilityName)), getCooldown(ability) * 20L);
    }
}
