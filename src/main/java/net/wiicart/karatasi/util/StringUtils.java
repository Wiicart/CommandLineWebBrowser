package net.wiicart.karatasi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static @NotNull @Unmodifiable List<String> box(@NotNull final List<String> content) {
        if(checkIfListIsBlank(content)) {
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

    public static boolean checkIfListIsBlank(@NotNull List<String> content) {
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

    public static int findLongestLength(@NotNull final List<String> content) {
        int longest = 0;
        for(String string : content) {
            if(string.length() > longest) {
                longest = string.length();
            }
        }

        return longest;
    }

    public static @NotNull @Unmodifiable List<String> convertToListByNewLine(@NotNull String string) {
        final String[] lines = string.split("\n");
        return Arrays.asList(lines);
    }

    public static @NotNull String convertListToString(@NotNull List<String> list) {
        StringBuilder builder = new StringBuilder();
        for(String str : list) {
            builder.append(str);
        }

        return builder.toString();
    }

    public static @NotNull List<String> modifiableCopy(@NotNull final List<String> list) {
        return new ArrayList<>(list);
    }

    public static @NotNull String getFileName(@NotNull final String address) {
        return address.substring(address.lastIndexOf('/') + 1);
    }

}
