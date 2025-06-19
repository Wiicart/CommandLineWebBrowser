package net.wiicart.webcli.screen.helper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class UnreachablePage {

    private UnreachablePage() {}

    private static final List<String> lines = new ArrayList<>();
    static {
        lines.add("$$$$$$$$\\ $$$$$$$\\  $$$$$$$\\   $$$$$$\\  $$$$$$$\\  ");
        lines.add("$$  _____|$$  __$$\\ $$  __$$\\ $$  __$$\\ $$  __$$\\ ");
        lines.add("$$ |      $$ |  $$ |$$ |  $$ |$$ /  $$ |$$ |  $$ |");
        lines.add("$$$$$\\    $$$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$  |");
        lines.add("$$  __|   $$  __$$< $$  __$$< $$ |  $$ |$$  __$$< ");
        lines.add("$$ |      $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |  $$ |");
        lines.add("$$$$$$$$\\ $$ |  $$ |$$ |  $$ | $$$$$$  |$$ |  $$ |");
        lines.add("\\________|\\__|  \\__|\\__|  \\__| \\______/ \\__|  \\__|");
        lines.add("");
        lines.add("Unable to reach page");
        lines.add("status: {status}");
    }
    public static @NotNull List<String> withCode(int code) {
        List<String> list = new ArrayList<>(lines);
        String pos = list.get(10);
        list.set(10, pos.replace("{status}", String.valueOf(code)));
        return list;
    }

}
