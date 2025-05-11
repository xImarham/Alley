package dev.revere.alley.feature.title.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.tier.DivisionTier;
import dev.revere.alley.feature.title.record.TitleRecord;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.visual.ProgressBarUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@AllArgsConstructor
public class TitleButton extends Button {
    private final Profile profile;
    private final TitleRecord title;

    @Override
    public ItemStack getButtonItem(Player player) {

        if (!this.profile.getProfileData().getUnlockedTitles().contains(this.title.getKit().getName())) {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name("&b&l" + this.title.getKit().getName())
                    .lore(
                            this.progress()
                    )
                    .durability(14)
                    .hideMeta()
                    .build();
        }

        return new ItemBuilder(this.title.getKit().getIcon())
                .name("&b&l" + this.title.getKit().getName())
                .lore(
                        "",
                        "&a&lUNLOCKED",
                        "",
                        "&aClick to select!"
                )
                .durability(this.title.getKit().getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!this.profile.getProfileData().getUnlockedTitles().contains(this.title.getKit().getName())) {
            player.sendMessage(CC.translate("&cTo select this title, you need to unlock it first."));
            return;
        }

        if (this.profile.getProfileData().getSelectedTitle().equals(this.title.getKit().getName())) {
            player.sendMessage(CC.translate("&cYou already have this title selected."));
            return;
        }

        this.profile.getProfileData().setSelectedTitle(this.title.getKit().getName());
        player.sendMessage(CC.translate("&aYou have selected the &b" + this.title.getKit().getName() + " &atitle."));
    }

    /**
     * Get the progress of the title.
     *
     * @return A list of strings representing the progress.
     */
    private List<String> progress() {
        ProfileData profileData = this.profile.getProfileData();
        int wins = profileData.getUnrankedKitData().get(this.title.getKit().getName()).getWins();

        Division currentDivision = profileData.getUnrankedKitData().get(this.title.getKit().getName()).getDivision();
        DivisionTier currentTier = profileData.getUnrankedKitData().get(this.title.getKit().getName()).getTier();

        List<DivisionTier> tiers = currentDivision.getTiers();
        int tierIndex = tiers.indexOf(currentTier);

        int nextTierWins;
        if (tierIndex < tiers.size() - 1) {
            nextTierWins = tiers.get(tierIndex + 1).getRequiredWins();
        } else if (this.profile.getNextDivision(this.title.getKit().getName()) != null) {
            nextTierWins = this.profile.getNextDivision(this.title.getKit().getName()).getTiers().get(0).getRequiredWins();
        } else {
            nextTierWins = currentTier.getRequiredWins();
        }

        String nextRank = this.profile.getNextDivisionAndTier(this.title.getKit().getName());
        String progressBar = ProgressBarUtil.generate(wins, nextTierWins, 12, "■");
        String progressPercent = nextTierWins > 0 ? Math.round((float) wins / nextTierWins * 100) + "%" : "100%";
        int requiredWinsToUnlock = nextTierWins - wins;
        String winOrWins = requiredWinsToUnlock == 1 ? "win" : "wins";

        return Arrays.asList(
                "",
                "&c&lLOCKED",
                "",
                "&fUnlock &b" + nextRank + " &fwith " + requiredWinsToUnlock + " more " + winOrWins + ".",
                "&f" + progressBar + " &7" + progressPercent,
                "",
                "&fRequires &c&l" + this.title.getRequiredDivision().getName().toUpperCase() + " &fdivision."
        );
    }
}