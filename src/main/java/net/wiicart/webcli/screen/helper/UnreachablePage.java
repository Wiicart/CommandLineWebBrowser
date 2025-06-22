package net.wiicart.webcli.screen.helper;

import net.wiicart.webcli.Debug;
import net.wiicart.webcli.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class UnreachablePage {

    private static final List<String> LINES = new ArrayList<>();
    static {
        LINES.add("$$$$$$$$\\ $$$$$$$\\  $$$$$$$\\   $$$$$$\\  $$$$$$$\\  ");
        LINES.add("$$  _____|$$  __$$\\ $$  __$$\\ $$  __$$\\ $$  __$$\\ ");
        LINES.add("$$ |      $$ |  $$ |$$ |  $$ |$$ /  $$ |$$ |  $$ |");
        LINES.add("$$$$$\\    $$$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$  |");
        LINES.add("$$  __|   $$  __$$< $$  __$$< $$ |  $$ |$$  __$$< ");
        LINES.add("$$ |      $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |  $$ |");
        LINES.add("$$$$$$$$\\ $$ |  $$ |$$ |  $$ | $$$$$$  |$$ |  $$ |");
        LINES.add("\\________|\\__|  \\__|\\__|  \\__| \\______/ \\__|  \\__|");
        LINES.add("");
        LINES.add("Unable to reach page");
        LINES.add("status: {status}");
    }

    private static final List<String> FOUR_ZERO_FOUR = new ArrayList<>();
    static {
        FOUR_ZERO_FOUR.add("________________________________________________                        ");
        FOUR_ZERO_FOUR.add("| 0000    0000   000000000000  0000    0000    |\\                      ");
        FOUR_ZERO_FOUR.add("| 0000    0000   0000    0000  0000    0000    | \\     _    _         ");
        FOUR_ZERO_FOUR.add("| 0000000000000000000    000000000000000000000 |  \\   | \\  | \\       ");
        FOUR_ZERO_FOUR.add("|         0000   0000    0000          0000    |   |--|  \\_|  \\_____  ");
        FOUR_ZERO_FOUR.add("|         0000   0000    0000          0000    |  /    \\____    _____\\");
        FOUR_ZERO_FOUR.add("|         0000   000000000000          0000    | /          |  /       ");
        FOUR_ZERO_FOUR.add("________________________________________________/           |_/        ");
        FOUR_ZERO_FOUR.add("               404 | Not Found                                         ");
        FOUR_ZERO_FOUR.add("                                                                       ");
    }

    private static final List<String> FOUR_ZERO_FOUR_2;
    static {
        List<String> list;
        try {
             list = ResourceManager.loadLocalResourceAsList("/unreachable/404.txt");
        } catch(Exception e) {
            Debug.log("Failed to load resource FOUR_ZERO_FOUR_2");
            list = FOUR_ZERO_FOUR;
        }

        FOUR_ZERO_FOUR_2 = list;
    }

    private static final List<String> UNKNOWN_HOST;
    static {
        List<String> list;
        try {
            list = ResourceManager.loadLocalResourceAsList("/unreachable/unknownhost.txt");
        } catch(Exception e) {
            Debug.log("Failed to load resource UNKNOWN_HOST");
            list = List.of("Unknown host");
        }

        UNKNOWN_HOST = list;
    }

    private UnreachablePage() {}

    public static @NotNull List<String> withCode(int code) {
        return switch (code) {
            case 404 -> {
                if(Math.random() * 2 < 1) {
                    yield FOUR_ZERO_FOUR_2;
                }
                yield FOUR_ZERO_FOUR;
            }

            case 700 -> UNKNOWN_HOST;

            default -> {
                List<String> list = new ArrayList<>(LINES);
                String pos = list.get(10);
                list.set(10, pos.replace("{status}", String.valueOf(code)));
                yield list;
            }
        };
    }

}
