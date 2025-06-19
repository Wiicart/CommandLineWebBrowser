package net.wiicart.webcli.web.renderer;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

// Fully text-based renderer that renders all text in to a single TextBox.
public class PrimitiveTextBoxRenderer implements Renderer {

    private static final Set<String> HEADERS = Set.of("h1", "h2", "h3", "h4", "h5", "h6");

    private final Document document;

    public PrimitiveTextBoxRenderer(@NotNull Document document) {
        this.document = document;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox textBox = generateFullBodyTextBox();
        textBox.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT));
        Iterator<Element> it = document.body().stream().iterator();
        while (it.hasNext()) {
            Element element = it.next();
            String text = element.ownText().strip().trim();

            if(isHeader(element.tagName())) {
                text = "=" + text + "=";
                textBox.addLine("");
                textBox.addLine(text);
                textBox.addLine("");
                continue;
            }

            if(!text.isEmpty()) {
                textBox.addLine(text);
                textBox.addLine("");
            }

        }
        panel.addComponent(textBox);
    }

    public static @NotNull TextBox generateFullBodyTextBox() {
        TextBox box = new TextBox(new TerminalSize(100, 100), TextBox.Style.MULTI_LINE);
        box.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        box.setSize(new TerminalSize(100, 100));
        box.setTheme(new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        box.setReadOnly(true);
        return box;
    }

    private boolean isHeader(String tag) {
        return HEADERS.contains(tag.toLowerCase(Locale.ROOT));
    }
}
