package net.wiicart.webcli.web.destination.jar;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import net.wiicart.webcli.Debug;
import net.wiicart.webcli.config.Configuration;
import net.wiicart.webcli.screen.PrimaryScreen;
import net.wiicart.webcli.web.destination.Destination;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A simple editor for the program's config file, config.yml
 */
final class JarConfigurationHandler implements Destination.Handler {

    private final PrimaryScreen screen;

    private TextBox box;

    JarConfigurationHandler(@NotNull PrimaryScreen screen) {
        this.screen = screen;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(createButtonPanel());
        panel.addComponent(box);
        try (BufferedReader reader = new BufferedReader(new FileReader(Configuration.CONFIG_FILE))) {
            reader.lines().forEach(line -> box.addLine(line));
        } catch(IOException e) {
            box.addLine("Failed to load configuration file");
            box.addLine(e.getMessage());
        }

        box.setReadOnly(false);
    }

    @Override
    public @NotNull String getTitle() {
        return "Configuration";
    }

    // Creates a Panel w/ the save button in the middle
    private @NotNull Panel createButtonPanel() {
        final Panel panel = new Panel();
        panel.addComponent(new Button("Save", this::initSave), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        panel.setSize(new TerminalSize(screen.getColumnCount(), 1));
        panel.setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK));
        panel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center, LinearLayout.GrowPolicy.CanGrow));
        panel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

        return panel;
    }

    // Dialog confirms whether the user wants to save the File or not
    private void initSave() {
        MessageDialogButton dialog = new MessageDialogBuilder()
                .setTitle("Confirm Save")
                .addButton(MessageDialogButton.Yes)
                .addButton(MessageDialogButton.Cancel)
                .setText("Are you sure you want to save this file?")
                .build()
                .showDialog(screen.getGui());
        if (dialog == MessageDialogButton.Yes) {
            save();
        }
    }

    private void showErrorDialog() {
        MessageDialog.showMessageDialog(
                screen.getGui(),
                "Error",
                "Failed to save the configuration.",
                MessageDialogButton.OK);
    }

    private void showSuccessDialog() {
        MessageDialog.showMessageDialog(
                screen.getGui(),
                "Success",
                "Configuration saved successfully."
        );
    }

    private void save() {
        String newContent = box.getText();
        File file = Configuration.CONFIG_FILE;
        try(PrintWriter out = new PrintWriter(file)) {
            out.print(newContent);
            showSuccessDialog();
        } catch(FileNotFoundException e) {
            Debug.log("Failed to save config.yml");
            Debug.log(e.getMessage());
            showErrorDialog();
        }
    }
}
