package dev.revere.alley.base.arena.artificial;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.tool.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Remi
 * @project alley-practice
 * @date 6/07/2025
 */
@Service(provides = IAIArenaGeneratorService.class, priority = 500)
public class AIArenaGeneratorService implements IAIArenaGeneratorService {
    private final IConfigService configService;
    private final ExecutorService executorService;
    private final Gson gson = new Gson();

    private String apiToken;
    private String modelVersion;

    public AIArenaGeneratorService(IConfigService configService) {
        this.configService = configService;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initialize(AlleyContext context) {
        this.apiToken = configService.getAiConfig().getString("replicate.api-token", "YOUR-REPLICATE-API-KEY-HERE");
        this.modelVersion = configService.getAiConfig().getString("replicate.model-version");

        if (apiToken == null || apiToken.equals("YOUR-REPLICATE-API-KEY-HERE") || modelVersion == null) {
            Logger.error("Replicate API Token or Model Version is not configured in ai.yml. Arena generation will be disabled.");
        }
    }

    @Override
    public void shutdown(AlleyContext context) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    @Override
    public CompletableFuture<String> generateBuildScript(String userPrompt, String dimensions) {
        if (apiToken == null || apiToken.equals("YOUR-REPLICATE-API-KEY-HERE")) {
            return exceptionallyCompletedFuture(new IllegalStateException("Replicate API token is not configured."));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                String pollingUrl = startPrediction(userPrompt, dimensions);
                return pollForResult(pollingUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }

    private String startPrediction(String userPrompt, String dimensions) throws Exception {
        String systemPrompt = buildSystemPrompt();
        String fullUserPrompt = buildUserPrompt(userPrompt, dimensions);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("prompt", fullUserPrompt);
        inputMap.put("system_prompt", systemPrompt);
        inputMap.put("temperature", 0.8);
        inputMap.put("top_p", 0.9);
        inputMap.put("max_tokens", 10000);

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("input", inputMap);
        String requestBody = gson.toJson(bodyMap);

        URL url = new URL("https://api.replicate.com/v1/models/" + this.modelVersion + "/predictions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Token " + this.apiToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (connection.getResponseCode() != 201) {
            throw new RuntimeException("Failed to create prediction: " + readResponse(connection));
        }

        String responseBody = readResponse(connection);
        JsonObject jsonResponse = new JsonParser().parse(responseBody).getAsJsonObject();
        return jsonResponse.getAsJsonObject("urls").get("get").getAsString();
    }

    private String buildSystemPrompt() {
        return "You are an expert Minecraft arena builder with deep knowledge of PvP arena design and WorldEdit commands.\n" +
                "Your task is to create detailed, balanced, and visually appealing PvP arenas using a specific JSON format.\n\n" +
                "CRITICAL RULES:\n" +
                "1. ONLY return a valid JSON ARRAY - no explanations, no markdown, no extra text, no wrapping objects\n" +
                "2. Start your response with '[' and end with ']'\n" +
                "3. All coordinates are relative to origin (0,0,0)\n" +
                "4. Floor level should always be at y=0\n" +
                "5. Use y-coordinates from 0 to 30 for most builds\n" +
                "6. Create balanced, competitive PvP layouts\n" +
                "7. Include varied terrain and strategic elements\n\n" +
                "REQUIRED JSON FORMAT - Direct array of operations:\n" +
                "[\n" +
                "  {\n" +
                "    \"type\": \"FILL\",\n" +
                "    \"pos1\": {\"x\": 0, \"y\": 0, \"z\": 0},\n" +
                "    \"pos2\": {\"x\": 10, \"y\": 1, \"z\": 10},\n" +
                "    \"material\": \"stone\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"STRUCTURE\",\n" +
                "    \"structure_type\": \"tower\",\n" +
                "    \"position\": {\"x\": 5, \"y\": 0, \"z\": 5},\n" +
                "    \"material\": \"stone_brick\",\n" +
                "    \"height\": 15,\n" +
                "    \"radius\": 3\n" +
                "  }\n" +
                "]\n\n" +
                "AVAILABLE OPERATIONS:\n" +
                "- FILL: Fill a rectangular area with blocks, this is usually barriers or glass\n" +
                "- WALLS: Create hollow walls around a rectangular area\n" +
                "- HOLLOW: Create a hollow rectangular frame\n" +
                "- REPLACE: Replace specific blocks with others\n" +
                "- SPHERE: Create spheres (solid or hollow)\n" +
                "- CYLINDER: Create cylinders (solid or hollow)\n" +
                "- PYRAMID: Create pyramids (solid or hollow)\n" +
                "- DOME: Create dome structures\n" +
                "- STRUCTURE: Create complex structures (towers, bridges, platforms, stairs)\n" +
                "- TERRAIN: Generate terrain features (hills, valleys, plateaus)\n\n" +
                "COORDINATE FORMAT:\n" +
                "Use \"pos1\" and \"pos2\" for rectangular operations, \"center\" for circular operations:\n" +
                "\"pos1\": {\"x\": 0, \"y\": 0, \"z\": 0}\n" +
                "\"pos2\": {\"x\": 10, \"y\": 5, \"z\": 10}\n" +
                "\"center\": {\"x\": 5, \"y\": 3, \"z\": 5}\n\n" +
                "MATERIAL PATTERNS:\n" +
                "- Single: \"stone\", \"grass\", \"cobblestone\"\n" +
                "- Percentage: \"70%stone,20%cobblestone,10%iron_ore\"\n" +
                "- Available: stone, grass, dirt, cobblestone, wood, sand, gravel, iron_ore, coal_ore,\n" +
                "  brick, obsidian, glass, wool, tnt, bookshelf, mossy_cobblestone, diamond_ore,\n" +
                "  ice, snow, clay, netherrack, glowstone, stone_brick, nether_brick, end_stone,\n" +
                "  quartz_block, prismarine, sea_lantern, magma, bone_block\n\n" +
                "Stained clay: red_clay, blue_clay, yellow_clay, green_clay, purple_clay, orange_clay, white_clay, black_clay, gray_clay, light_gray_clay, cyan_clay, pink_clay, lime_clay, magenta_clay, light_blue_clay, brown_clay\n" +
                "ARENA DESIGN PRINCIPLES:\n" +
                "- Arenas should be mostly flat for fair PvP combat\n" +
                "- Surround the arena with clear glass walls\n" +
                "- All decorative or structural elements must be placed outside the fighting area\n" +
                "- Use elevation changes, terrain, and detail behind the glass for visual and thematic effect\n" +
                "- Ensure clean sight lines and minimal obstructions inside the arena\n\n" +
                "EXAMPLE STRUCTURE OPERATIONS:\n" +

                "{\n" +
                "  \"type\": \"STRUCTURE\",\n" +
                "  \"structure_type\": \"tower\",\n" +
                "  \"position\": {\"x\": 0, \"y\": 0, \"z\": 0},\n" +
                "  \"material\": \"stone_brick\",\n" +
                "  \"height\": 15,\n" +
                "  \"radius\": 4\n" +
                "}\n\n" +
                "{\n" +
                "  \"type\": \"TERRAIN\",\n" +
                "  \"terrain_type\": \"hills\",\n" +
                "  \"center\": {\"x\": 0, \"y\": 0, \"z\": 0},\n" +
                "  \"material\": \"60%grass,30%dirt,10%stone\",\n" +
                "  \"size\": 20,\n" +
                "  \"height\": 8\n" +
                "}\n\n" +
                "Remember: Return ONLY the JSON array - no explanations, no markdown blocks, no extra text!";
    }

    private String buildUserPrompt(String userPrompt, String dimensions) {
        return String.format("Create a detailed PvP arena based on this description: \"%s\"\n\n" +
                "Arena Dimensions: %s\n\n" +
                "REQUIREMENTS:\n" +
                "- Build a competitive PvP arena designed for 1v1 or small team fights\n" +
                "- Use a flat combat area enclosed by glass walls\n" +
                "- Place all structures and terrain features outside the arena walls\n" +
                "- Make use of elevation and detail outside the arena for atmosphere\n" +
                "- Use appropriate and varied materials for aesthetics\n" +
                "- Ensure the arena is symmetrical, balanced, and fair\n" +
                "- Add visual polish without interfering with gameplay\n\n" +
                "Generate a comprehensive JSON build script with 15–25 operations that builds this arena cleanly.\n" +
                "Focus on creating an engaging, strategic combat environment.\n\n" +
                "JSON OUTPUT:", userPrompt, dimensions);
    }


    private String pollForResult(String pollingUrl) throws Exception {
        while (true) {
            URL url = new URL(pollingUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Token " + this.apiToken);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Polling failed with status code " + responseCode + ": " + readResponse(connection));
            }

            String responseBody = readResponse(connection);
            JsonObject jsonResponse = new JsonParser().parse(responseBody).getAsJsonObject();
            String status = jsonResponse.get("status").getAsString();

            switch (status) {
                case "succeeded":
                    return String.join("", gson.fromJson(jsonResponse.get("output"), List.class));
                case "failed":
                case "canceled":
                    String error = jsonResponse.get("error").getAsString();
                    throw new RuntimeException("Prediction failed or was canceled: " + error);
                case "starting":
                case "processing":
                    TimeUnit.SECONDS.sleep(2);
                    break;
                default:
                    throw new RuntimeException("Unknown prediction status: " + status);
            }
        }
    }

    private String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private <T> CompletableFuture<T> exceptionallyCompletedFuture(Throwable throwable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(throwable);
        return future;
    }
}