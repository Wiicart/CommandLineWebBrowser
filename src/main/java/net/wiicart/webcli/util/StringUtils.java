package net.wiicart.webcli.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * String utility methods
 */
public final class StringUtils {

    private StringUtils() {}

    /* Applies a box around a List of Strings, and returns an updated List.
     * There should be one space for padding on all sides.
     * Example:
     * +-------------------+
     * |                   |
     * |                   |
     * +-------------------+
     * */
    public static @NotNull List<String> box(@NotNull final List<String> content) {
        if(listIsBlank(content)) {
            return List.of();
        }

        final List<String> result = new ArrayList<>();
        final int longest = findLongestLength(content);
        final String ends = "+" + "-".repeat(longest + 2) + "+";
        result.add(ends);

        for(String str : content) {
            String blankSpace = " ".repeat(longest - str.length());
            String rep = "| " + str + blankSpace + " |";
            result.add(rep);
        }
        result.add(ends);

        return result;
    }

    public static boolean listIsBlank(@NotNull List<String> content) {
        if(content.isEmpty()) {
            return true;
        }

        for(String str : content) {
            if(!str.isBlank()) {
                return false;
            }
        }

        return true;
    }

    private static int findLongestLength(@NotNull final List<String> content) {
        int longest = 0;
        for(String string : content) {
            if(string.length() > longest) {
                longest = string.length();
            }
        }

        return longest;
    }

}
