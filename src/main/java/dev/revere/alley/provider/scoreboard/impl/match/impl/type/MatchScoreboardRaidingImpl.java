package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaidingImpl;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.AbstractMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.IMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(kit = KitSettingRaidingImpl.class)
public class MatchScoreboardRaidingImpl extends AbstractMatchScoreboard {
    public MatchScoreboardRaidingImpl(Alley plugin) {
        super(plugin);
    }

    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.raiding-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.raiding-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);

        return baseLine
                .replace("{role}", you.getPlayer().getData().getRole().getDisplayName());
    }
}
