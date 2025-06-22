package net.wiicart.webcli;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import net.wiicart.webcli.screen.WebPageScreen;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CLIWebBrowser {

    public static void main( String args[]) {
        for(String arg : args) {
            if(arg.equalsIgnoreCase("--debug")) {
                Debug.enablePrinting();
            }
        }

        new CLIWebBrowser();
    }

    private final DefaultTerminalFactory factory = new DefaultTerminalFactory();

    private CLIWebBrowser() {
        try (final Terminal terminal = createTerminalAndInit()) {
            final Screen screen = createScreen();
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

            new WebPageScreen(gui).show();

            // shutdown
            terminal.exitPrivateMode();
            terminal.flush();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull Terminal createTerminalAndInit() {
        try {
            Terminal terminal = factory.createTerminal();
            terminal.enterPrivateMode();

            return terminal;
        } catch (final IOException e) {
            throw new RuntimeException("An error occurred while initializing the terminal.", e);
        }
    }

    private @NotNull Screen createScreen() {
        try {
            Screen screen = factory.createScreen();
            screen.startScreen();
            return screen;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
