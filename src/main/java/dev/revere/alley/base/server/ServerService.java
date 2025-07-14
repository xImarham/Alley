package dev.revere.alley.base.server;

import dev.revere.alley.base.hotbar.IHotbarService;
import dev.revere.alley.base.spawn.ISpawnService;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.IMatchService;
import dev.revere.alley.game.party.IPartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
@Service(provides = IServerService.class, priority = 300)
public class ServerService implements IServerService {
    private final IMatchService matchService;
    private final IPartyService partyService;
    private final IProfileService profileService;
    private final IHotbarService hotbarService;
    private final ISpawnService spawnService;
    private final IConfigService configService;

    private boolean queueingAllowed;

    private List<Material> blockedCraftingMaterials;
    private final String BLOCKED_MATERIALS_SECTION = "blocked-crafting-items";

    /**
     * Constructor for dependency injection.
     *
     * @param matchService   The match service.
     * @param partyService   The party service.
     * @param profileService The profile service.
     * @param hotbarService  The hotbar service.
     * @param spawnService   The spawn service.
     * @param configService  The config service.
     */
    public ServerService(IMatchService matchService, IPartyService partyService, IProfileService profileService, IHotbarService hotbarService, ISpawnService spawnService, IConfigService configService) {
        this.matchService = matchService;
        this.partyService = partyService;
        this.profileService = profileService;
        this.hotbarService = hotbarService;
        this.spawnService = spawnService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.queueingAllowed = true;
        this.blockedCraftingMaterials = new ArrayList<>();
        this.loadBlockedCraftingMaterials();
    }


    @Override
    public boolean isQueueingAllowed() {
        return this.queueingAllowed;
    }

    @Override
    public void setQueueingAllowed(boolean allowed) {
        this.queueingAllowed = allowed;
    }

    @Override
    public void endAllMatches(Player issuer) {
        List<AbstractMatch> matches = new ArrayList<>(this.matchService.getMatches());
        if (matches.isEmpty()) {
            if (issuer != null) issuer.sendMessage(CC.translate("&cCould not find any matches to end."));
            return;
        }

        int rankedMatches = 0;
        int unrankedMatches = 0;

        for (AbstractMatch match : matches) {
            if (match.isRanked()) rankedMatches++;
            else unrankedMatches++;
            match.endMatch();
        }

        if (issuer != null) {
            issuer.sendMessage(CC.translate("&cEnding a total of &f" + unrankedMatches + " &cunranked matches and &f" + rankedMatches + " &cranked matches."));
        }
    }

    @Override
    public void disbandAllParties(Player issuer) {
        List<Party> parties = new ArrayList<>(this.partyService.getParties());
        if (parties.isEmpty()) {
            if (issuer != null) issuer.sendMessage(CC.translate("&cCould not find any parties to disband."));
            return;
        }

        if (issuer != null)
            issuer.sendMessage(CC.translate("&cDisbanding a total of &f" + parties.size() + " &cparties."));

        for (Party party : parties) {
            this.partyService.disbandParty(party.getLeader());
        }
    }

    @Override
    public void clearAllQueues(Player issuer) {
        int playersRemoved = 0;
        for (Profile profile : this.profileService.getProfiles().values()) {
            if (profile.getState() == EnumProfileState.WAITING && profile.getQueueProfile() != null) {
                Player queuePlayer = Bukkit.getPlayer(profile.getUuid());
                if (queuePlayer != null) {
                    profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());

                    profile.setState(EnumProfileState.LOBBY);
                    profile.setQueueProfile(null);
                    this.hotbarService.applyHotbarItems(queuePlayer);
                    this.spawnService.teleportToSpawn(queuePlayer);
                    queuePlayer.sendMessage(CC.translate("&cYou have been removed from the queue by an administrator."));
                    playersRemoved++;
                }
            }
        }

        if (issuer != null) {
            if (playersRemoved > 0) {
                issuer.sendMessage(CC.translate("&cRemoved &f" + playersRemoved + " &cplayer(s) from the queue."));
            } else {
                issuer.sendMessage(CC.translate("&cCould not find any players in a queue."));
            }
        }
    }

    @Override
    public List<Material> getBlockedCraftingMaterials() {
        return Collections.unmodifiableList(this.blockedCraftingMaterials);
    }

    @Override
    public void loadBlockedCraftingMaterials() {
        FileConfiguration config = this.configService.getSettingsConfig();
        List<String> blocked = config.getStringList(this.BLOCKED_MATERIALS_SECTION);

        this.blockedCraftingMaterials.clear();
        for (String mat : blocked) {
            try {
                Material material = Material.valueOf(mat);
                this.blockedCraftingMaterials.add(material);
            } catch (IllegalArgumentException ignored) {
                Bukkit.getLogger().warning("[ServerService] Invalid material in blocked list: " + mat);
            }
        }
    }

    @Override
    public void saveCraftingRecipes(Material material) {
        FileConfiguration config = this.configService.getSettingsConfig();
        File configFile = this.configService.getConfigFile("settings.yml");

        config.set(this.BLOCKED_MATERIALS_SECTION, this.blockedCraftingMaterials.stream().map(Material::name).collect(Collectors.toList()));

        this.configService.saveConfig(configFile, config);
        this.updateCraftingRecipes();
    }

    @Override
    public void addToBlockedCraftingList(Material material) {
        if (!this.blockedCraftingMaterials.contains(material)) {
            this.blockedCraftingMaterials.add(material);
        }
    }

    @Override
    public void removeFromBlockedCraftingList(Material material) {
        this.blockedCraftingMaterials.remove(material);
    }


    @Override
    public boolean isCraftable(Material material) {
        ItemStack itemStack = new ItemStack(material);
        return !Bukkit.getServer().getRecipesFor(itemStack).isEmpty();
    }

    @Override
    public void updateCraftingRecipes() {
        Bukkit.resetRecipes();

        Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();

        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();

            for (Material blockedMaterial : this.blockedCraftingMaterials) {
                if (recipe.getResult().getType() == blockedMaterial) {
                    iterator.remove();
                    break;
                }
            }
        }
    }
}