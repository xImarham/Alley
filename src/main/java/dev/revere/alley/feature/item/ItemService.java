package dev.revere.alley.feature.item;

import dev.revere.alley.config.IConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.visual.TexturesConstant;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
@Service(provides = IItemService.class, priority = 440)
public class ItemService implements IItemService {
    private final IConfigService configService;

    protected String GOLDEN_HEAD_TEXTURE;
    private ItemStack goldenHead;

    /**
     * DI Constructor for the ItemService class.
     *
     * @param configService The configuration service to load item textures from.
     */
    public ItemService(IConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadGoldenHeadTextureAndConstructItem();
    }

    private void loadGoldenHeadTextureAndConstructItem() {
        FileConfiguration config = this.configService.getTexturesConfig();
        if (config == null) {
            throw new IllegalStateException("Textures configuration is not loaded.");
        }

        String path = "golden-head";

        this.GOLDEN_HEAD_TEXTURE = config.getString(path + ".texture", TexturesConstant.GOLDEN_STEVE_SKIN);

        String name = config.getString(path + ".name", "&6Golden Head");
        List<String> lore = config.getStringList(path + ".lore");

        this.goldenHead = new ItemBuilder(Material.SKULL_ITEM)
                .name(name)
                .lore(lore)
                .durability(3)
                .setSkullTexture(this.GOLDEN_HEAD_TEXTURE)
                .build();
    }

    @Override
    public ItemStack getGoldenHead() {
        return this.goldenHead;
    }

    @Override
    public void performHeadConsume(Player player, ItemStack item) {
        if (player.getHealth() >= player.getMaxHealth()) {
            player.sendMessage(CC.translate("&cYou're already full at health!"));
            return;
        }

        double currentHealth = player.getHealth();

        player.setHealth(Math.min(player.getHealth() + 8.0, 20.0));
        player.setSaturation(Math.min(player.getSaturation() + 4.0F, 20.0F));

        double healthGained = player.getHealth() - currentHealth;

        player.sendMessage(CC.translate("&6+" + healthGained + " &4❤"));

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }
    }
}