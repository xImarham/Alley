package dev.revere.alley.game.party.command.impl.donator;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.enums.EnumPartyState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 11:16
 */
public class PartyAnnounceCommand extends BaseCommand {
    @CommandData(name = "party.announce", aliases = {"p.announce"}, permission = "alley.donator.party.announce")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        if (!profile.getState().equals(EnumProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to announce to your party."));
            return;
        }

        if (profile.getParty().getState() != EnumPartyState.PUBLIC) {
            player.sendMessage(CC.translate("&cYour party is not open to the public to announce. Please run the following command: &7/party open"));
            return;
        }

        CooldownRepository cooldownRepository = this.plugin.getCooldownRepository();
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), EnumCooldownType.PARTY_ANNOUNCE_COOLDOWN));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTimeInMinutes() + " &cbefore announcing your party again."));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(EnumCooldownType.PARTY_ANNOUNCE_COOLDOWN, () -> player.sendMessage(CC.translate("&aYour party announce cooldown has expired.")));
            cooldownRepository.addCooldown(player.getUniqueId(), EnumCooldownType.PARTY_ANNOUNCE_COOLDOWN, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();

        this.plugin.getPartyService().announceParty(profile.getParty());
    }
}