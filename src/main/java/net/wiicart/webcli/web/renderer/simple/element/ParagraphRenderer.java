package net.wiicart.webcli.web.renderer.simple.element;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

final class ParagraphRenderer extends AbstractElementRenderer {

    private final Element element;

    int columns = 60; // todo: Make dynamic

    ParagraphRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        super(screen, element);
        this.element = element;
    }

    @Override
    public @NotNull Component getContent() {
        final Panel panel = new Panel();
        final String text = element.ownText().strip().trim();
        final List<String> lines = getLines(text);

        TextBox box = getTextBox(lines.size());
        for(String line : lines) {
            box.addLine(line);
        }

        panel.addComponent(box);
        super.includeChildren(panel);

        return panel;
    }

    private @NotNull List<String> getLines(@NotNull String text) {
        final List<String> lines = new ArrayList<>();
        final String[] words = text.split(" ");

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if(builder.length() + word.length() + 1 < columns) {
                if(!builder.isEmpty()) {
                    builder.append(" ");
                }

                builder.append(word);
            } else {
                lines.add(builder.toString());
                builder = new StringBuilder(word + " ");
            }
        }

        if(!builder.isEmpty()) {
            lines.add(builder.toString());
        }

        return lines;
    }

    private @NotNull TextBox getTextBox(int rows) {
        final TextBox box = new TextBox(new TerminalSize(60, rows), TextBox.Style.MULTI_LINE);
        box.setCaretWarp(true);
        return box;
    }
}
