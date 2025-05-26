package dev.revere.alley.base.kit.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class KitSetupFFACommand extends BaseCommand {
    @CommandData(name = "kit.setupffa", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 4) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setfupfa &b<kitName> <arenaName> <maxPlayers> <menu-slot>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        if (kit.isFfaEnabled()) {
            player.sendMessage(CC.translate("&cThis kit is already set up for FFA!"));
            return;
        }

        AbstractArena arena = this.plugin.getArenaService().getArenaByName(args[1]);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arena.getType() != EnumArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou can only set up FFA matches in FFA arenas!"));
            return;
        }

        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe max players must be a number."));
            return;
        }

        int menuSlot;
        try {
            menuSlot = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe menu slot must be a number."));
            return;
        }
        
        /* TODO: Verify which settings are enabled for the kit to prevent conflicting settings, 
            such as setting Boxing as FFA. Afterwards too, if (/kit setsetting) */


        kit.setFfaEnabled(true);
        kit.setFfaSlot(menuSlot);
        kit.setFfaArenaName(arena.getName());
        kit.setMaxFfaPlayers(maxPlayers);
        this.plugin.getFfaService().createFFAMatch(arena, kit, maxPlayers);
        kitService.saveKit(kit);
        this.plugin.getProfileService().loadProfiles();
        player.sendMessage(CC.translate("&aFFA match has been created with the kit &b" + kit.getName() + "&a!"));
    }
}
