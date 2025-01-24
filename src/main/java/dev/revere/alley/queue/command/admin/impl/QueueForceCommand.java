package dev.revere.alley.queue.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.hotbar.enums.HotbarType;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueForceCommand extends BaseCommand {
    @Command(name = "queue.force", aliases = {"forcequeue"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&6Usage: &e/queue force &b<player> <kit> <ranked>"));
            player.sendMessage(CC.translate("&7Example: /queue force hmRemi Boxing true"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        String kitType = args[1];
        boolean ranked = Boolean.parseBoolean(args[2]);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitType);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (queue.getKit().equals(kit) && queue.isRanked() == ranked) {
                queue.addPlayer(target, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
                PlayerUtil.reset(target, false);
                SoundUtil.playBanHammer(target);
                Alley.getInstance().getHotbarRepository().applyHotbarItems(target, HotbarType.QUEUE);
                player.sendMessage(CC.translate("&aYou've added &b" + target.getName() + " &ato the &b" + queue.getQueueType() + " &aqueue."));
                return;
            }
        }
    }
}
