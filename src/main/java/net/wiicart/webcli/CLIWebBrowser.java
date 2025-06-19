package net.wiicart.webcli;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import net.wiicart.webcli.screen.URLEntryScreen;
import net.wiicart.webcli.screen.WebPageScreen;
import net.wiicart.webcli.web.WebPage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CLIWebBrowser {

    private boolean active;
    private final MultiWindowTextGUI gui;

    public CLIWebBrowser() {
        try (Terminal terminal = createTerminalAndInit()){
            terminal.setBackgroundColor(TextColor.ANSI.RED);
            Screen screen = createScreen();
            screen.startScreen();
            gui = new MultiWindowTextGUI(screen);
            WebPageScreen screen1 = new WebPageScreen(gui);
            screen1.goToAddress("https://www.wiicart.net/clbrowser");
            screen1.show();
            terminal.exitPrivateMode();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO make escapable
    private void loadUrlEntryScreen(MultiWindowTextGUI gui) {
        URLEntryScreen s = new URLEntryScreen(gui);
        s.show()
                .then(() -> MessageDialog.showMessageDialog(gui, "Going to URL", s.getEntry()))
                .then(() -> System.out.println(s.getEntry()))
                .then(() -> {
                    try {
                        WebPage.fromAddress(s.getEntry());
                        MessageDialog.showMessageDialog(gui, "Connected to URL successfully", s.getEntry());
                    } catch(Exception e) {
                        MessageDialog.showMessageDialog(gui, "Couldn't reach URL", s.getEntry());
                    }
                })
                .then(() -> loadUrlEntryScreen(gui));
    }

    private @NotNull Terminal createTerminalAndInit() {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            Terminal terminal = factory.createTerminal();
            terminal.enterPrivateMode();
            terminal.setBackgroundColor(TextColor.ANSI.YELLOW_BRIGHT);
            return terminal;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull Screen createScreen() {
        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            return factory.createScreen();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}
