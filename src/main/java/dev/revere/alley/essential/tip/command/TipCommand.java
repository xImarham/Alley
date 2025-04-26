package dev.revere.alley.essential.tip.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.essential.tip.Tip;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class TipCommand extends BaseCommand {
    private final Tip tip;

    public TipCommand() {
        this.tip = new Tip();
    }

    @CommandData(name = "tip", aliases = {"tips"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.tip.canGiveTip(player.getUniqueId())) {
            player.sendMessage(CC.translate(this.tip.getRandomTip()));
            this.tip.addPlayer(player.getUniqueId());
        } else {
            long lastTime = this.tip.getLastTipTime(player.getUniqueId());
            long currentTime = System.currentTimeMillis();
            long remainingTime = this.tip.COOLDOWN_TIME - (currentTime - lastTime);

            if (remainingTime > 0) {
                long remainingSeconds = remainingTime / 1000;
                long seconds = remainingSeconds % 60;

                player.sendMessage(CC.translate(
                    "&cYou need to wait " + seconds + "s before you can get another tip."
                ));
            }
        }
    }
}