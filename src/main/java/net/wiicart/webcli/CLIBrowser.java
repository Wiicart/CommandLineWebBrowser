package net.wiicart.webcli;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import net.wiicart.webcli.config.Configuration;
import net.wiicart.webcli.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CLIBrowser {

    public static void main(String args[]) {
        for(String arg : args) {
            if(arg.equalsIgnoreCase("--debug")) {
                Debug.enablePrinting();
            }
        }

        new CLIBrowser();
    }

    private final DefaultTerminalFactory factory = new DefaultTerminalFactory();

    private final Configuration config;

    private final PrimaryScreen screen;

    private final Screen lanternaScreen;

    private CLIBrowser() {
        config = new Configuration();
        try (final Terminal terminal = createTerminalAndInit()) {
            lanternaScreen = createScreen();
            screen = new PrimaryScreen(this, new MultiWindowTextGUI(lanternaScreen));
            // shutdown
            terminal.exitPrivateMode();
            terminal.flush();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration config() {
        return config;
    }

    public PrimaryScreen screen() {
        return screen;
    }

    public Screen lanternaScreen() {
        return lanternaScreen;
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
            Screen s = factory.createScreen();
            s.startScreen();
            return s;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
