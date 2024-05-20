package me.emmy.alley.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class RandomItemBuilder {
 
    /**
     * Vars
     */
    private Inventory inventory;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private SkullMeta skullMeta;
    private PotionMeta potionMeta;
 
    @Expose
    private Material type;
    @Expose
    private int amount;
    @Expose
    private int data;
    @Expose
    private int position = -1;
    @Expose
    private String name;
    @Expose
    private boolean unbreackable = false;
    @Expose
    private List<String> lores = new ArrayList<>();
    @Expose
    private Map<String, Integer> enchantments = new HashMap<>();
    @Expose
    private List<ItemFlag> itemFlags = new ArrayList<>();
    @Expose
    private int durability;
    @Expose
    private PotionType potionType;
    @Expose
    private boolean splash;
    @Expose
    private LinkedList<String> potionEffectType = new LinkedList<>();
    @Expose
    private LinkedList<Integer> potionDurration = new LinkedList<>();
    @Expose
    private LinkedList<Integer> potionAmplifier = new LinkedList<>();
    @Expose
    private LinkedList<Boolean> potionAmbient = new LinkedList<>();
    @Expose
    private LinkedList<Boolean> potionOverwrite = new LinkedList<>();
 
    /**
     * init ItemBuilder without argument
     */
    public RandomItemBuilder(){
        this(Material.AIR);
    }
 
    /**
     * init ItemBuilder
     * @param material
     */
    public RandomItemBuilder(Material material){
        this(material, 1);
    }
 
    /**
     * init ItemBuilder
     * @param material
     * @param amount
     */
    public RandomItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }
 
    /**
     * init ItemBuilder
     * @param material
     * @param amount
     * @param data
     */
    public RandomItemBuilder(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (short) data);
        this.itemMeta = this.itemStack.getItemMeta();
    }
 
    /**
     * init ItemBuilder
     * @param itemStack
     */
    public RandomItemBuilder(ItemStack itemStack){
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }
 
    /**
     * init ItemBuilder from his gson string
     * @param gsonString
     */
    public static RandomItemBuilder fromString(String gsonString) {
        RandomItemBuilder randomItemBuilder = new Gson().fromJson(gsonString, RandomItemBuilder.class);
        randomItemBuilder.setItem(randomItemBuilder.type);
        randomItemBuilder.setAmount(randomItemBuilder.amount);
        randomItemBuilder.setData(randomItemBuilder.data);
        randomItemBuilder.setPosition(randomItemBuilder.position);
        if (!randomItemBuilder.name.equals(""))
            randomItemBuilder.setName(randomItemBuilder.coloredString(randomItemBuilder.name));
        randomItemBuilder.setNewDurability(randomItemBuilder.durability);
        randomItemBuilder.setUnbreakable(randomItemBuilder.unbreackable);
        List<String> list = new ArrayList<>();
        randomItemBuilder.lores.forEach(s -> list.add(randomItemBuilder.coloredString(s)));
        randomItemBuilder.addLore(list);
        randomItemBuilder.addItemFlag(randomItemBuilder.itemFlags);
        randomItemBuilder.setPotion(randomItemBuilder.potionType, randomItemBuilder.splash);
        randomItemBuilder.enchantments.forEach((s, integer) -> {
            Enchantment enchant = Enchantment.getByName(s);
            randomItemBuilder.addEnchantment(enchant, integer, integer > enchant.getMaxLevel());
        });
        List<Boolean> currentOverwrite = new ArrayList<>(randomItemBuilder.potionOverwrite);
        randomItemBuilder.potionOverwrite.clear();
        for (int i = 0; i < randomItemBuilder.potionEffectType.size(); i++) {
            randomItemBuilder.addPotionEffect(PotionEffectType.getByName(randomItemBuilder.potionEffectType.get(i)),
                    randomItemBuilder.potionDurration.get(i), randomItemBuilder.potionAmplifier.get(i),
                    randomItemBuilder.potionAmbient.get(i), currentOverwrite.get(i));
        }
        return randomItemBuilder;
    }
 
    /**
     * Set item
     * @param material
     * @return
     */
    public RandomItemBuilder setItem(Material material){
        this.itemStack.setType(material);
        return this;
    }
 
    /**
     * Set amount
     * @param amount
     * @return
     */
    public RandomItemBuilder setAmount(int amount){
        this.itemStack.setAmount(amount);
        return this;
    }
 
    /**
     * Set data
     * @param data
     * @return
     */
    public RandomItemBuilder setData(int data){
        this.itemStack = new ItemStack(itemStack.getType(), itemStack.getAmount(), (byte)data);
        return this;
    }
 
    /**
     * Set ItemStack
     * @param itemStack
     * @return
     */
    public RandomItemBuilder setItemStack(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }
 
    /**
     * set this.inventory value
     * @param inventory
     * @return
     */
    public RandomItemBuilder setInventory(Inventory inventory){
        this.inventory = inventory;
        return this;
    }
 
    /**
     * @param unbreakable
     * Set item in unbreakable/breakable
     * @return
     */
    private RandomItemBuilder setUnbreakable(boolean unbreakable){
        if (this.itemMeta == null) {
            if (this.itemStack == null)
                return null;
            this.itemMeta = this.itemStack.getItemMeta();
        }
        assert this.itemMeta != null;
        this.itemMeta.spigot().setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }
 
    /**
     * set the display name of the item
     * @param name
     * @return
     */
    public RandomItemBuilder setName(String name){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * Add lore from String list
     * @param lores
     * @return
     */
    public RandomItemBuilder addLore(List<String> lores){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * Add lore from String...
     * @param lores
     * @return
     */
    public RandomItemBuilder addLore(String... lores){
        addLore(Arrays.asList(lores));
        return this;
    }
 
    /**
     * add enchant to the item
     * @param enchantment
     * @param value
     * @param ignoreLevelRestriction
     * @return
     */
    public RandomItemBuilder addEnchantment(Enchantment enchantment, int value, boolean ignoreLevelRestriction){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, value, ignoreLevelRestriction);
        this.itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * add enchants from map (use for json object)
     * @param enchantment
     * @return
     */
    public RandomItemBuilder setEnchants(Map<Enchantment, Integer> enchantment){
        for (Map.Entry<Enchantment, Integer> entry : enchantment.entrySet()) {
            Enchantment enchant = entry.getKey();
            addEnchantment(enchant, entry.getValue(), entry.getValue() > enchant.getMaxLevel());
        }
        return this;
    }
 
    /**
     * Remove an enchantment on the item
     * @param enchantment
     * @return
     */
    public RandomItemBuilder removeEnchant(Enchantment enchantment) {
        if (!this.getEnchantments().containsKey(enchantment))
            return this;
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeEnchant(enchantment);
        this.itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * remove all enchant on item from a list
     * @param enchantments
     * @return
     */
    public RandomItemBuilder removeEnchants(List<Enchantment> enchantments){
        for (Enchantment enchantment : enchantments) {
            if (!this.getEnchantments().containsKey(enchantment))
                continue;
            this.removeEnchant(enchantment);
        }
        return this;
    }
 
    /**
     * remove all enchantment on the item
     * @return
     */
    public RandomItemBuilder clearEnchants() {
        if (this.getEnchantments() == null)
            return this;
        for (Enchantment enchantment : this.getEnchantments().keySet())
            this.removeEnchant(enchantment);
        return this;
    }
 
    /**
     * add ItemFlag on your item
     * @param itemFlag
     * @return
     */
    public RandomItemBuilder addItemFlag(ItemFlag itemFlag){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * add ItemFlags on your item
     * @param itemFlag
     * @return
     */
    public RandomItemBuilder addItemFlag(ItemFlag... itemFlag){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * add ItemFlags on your item from ItemFlag list
     * @param itemFlag
     * @return
     */
    public RandomItemBuilder addItemFlag(List<ItemFlag> itemFlag){
        itemFlag.forEach(this::addItemFlag);
        return this;
    }
 
    /**
     * remove ItemFlag on your item
     * @param itemFlag
     * @return
     */
    public RandomItemBuilder removeItemFlag(ItemFlag itemFlag){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * hide enchant
     * @return
     */
    public RandomItemBuilder hideEnchant(){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * show enchant
     * @return
     */
    public RandomItemBuilder showEnchant(){
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }
 
    /**
     * Set durrability of item
     * /!\ 100 >= percent >= 0
     * @param percent
     * @return
     */
    public RandomItemBuilder setDurability(float percent){
        if (percent > 100.0){
            return this;
        }else if (percent < 0.0){
            return this;
        }
        itemStack.setDurability((short) (itemStack.getDurability() * (percent / 100)));
        return this;
    }
 
    /**
     * Set durrability of item
     * @param durability
     * @return
     */
    public RandomItemBuilder setNewDurability(int durability){
        itemStack.setDurability((short)durability);
        return this;
    }
 
    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param playerName
     * @return
     */
    public RandomItemBuilder setSkullTextureFromePlayerName(String playerName){
        this.skullMeta = (SkullMeta) itemStack.getItemMeta();
        this.skullMeta.setOwner(playerName);
        itemStack.setItemMeta(skullMeta);
        return this;
    }
 
    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param player
     * @return
     */
    public RandomItemBuilder setSkullTexture(Player player){
        setSkullTextureFromePlayerName(player.getName());
        return this;
    }
 
    /**
     * If your item is a player skull you can apply a texture
     * value is the base64 value of the skull texture
     * You can find the value on https://minecraft-heads.com
     * @param value
     * @return
     */
 
    /**
     * Obtain the texture (in BASE64) of a skull_item.
     * Need Player skull type data.
     * @return
     */
 
    /**
     * apply potion effect type on the potion bottle
     * @param potionEffectType
     * @return
     */
    public RandomItemBuilder addPotionEffect(PotionEffectType potionEffectType) {
        return addPotionEffect(potionEffectType, 10 * 20);
    }
 
    /**
     * apply potion effect type with duration (in tick) on the potion bottle
     * @param potionEffectType
     * @param duration
     * @return
     */
    public RandomItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration) {
        return addPotionEffect(potionEffectType, duration, 1);
    }
 
    /**
     * apply potion effect type with duration and level on the potion bottle
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @return
     */
    public RandomItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
        return addPotionEffect(potionEffectType, duration, amplifier, true);
    }
 
    /**
     * apply potion effect type with duration, level and ambiance option on the potion bottle
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @param ambient
     * @return
     */
    public RandomItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient) {
        return addPotionEffect(potionEffectType, duration, amplifier, ambient, false);
    }
 
    /**
     * apply potion effect type with duration, level and ambiance option on the potion bottle, can make this effect to overwrite
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @param ambient
     * @param overwrite
     * @return
     */
    public RandomItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient, boolean overwrite) {
        if (getMaterial() !=  Material.POTION)
            return this;
        this.potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        this.potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier, ambient), overwrite);
        this.itemStack.setItemMeta(this.potionMeta);
        this.potionOverwrite.add(overwrite);
        return this;
    }
 
    /**
     * remove specific potion effect type
     * @param potionEffectType
     * @return
     */
    public RandomItemBuilder removePotionEffect(PotionEffectType potionEffectType) {
        this.potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        if (this.potionMeta == null || !this.potionMeta.hasCustomEffect(potionEffectType))
            return this;
        this.potionMeta.removeCustomEffect(potionEffectType);
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }
 
    /**
     * remove all potion effect from a list
     * @param potionEffectTypes
     * @return
     */
    public RandomItemBuilder removePotionEffect(List<PotionEffectType> potionEffectTypes) {
        for (PotionEffectType potionEffectType : potionEffectTypes) {
            if (this.potionMeta == null || !this.potionMeta.hasCustomEffect(potionEffectType))
                continue;
            removePotionEffect(potionEffectType);
        }
        return this;
    }
 
    /**
     * clear potion effect on item
     * @return
     */
    public RandomItemBuilder clearPotionEffect() {
        if (this.getPotionEffects() == null)
            return this;
        for (PotionEffect potionEffect : this.getPotionEffects()) {
            removePotionEffect(potionEffect.getType());
        }
        return this;
    }
 
    /**
     * set potion type on the potion
     * @param potionType
     * @return
     */
    public RandomItemBuilder setPotion(PotionType potionType) {
        setPotion(potionType, true);
        return this;
    }
 
    /**
     * set potion type on the item with splash option
     * @param potionType
     * @param splash
     * @return
     */
    public RandomItemBuilder setPotion(PotionType potionType, boolean splash) {
        if (getMaterial() != Material.POTION)
            return this;
        Potion potion = new Potion(potionType);
        potion.setSplash(splash);
        potion.setType(potionType);
        potion.apply(this.itemStack);
        this.potionType = potionType;
        this.splash = splash;
        return this;
    }
 
    /**
     * Inject item in inventory
     * @param inventory
     * @param position
     * @return
     */
    public RandomItemBuilder inject(Inventory inventory, int position){
        inventory.setItem(position, toItemStack());
        return this;
    }
 
    /**
     * Inject item in inventory
     * @param inventory
     * @return
     */
    public RandomItemBuilder inject(Inventory inventory){
        inventory.addItem(toItemStack());
        return this;
    }
 
    /**
     * Inject item in inventory
     * @param position
     * @return
     */
    public RandomItemBuilder inject(int position){
        inventory.setItem(position, toItemStack());
        return this;
    }
 
    /**
     * Inject item in inventory
     * @return
     */
    public RandomItemBuilder inject(){
        this.inventory.addItem(toItemStack());
        return this;
    }
 
    /**
     * Open inventory to the player
     * @param player
     */
    public void open(Player player){
        player.openInventory(inventory);
    }
 
    /**
     * Set the position of the item
     * @param position
     * @return
     */
    public RandomItemBuilder setPosition(int position) {
        this.position = position;
        return this;
    }
 
    /**
     * get position
     * @return
     */
    public int getPosition(){
        return this.position;
    }
 
    /**
     * build item
     * @return
     */
    public ItemStack toItemStack(){
        return itemStack;
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder are similar
     * This method compare type, data, and display name of items
     * @return
     */
    public boolean isSimilar(RandomItemBuilder randomItemBuilder){
        return hasSameMaterial(randomItemBuilder) && hasSameData(randomItemBuilder) && hasSameDisplayName(randomItemBuilder);
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder are exactly same
     * This method compare all parameter of items
     * @return
     */
    public boolean isExactlySame(RandomItemBuilder randomItemBuilder){
        return hasSameMaterial(randomItemBuilder) && hasSameData(randomItemBuilder) && hasSameDisplayName(randomItemBuilder)
                && hasSameAmount(randomItemBuilder) && hasSameDurability(randomItemBuilder) && hasSameEnchantment(randomItemBuilder)
                && hasSameItemFlag(randomItemBuilder) && hasSameLore(randomItemBuilder) && hasSameBreakableStat(randomItemBuilder);
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same type
     * @return
     */
    public boolean hasSameMaterial(RandomItemBuilder randomItemBuilder){
        return getMaterial() == randomItemBuilder.getMaterial();
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same display name
     * @return
     */
    public boolean hasSameDisplayName(RandomItemBuilder randomItemBuilder){
        return getDisplayName().equalsIgnoreCase(randomItemBuilder.getDisplayName());
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same enchantments
     * @return
     */
    public boolean hasSameEnchantment(RandomItemBuilder randomItemBuilder){
        return getEnchantments().equals(randomItemBuilder.getEnchantments());
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same item flags
     * @return
     */
    public boolean hasSameItemFlag(RandomItemBuilder randomItemBuilder){
        return getItemFlag().equals(randomItemBuilder.getItemFlag());
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same lore
     * @return
     */
    public boolean hasSameLore(RandomItemBuilder randomItemBuilder){
        if (getLore() == null)
            return false;
        return getLore().equals(randomItemBuilder.getLore());
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same data
     * @return
     */
    public boolean hasSameData(RandomItemBuilder randomItemBuilder){
        return getData() == randomItemBuilder.getData();
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same amount
     * @return
     */
    public boolean hasSameAmount(RandomItemBuilder randomItemBuilder){
        return getAmount() == randomItemBuilder.getAmount();
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same durability
     * @return
     */
    public boolean hasSameDurability(RandomItemBuilder randomItemBuilder){
        return getDurability() == randomItemBuilder.getDurability();
    }
 
    /**
     * @param randomItemBuilder
     * returns if two item builder has same breakable stat
     * @return
     */
    public boolean hasSameBreakableStat(RandomItemBuilder randomItemBuilder){
        return isUnbreakable() == randomItemBuilder.isUnbreakable();
    }
 
    /**
     * get type
     * @return
     */
    public Material getMaterial(){
        return itemStack.getType();
    }
 
    /**
     * get amount
     * @return
     */
    public int getAmount(){
        return itemStack.getAmount();
    }
 
    /**
     * get data
     * @return
     */
    public int getData(){
        return itemStack.getData().getData();
    }
 
    /**
     * get durability
     * @return
     */
    public int getDurability(){
        return itemStack.getDurability();
    }
 
    /**
     * get item meta
     * @return
     */
    public ItemMeta getItemMeta(){
        return itemMeta;
    }
 
    /**
     * get display name
     * @return
     */
    public String getDisplayName(){
        return itemStack.hasItemMeta() && itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : "";
    }
 
    /**
     * get enchant
     * @return
     */
    public Map<Enchantment, Integer> getEnchantments(){
        return this.itemStack.hasItemMeta() && this.itemMeta.hasEnchants() ? this.itemMeta.getEnchants() : new HashMap<>();
    }
 
    /**
     * get lore
     * @return
     */
    public List<String> getLore(){
        return itemStack.hasItemMeta() && itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
    }
 
    /**
     * get item flag
     * @return
     */
    public Set<ItemFlag> getItemFlag(){
        return itemStack.hasItemMeta() && itemMeta.getItemFlags().size() > 0 ? itemMeta.getItemFlags() : new HashSet<>();
    }
 
    /**
     * get potion effects
     * @return
     */
    public List<PotionEffect> getPotionEffects() {
        return this.potionMeta != null && this.potionMeta.getCustomEffects().size() > 0 ? this.potionMeta.getCustomEffects() : new ArrayList<>();
    }
 
    /**
     * get potion type
     * @return
     */
    public PotionType getPotionType() {
        return this.potionMeta != null ? this.potionType : getPotionEffects().size() > 0 ?
                PotionType.getByEffect(getPotionEffects().get(0).getType()) : PotionType.WATER;
    }
 
    /**
     * get if is splash potion
     * @return
     */
    public boolean isSplashPotion() {
        return this.splash;
    }
 
    /**
     * get if item is or isn't unbreakable
     * @return
     */
    public boolean isUnbreakable(){
        return itemStack.hasItemMeta() && itemMeta.spigot().isUnbreakable();
    }
 
    /**
     * parse ItemBuilder to String
     * @return
     */
    @Override
    public String toString() {
        this.type = getMaterial();
        this.amount = getAmount();
        this.data = getData();
        this.position = getPosition();
        this.name = uncoloredString(getDisplayName());
        this.unbreackable = isUnbreakable();
        this.lores.clear();
        getLore().forEach(s -> this.lores.add(uncoloredString(s)));
        Map<String, Integer> enchant = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> entry : getEnchantments().entrySet())
            enchant.put(entry.getKey().getName(), entry.getValue());
        this.enchantments = new HashMap<>(enchant);
        this.itemFlags = new ArrayList<>(getItemFlag());
        this.durability = getDurability();
        this.potionType = getPotionType();
        this.splash = isSplashPotion();
        this.potionEffectType.clear();
        this.potionDurration.clear();
        this.potionAmplifier.clear();
        this.potionAmbient.clear();
        for (PotionEffect potionEffect : getPotionEffects()) {
            this.potionEffectType.add(potionEffect.getType().getName());
            this.potionDurration.add(potionEffect.getDuration());
            this.potionAmplifier.add(potionEffect.getAmplifier());
            this.potionAmbient.add(potionEffect.isAmbient());
        }
 
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
 
    /**
     * @param string
     * @return
     */
    private String uncoloredString(String string){
        return string.replace(ChatColor.COLOR_CHAR + "", "&");
    }
 
    /**
     * @param string
     * @return
     */
    private String coloredString(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }
 
}