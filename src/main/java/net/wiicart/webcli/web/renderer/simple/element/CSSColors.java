package net.wiicart.webcli.web.renderer.simple.element;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class CSSColors {

    private CSSColors() {}

    private static final Map<String, String> COLORS;
    static {
        COLORS = new HashMap<>();

    }

    public static String getHex(@NotNull String color) {
        return COLORS.get(color.toLowerCase(Locale.ROOT));
    }

}
