package dev.revere.alley.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.util.chat.CC;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:56
 */
public class KitViewCommand extends BaseCommand {
    @Command(name = "kit.view", permission = "alley.admin.view")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit view &b<kitName>"));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lKit " + kit.getName() +  " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        player.sendMessage(CC.translate(" &f● &bDisplay Name: &f" + kit.getDisplayName()));
        player.sendMessage(CC.translate(" &f● &bName: &f" + kit.getName()));
        player.sendMessage(CC.translate(" &f● &bIcon: &f" + kit.getIcon().name().toLowerCase() + " &7(" + kit.getIconData() + ")"));
        player.sendMessage(CC.translate(" &f● &bDisclaimer: &f" + kit.getDisclaimer()));
        player.sendMessage(CC.translate(" &f● &bDescription: &f" + kit.getDescription()));
        player.sendMessage("");
        player.spigot().sendMessage(sendClickable(kit));
        player.sendMessage("");
    }

    /**
     * Sends a clickable message to the player to view the kit settings
     *
     * @param kit the kit to view the settings of
     * @return the clickable message
     */
    private @NotNull TextComponent sendClickable(Kit kit) {
        TextComponent clickableMessage = new TextComponent(CC.translate("  &a(Click here to view the kit settings)"));
        clickableMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit viewsettings " + kit.getName()));
        String hover = CC.translate("&7Click to view the settings of the kit &b" + kit.getName());
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();
        clickableMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        return clickableMessage;
    }
}