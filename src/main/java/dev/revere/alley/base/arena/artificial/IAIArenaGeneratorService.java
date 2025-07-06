package dev.revere.alley.base.arena.artificial;

import dev.revere.alley.core.lifecycle.IService;

import java.util.concurrent.CompletableFuture;

/**
 * @author Remi
 * @project alley-practice
 * @date 6/07/2025
 */
public interface IAIArenaGeneratorService extends IService {
    /**
     * Takes a user prompt, communicates with the AI model, and returns a structured build script.
     * This process is asynchronous.
     *
     * @param userPrompt The natural language prompt from the user.
     * @param dimensions The desired size (e.g., 100x100).
     * @return A CompletableFuture containing the JSON build script as a String.
     */
    CompletableFuture<String> generateBuildScript(String userPrompt, String dimensions);
}