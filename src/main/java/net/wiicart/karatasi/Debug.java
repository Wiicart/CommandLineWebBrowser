package net.wiicart.karatasi;

import org.jetbrains.annotations.NotNull;

public final class Debug {

    private Debug() {}

    private static boolean printing = false;

    static void enablePrinting() {
        printing = true;
    }

    public static void log(@NotNull String message) {
        if (printing) {
            System.out.println(message);
        }
    }

    public static void log(@NotNull Object object) {
        log(object.toString());
    }

}
