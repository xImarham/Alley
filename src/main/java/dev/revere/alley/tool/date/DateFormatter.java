package dev.revere.alley.tool.date;

import dev.revere.alley.tool.date.enums.EnumDateFormat;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Emmy
 * @project Alley
 * @since 05/04/2025
 */
@Getter
public class DateFormatter {
    private final SimpleDateFormat dateFormat;
    private final Date date;

    /**
     * Constructor for the DateFormatter class.
     *
     * @param dateFormat The date format to use.
     * @param time The time in milliseconds since the epoch.
     */
    public DateFormatter(EnumDateFormat dateFormat, long time) {
        this.date = new Date(time);
        this.dateFormat = new SimpleDateFormat(dateFormat.getFormat());
        this.dateFormat.format(this.date);
    }

    /**
     * Get the formatted date in a fancy and readable format (e.g., "1st of December, 2024").
     *
     * @param primaryColor The primary color for the date.
     * @param secondaryColor The secondary color for the date.
     * @return The formatted readable date.
     */
    public String setFancy(ChatColor primaryColor, ChatColor secondaryColor) {
        this.dateFormat.applyPattern("dd MMMM yyyy");
        String formattedDate = this.dateFormat.format(this.date);

        String[] parts = formattedDate.split(" ");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];

        String dayWithSuffix = this.addOrdinalSuffix(Integer.parseInt(day));

        return primaryColor + dayWithSuffix + secondaryColor + " of " + primaryColor + month + secondaryColor + ", " + primaryColor + year;
    }

    /**
     * Add the appropriate ordinal suffix (st, nd, rd, th) to the given day.
     *
     * @param day The day to which the suffix should be added.
     * @return The day with the ordinal suffix.
     */
    private String addOrdinalSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:  return day + "st";
            case 2:  return day + "nd";
            case 3:  return day + "rd";
            default: return day + "th";
        }
    }
}