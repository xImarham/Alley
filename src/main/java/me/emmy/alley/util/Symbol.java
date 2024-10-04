package me.emmy.alley.util;

import lombok.Getter;

/**
 * @author Emmy
 * @project Spigkit
 * @date 18/09/2024 - 12:51
 */
@Getter
public enum Symbol {
    ARROW_R("»"),
    ARROW_L("«"),
    STAR_FILLED("★"),
    STAR_EMPTY("☆"),
    HEART("❤"),
    TICK("✔"),
    CROSS("✘"),
    CROWN("♛"),
    WARNING("[⚠]"),
    QUESTION("❓"),
    EXCLAMATION("❗"),
    CHECK("✓"),
    X("✗"),
    FLAG("⚑"),
    SKULL("☠"),
    GEAR("⚙"),

    ;

    private final String symbol;

    /**
     * Constructor for the Symbol enum.
     *
     * @param symbol the symbol
     */
    Symbol(String symbol) {
        this.symbol = symbol;
    }
}