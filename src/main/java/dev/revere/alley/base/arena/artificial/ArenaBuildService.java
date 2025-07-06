package dev.revere.alley.base.arena.artificial;

import com.boydti.fawe.FaweAPI;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.patterns.BlockChance;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.RandomFillPattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.arena.impl.SharedArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author Remi
 * @project alley-practice
 * @date 6/07/2025
 */
@Service(provides = IArenaBuildService.class, priority = 510)
public class ArenaBuildService implements IArenaBuildService {
    private final Alley plugin;
    private final IArenaService arenaService;
    private final Gson gson = new Gson();

    private static final int ARENA_SPACING = 512;
    private static Location nextBuildLocation;

    private static final Map<String, Integer> MATERIAL_IDS = new HashMap<>();

    static {
        MATERIAL_IDS.put("air", 0);
        MATERIAL_IDS.put("stone", 1);
        MATERIAL_IDS.put("grass", 2);
        MATERIAL_IDS.put("dirt", 3);
        MATERIAL_IDS.put("cobblestone", 4);
        MATERIAL_IDS.put("wood", 5);
        MATERIAL_IDS.put("bedrock", 7);
        MATERIAL_IDS.put("water", 8);
        MATERIAL_IDS.put("lava", 10);
        MATERIAL_IDS.put("sand", 12);
        MATERIAL_IDS.put("gravel", 13);
        MATERIAL_IDS.put("gold_ore", 14);
        MATERIAL_IDS.put("iron_ore", 15);
        MATERIAL_IDS.put("coal_ore", 16);
        MATERIAL_IDS.put("log", 17);
        MATERIAL_IDS.put("leaves", 18);
        MATERIAL_IDS.put("sponge", 19);
        MATERIAL_IDS.put("glass", 20);
        MATERIAL_IDS.put("wool", 35);
        MATERIAL_IDS.put("brick", 45);
        MATERIAL_IDS.put("tnt", 46);
        MATERIAL_IDS.put("bookshelf", 47);
        MATERIAL_IDS.put("mossy_cobblestone", 48);
        MATERIAL_IDS.put("obsidian", 49);
        MATERIAL_IDS.put("diamond_ore", 56);
        MATERIAL_IDS.put("diamond_block", 57);
        MATERIAL_IDS.put("redstone_ore", 73);
        MATERIAL_IDS.put("ice", 79);
        MATERIAL_IDS.put("snow", 80);
        MATERIAL_IDS.put("clay", 82);
        MATERIAL_IDS.put("pumpkin", 86);
        MATERIAL_IDS.put("netherrack", 87);
        MATERIAL_IDS.put("glowstone", 89);
        MATERIAL_IDS.put("stone_brick", 98);
        MATERIAL_IDS.put("iron_bars", 101);
        MATERIAL_IDS.put("glass_pane", 102);
        MATERIAL_IDS.put("melon", 103);
        MATERIAL_IDS.put("mycelium", 110);
        MATERIAL_IDS.put("nether_brick", 112);
        MATERIAL_IDS.put("end_stone", 121);
        MATERIAL_IDS.put("emerald_ore", 129);
        MATERIAL_IDS.put("emerald_block", 133);
        MATERIAL_IDS.put("quartz_ore", 153);
        MATERIAL_IDS.put("quartz_block", 155);
        MATERIAL_IDS.put("prismarine", 168);
        MATERIAL_IDS.put("sea_lantern", 169);
        MATERIAL_IDS.put("magma", 213);
        MATERIAL_IDS.put("nether_wart_block", 214);
        MATERIAL_IDS.put("bone_block", 216);
    }

    public ArenaBuildService(Alley plugin, IArenaService arenaService) {
        this.plugin = plugin;
        this.arenaService = arenaService;
    }

    @Override
    public CompletableFuture<AbstractArena> buildArenaFromScript(Player issuer, String arenaName, EnumArenaType arenaType, String jsonScript) {
        CompletableFuture<AbstractArena> future = new CompletableFuture<>();

        try {
            Logger.info("[AI] Processing build script for arena: " + arenaName);
            Logger.info("[AI] Script content: " + jsonScript);

            List<Map<String, Object>> operations;

            try {
                Type operationListType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                operations = gson.fromJson(jsonScript, operationListType);
            } catch (Exception e) {
                try {
                    JsonObject jsonObject = gson.fromJson(jsonScript, JsonObject.class);
                    if (jsonObject.has("operations")) {
                        Type operationListType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                        operations = gson.fromJson(jsonObject.get("operations"), operationListType);
                    } else {
                        throw new IllegalArgumentException("JSON does not contain 'operations' array");
                    }
                } catch (Exception e2) {
                    throw new IllegalArgumentException("Unable to parse JSON as either direct array or wrapped object: " + e2.getMessage());
                }
            }

            if (operations == null || operations.isEmpty()) {
                throw new IllegalArgumentException("AI returned an empty or invalid build script.");
            }

            List<Map<String, Object>> finalOperations = operations;
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                try {
                    Location buildOrigin = getNextAvailableBuildLocation(issuer.getWorld());
                    Logger.info("[AI] Building arena at: " + buildOrigin.toString());

                    EditSession editSession = FaweAPI.getEditSessionBuilder(new BukkitWorld(issuer.getWorld()))
                            .fastmode(true)
                            .build();

                    Vector minBound = null;
                    Vector maxBound = null;

                    for (Map<String, Object> operation : finalOperations) {
                        try {
                            Vector[] bounds = executeOperation(operation, buildOrigin, editSession);
                            if (bounds != null) {
                                if (minBound == null) {
                                    minBound = bounds[0];
                                    maxBound = bounds[1];
                                } else {
                                    minBound = Vector.getMinimum(minBound, bounds[0]);
                                    maxBound = Vector.getMaximum(maxBound, bounds[1]);
                                }
                            }
                        } catch (Exception e) {
                            Logger.warn("[AI] Failed to execute operation: " + operation + " - " + e.getMessage());
                        }
                    }

                    editSession.flushQueue();

                    finalizeArena(arenaName, arenaType, buildOrigin, minBound, maxBound, future);
                } catch (Exception e) {
                    Logger.error("[AI] Failed during arena construction: " + e.getMessage());
                    future.completeExceptionally(new RuntimeException("Failed during arena construction: " + e.getMessage(), e));
                }
            });

        } catch (JsonSyntaxException e) {
            Logger.error("[AI] Malformed JSON script: " + e.getMessage());
            future.completeExceptionally(new RuntimeException("AI returned malformed JSON script.", e));
        } catch (Exception e) {
            Logger.error("[AI] Unexpected error: " + e.getMessage());
            future.completeExceptionally(e);
        }

        return future;
    }

    private Vector[] executeOperation(Map<String, Object> operation, Location origin, EditSession session) throws Exception {
        String type = (String) operation.get("type");
        if (type == null) {
            Logger.warn("[AI] Operation missing type: " + operation);
            return null;
        }

        Logger.info("[AI] Executing operation: " + type);

        switch (type.toUpperCase()) {
            case "FILL":
                return executeFill(operation, origin, session);
            case "WALLS":
                return executeWalls(operation, origin, session);
            case "HOLLOW":
                return executeHollow(operation, origin, session);
            case "REPLACE":
                return executeReplace(operation, origin, session);
            case "SPHERE":
                return executeSphere(operation, origin, session);
            case "CYLINDER":
                return executeCylinder(operation, origin, session);
            case "PYRAMID":
                return executePyramid(operation, origin, session);
            case "DOME":
                return executeDome(operation, origin, session);
            case "STRUCTURE":
                return executeStructure(operation, origin, session);
            case "TERRAIN":
                return executeTerrain(operation, origin, session);
            default:
                Logger.warn("[AI] Unknown operation type: " + type);
                return null;
        }
    }
    private Vector[] executeFill(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector pos1 = parseVector(op, "pos1", origin);
        Vector pos2 = parseVector(op, "pos2", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (pos1 == null || pos2 == null || pattern == null) return null;

        CuboidRegion region = new CuboidRegion(pos1, pos2);
        session.setBlocks(region, pattern);

        return new Vector[]{Vector.getMinimum(pos1, pos2), Vector.getMaximum(pos1, pos2)};
    }

    private Vector[] executeWalls(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector pos1 = parseVector(op, "pos1", origin);
        Vector pos2 = parseVector(op, "pos2", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (pos1 == null || pos2 == null || pattern == null) return null;

        CuboidRegion region = new CuboidRegion(pos1, pos2);
        session.makeWalls(region, pattern);

        return new Vector[]{Vector.getMinimum(pos1, pos2), Vector.getMaximum(pos1, pos2)};
    }

    private Vector[] executeHollow(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector pos1 = parseVector(op, "pos1", origin);
        Vector pos2 = parseVector(op, "pos2", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (pos1 == null || pos2 == null || pattern == null) return null;

        CuboidRegion region = new CuboidRegion(pos1, pos2);
        session.makeCuboidFaces(region, pattern);

        return new Vector[]{Vector.getMinimum(pos1, pos2), Vector.getMaximum(pos1, pos2)};
    }

    private Vector[] executeReplace(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector pos1 = parseVector(op, "pos1", origin);
        Vector pos2 = parseVector(op, "pos2", origin);
        Pattern fromPattern = parsePattern(op.get("from_material"));
        Pattern toPattern = parsePattern(op.get("to_material"));

        if (pos1 == null || pos2 == null || fromPattern == null || toPattern == null) return null;

        CuboidRegion region = new CuboidRegion(pos1, pos2);

        BlockMask mask = new BlockMask(session);
        if (fromPattern instanceof SingleBlockPattern) {
            mask.add(((SingleBlockPattern) fromPattern).getBlock());
        }

        session.replaceBlocks(region, mask, toPattern);

        return new Vector[]{Vector.getMinimum(pos1, pos2), Vector.getMaximum(pos1, pos2)};
    }

    private Vector[] executeSphere(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector center = parseVector(op, "center", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (center == null || pattern == null) return null;

        double radius = getDouble(op, "radius", 5.0);
        boolean filled = getBoolean(op, "filled", true);

        session.makeSphere(center, pattern, radius, filled);

        Vector radiusVec = new Vector(radius, radius, radius);
        return new Vector[]{center.subtract(radiusVec), center.add(radiusVec)};
    }

    private Vector[] executeCylinder(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector center = parseVector(op, "center", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (center == null || pattern == null) return null;

        double radius = getDouble(op, "radius", 5.0);
        int height = getInt(op, "height", 10);
        boolean filled = getBoolean(op, "filled", true);

        session.makeCylinder(center, pattern, radius, height, filled);

        Vector radiusVec = new Vector(radius, height / 2.0, radius);
        return new Vector[]{center.subtract(radiusVec), center.add(radiusVec)};
    }

    private Vector[] executePyramid(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector center = parseVector(op, "center", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (center == null || pattern == null) return null;

        int size = getInt(op, "size", 5);
        boolean filled = getBoolean(op, "filled", true);

        session.makePyramid(center, pattern, size, filled);

        Vector sizeVec = new Vector(size, size, size);
        return new Vector[]{center.subtract(sizeVec), center.add(sizeVec)};
    }

    private Vector[] executeDome(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        Vector center = parseVector(op, "center", origin);
        Pattern pattern = parsePattern(op.get("material"));

        if (center == null || pattern == null) return null;

        double radius = getDouble(op, "radius", 5.0);

        session.makeSphere(center, pattern, radius, true);

        Vector bottomMin = center.subtract(new Vector(radius, radius, radius));
        Vector bottomMax = center.subtract(new Vector(-radius, 0, -radius));
        CuboidRegion bottomRegion = new CuboidRegion(bottomMin, bottomMax);
        session.setBlocks(bottomRegion, new SingleBlockPattern(new BaseBlock(0)));

        Vector radiusVec = new Vector(radius, radius, radius);
        return new Vector[]{center.subtract(radiusVec), center.add(radiusVec)};
    }

    private Vector[] executeStructure(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        String structureType = getString(op, "structure_type", "tower");
        Vector position = parseVector(op, "position", origin);
        Pattern material = parsePattern(op.get("material"));

        if (position == null || material == null) return null;

        switch (structureType.toLowerCase()) {
            case "tower":
                return buildTower(position, material, session, op);
            case "bridge":
                return buildBridge(position, material, session, op);
            case "platform":
                return buildPlatform(position, material, session, op);
            case "stairs":
                return buildStairs(position, material, session, op);
            default:
                Logger.warn("[AI] Unknown structure type: " + structureType);
                return null;
        }
    }

    private Vector[] executeTerrain(Map<String, Object> op, Location origin, EditSession session) throws Exception {
        String terrainType = getString(op, "terrain_type", "hills");
        Vector center = parseVector(op, "center", origin);
        Pattern material = parsePattern(op.get("material"));

        if (center == null || material == null) return null;

        int size = getInt(op, "size", 20);
        int height = getInt(op, "height", 5);

        switch (terrainType.toLowerCase()) {
            case "hills":
                return generateHills(center, material, session, size, height);
            case "valleys":
                return generateValleys(center, material, session, size, height);
            case "plateau":
                return generatePlateau(center, material, session, size, height);
            default:
                Logger.warn("[AI] Unknown terrain type: " + terrainType);
                return null;
        }
    }

    private Vector[] buildTower(Vector pos, Pattern material, EditSession session, Map<String, Object> op) throws Exception {
        int height = getInt(op, "height", 20);
        int radius = getInt(op, "radius", 3);

        session.makeCylinder(pos, material, radius, height, false);

        for (int y = 0; y < height; y += 5) {
            Vector floorPos = pos.add(0, y, 0);
            session.makeCylinder(floorPos, material, radius - 1, 1, true);
        }

        Vector radiusVec = new Vector(radius, height, radius);
        return new Vector[]{pos.subtract(radiusVec), pos.add(radiusVec)};
    }

    private Vector[] buildBridge(Vector pos, Pattern material, EditSession session, Map<String, Object> op) throws Exception {
        int length = getInt(op, "length", 10);
        int width = getInt(op, "width", 3);
        String direction = getString(op, "direction", "east");

        Vector directionVec = getDirectionVector(direction);
        Vector end = pos.add(directionVec.multiply(length));

        for (int i = 0; i <= length; i++) {
            Vector bridgePos = pos.add(directionVec.multiply(i));
            for (int w = -width/2; w <= width/2; w++) {
                Vector perpendicular;
                if (directionVec.getX() != 0) {
                    perpendicular = new Vector(0, 0, 1);
                } else {
                    perpendicular = new Vector(1, 0, 0);
                }

                Vector sidePos = bridgePos.add(perpendicular.multiply(w));
                session.setBlock(sidePos, material.next(sidePos));
            }
        }

        Vector min = Vector.getMinimum(pos, end).subtract(new Vector(width/2, 0, width/2));
        Vector max = Vector.getMaximum(pos, end).add(new Vector(width/2, 1, width/2));
        return new Vector[]{min, max};
    }

    private Vector[] buildPlatform(Vector pos, Pattern material, EditSession session, Map<String, Object> op) throws Exception {
        int size = getInt(op, "size", 10);
        int height = getInt(op, "height", 1);

        Vector pos1 = pos.subtract(new Vector(size/2, 0, size/2));
        Vector pos2 = pos.add(new Vector(size/2, height, size/2));

        CuboidRegion region = new CuboidRegion(pos1, pos2);
        session.setBlocks(region, material);

        return new Vector[]{pos1, pos2};
    }

    private Vector[] buildStairs(Vector pos, Pattern material, EditSession session, Map<String, Object> op) throws Exception {
        int steps = getInt(op, "steps", 10);
        int width = getInt(op, "width", 3);
        String direction = getString(op, "direction", "north");

        Vector stepDirection = getDirectionVector(direction);

        for (int i = 0; i < steps; i++) {
            Vector stepPos = pos.add(stepDirection.multiply(i)).add(0, i, 0);
            for (int w = -width/2; w <= width/2; w++) {
                Vector sidePos = stepPos.add(stepDirection.cross(new Vector(0, 1, 0)).normalize().multiply(w));
                session.setBlock(sidePos, material.next(sidePos));
            }
        }

        Vector min = pos.subtract(new Vector(width/2, 0, width/2));
        Vector max = pos.add(stepDirection.multiply(steps)).add(new Vector(width/2, steps, width/2));
        return new Vector[]{min, max};
    }

    private Vector[] generateHills(Vector center, Pattern material, EditSession session, int size, int height) throws Exception {
        Random rand = new Random();

        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                double distance = Math.sqrt(x*x + z*z);
                if (distance <= size) {
                    double hillHeight = height * (1 - (distance / size)) * (0.5 + rand.nextDouble() * 0.5);
                    Vector pos = center.add(x, 0, z);

                    for (int y = 0; y <= hillHeight; y++) {
                        session.setBlock(pos.add(0, y, 0), material.next(pos));
                    }
                }
            }
        }

        Vector sizeVec = new Vector(size, height, size);
        return new Vector[]{center.subtract(sizeVec), center.add(sizeVec)};
    }

    private Vector[] generateValleys(Vector center, Pattern material, EditSession session, int size, int height) throws Exception {
        Random rand = new Random();

        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                double distance = Math.sqrt(x*x + z*z);
                if (distance <= size) {
                    double valleyDepth = height * (distance / size) * (0.3 + rand.nextDouble() * 0.4);
                    Vector pos = center.add(x, 0, z);

                    for (int y = 0; y >= -valleyDepth; y--) {
                        session.setBlock(pos.add(0, y, 0), new SingleBlockPattern(new BaseBlock(0)));
                    }

                    session.setBlock(pos.add(0, -valleyDepth, 0), material.next(pos));
                }
            }
        }

        Vector sizeVec = new Vector(size, height, size);
        return new Vector[]{center.subtract(sizeVec), center.add(sizeVec)};
    }

    private Vector[] generatePlateau(Vector center, Pattern material, EditSession session, int size, int height) throws Exception {
        Vector pos1 = center.subtract(new Vector(size/2, 0, size/2));
        Vector pos2 = center.add(new Vector(size/2, height, size/2));

        CuboidRegion region = new CuboidRegion(pos1, pos2);
        session.setBlocks(region, material);

        return new Vector[]{pos1, pos2};
    }

    private Pattern parsePercentagePattern(String pattern) {
        String[] parts = pattern.split(",");
        List<BlockChance> blocks = new ArrayList<>();

        for (String part : parts) {
            String[] split = part.trim().split("%");
            if (split.length == 2) {
                double percentage = Double.parseDouble(split[0]) / 100.0;
                String material = split[1];
                BaseBlock block = getBlockWithData(material);

                blocks.add(new BlockChance(block, percentage));
            }
        }

        if (blocks.isEmpty()) {
            return new SingleBlockPattern(new BaseBlock(0));
        }

        return new RandomFillPattern(blocks);
    }

    private BaseBlock getBlockWithData(String materialName) {
        String cleanName = materialName.toLowerCase().trim();

        switch (cleanName) {
            case "white_clay": return new BaseBlock(172, 0);
            case "orange_clay": return new BaseBlock(172, 1);
            case "magenta_clay": return new BaseBlock(172, 2);
            case "light_blue_clay": return new BaseBlock(172, 3);
            case "yellow_clay": return new BaseBlock(172, 4);
            case "lime_clay": return new BaseBlock(172, 5);
            case "pink_clay": return new BaseBlock(172, 6);
            case "gray_clay": return new BaseBlock(172, 7);
            case "light_gray_clay": return new BaseBlock(172, 8);
            case "cyan_clay": return new BaseBlock(172, 9);
            case "purple_clay": return new BaseBlock(172, 10);
            case "blue_clay": return new BaseBlock(172, 11);
            case "brown_clay": return new BaseBlock(172, 12);
            case "green_clay": return new BaseBlock(172, 13);
            case "red_clay": return new BaseBlock(172, 14);
            case "black_clay": return new BaseBlock(172, 15);
            default:
                int materialId = MATERIAL_IDS.getOrDefault(cleanName, 1);
                return new BaseBlock(materialId);
        }
    }

    private Pattern parsePattern(Object patternData) {
        if (patternData == null) {
            return new SingleBlockPattern(new BaseBlock(0));
        }

        if (!(patternData instanceof String)) {
            return new SingleBlockPattern(new BaseBlock(0));
        }

        String pattern = (String) patternData;

        if (pattern.contains("%")) {
            return parsePercentagePattern(pattern);
        }

        return new SingleBlockPattern(getBlockWithData(pattern));
    }

    private Vector parseVector(Map<String, Object> data, String key, Location origin) {
        Object vectorData = data.get(key);
        if (vectorData == null) return null;

        if (vectorData instanceof Map) {
            Map<String, Object> coords = (Map<String, Object>) vectorData;
            double x = getDouble(coords, "x", 0);
            double y = getDouble(coords, "y", 0);
            double z = getDouble(coords, "z", 0);

            return new Vector(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
        }

        return null;
    }

    private Vector getDirectionVector(String direction) {
        switch (direction.toLowerCase()) {
            case "north": return new Vector(0, 0, -1);
            case "south": return new Vector(0, 0, 1);
            case "east": return new Vector(1, 0, 0);
            case "west": return new Vector(-1, 0, 0);
            case "up": return new Vector(0, 1, 0);
            case "down": return new Vector(0, -1, 0);
            default: return new Vector(0, 0, -1);
        }
    }

    private double getDouble(Map<String, Object> data, String key, double defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }

    private int getInt(Map<String, Object> data, String key, int defaultValue) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    private boolean getBoolean(Map<String, Object> data, String key, boolean defaultValue) {
        Object value = data.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }

    private String getString(Map<String, Object> data, String key, String defaultValue) {
        Object value = data.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return defaultValue;
    }

    private void finalizeArena(String arenaName, EnumArenaType arenaType, Location origin,
                               Vector minBound, Vector maxBound, CompletableFuture<AbstractArena> future) {

        if (minBound == null || maxBound == null) {
            future.completeExceptionally(new RuntimeException("Could not determine arena bounds from the AI's build script."));
            return;
        }

        Vector padding = new Vector(5, 5, 5);
        minBound = minBound.subtract(padding);
        maxBound = maxBound.add(padding);

        Location min = BukkitUtil.toLocation(origin.getWorld(), minBound);
        Location max = BukkitUtil.toLocation(origin.getWorld(), maxBound);

        AbstractArena newArena = (arenaType == EnumArenaType.STANDALONE)
                ? new StandAloneArena(arenaName, min, max, null, null, 256, 0)
                : new SharedArena(arenaName, min, max);

        newArena.setDisplayName(CC.translate("&e" + arenaName));
        newArena.setEnabled(true);

        Location center = min.clone().add(max.clone().subtract(min).toVector().multiply(0.5));
        newArena.setCenter(center);

        newArena.setPos1(center.clone().add(10, 1, 0));
        newArena.setPos2(center.clone().subtract(10, -1, 0));

        this.arenaService.registerNewArena(newArena);
        this.arenaService.saveArena(newArena);

        Logger.info("[AI] Successfully created arena: " + arenaName + " at " + center);
        future.complete(newArena);
    }

    private synchronized Location getNextAvailableBuildLocation(World world) {
        if (nextBuildLocation == null) {
            nextBuildLocation = new Location(world, 10000, 100, 10000);
        }
        Location locationToUse = nextBuildLocation.clone();
        nextBuildLocation.add(ARENA_SPACING, 0, 0);
        return locationToUse;
    }
}