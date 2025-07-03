package dev.revere.alley.base.queue.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.hotbar.IHotbarService;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.IQueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueForceCommand extends BaseCommand {
    @CommandData(name = "queue.force", aliases = {"forcequeue"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&6Usage: &e/queue force &6<player> <kit> <ranked>"));
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

        Kit kit = Alley.getInstance().getService(IKitService.class).getKit(kitType);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(target.getUniqueId());
        for (Queue queue : Alley.getInstance().getService(IQueueService.class).getQueues()) {
            if (queue.getKit().equals(kit) && queue.isRanked() == ranked) {
                queue.addPlayer(target, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
                PlayerUtil.reset(target, false);
                SoundUtil.playBanHammer(target);
                Alley.getInstance().getService(IHotbarService.class).applyHotbarItems(target);
                player.sendMessage(CC.translate("&aYou've added &6" + target.getName() + " &ato the &6" + queue.getQueueType() + " &aqueue."));

                if (ranked && profile.getProfileData().isRankedBanned()) {
                    player.sendMessage(CC.translate("&cKeep in mind that &6" + target.getName() + " &cis currently banned from ranked queues!"));
                }

                return;
            }
        }
    }
}
