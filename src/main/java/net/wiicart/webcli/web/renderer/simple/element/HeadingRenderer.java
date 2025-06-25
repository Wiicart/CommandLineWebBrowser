package net.wiicart.webcli.web.renderer.simple.element;

import com.github.lalyos.jfiglet.FigletFont;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

final class HeadingRenderer extends AbstractElementRenderer {

    private final PrimaryScreen screen;

    private final Element element;

    public HeadingRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        super(screen, element);
        this.screen = screen;
        this.element = element;
    }

    @Override
    public @NotNull Component getContent() {
        final Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        panel.setSize(new TerminalSize(100, 100));
        String text = element.ownText().strip().trim();
        if(text.isBlank()) {
            super.includeChildren(panel);
            return panel;
        }

        TextBox box;
        try {
            final String figlet = FigletFont.convertOneLine(text);
            box = new TextBox(new TerminalSize(screen.getColumnCount(), 6), TextBox.Style.MULTI_LINE) // or estimate from line count
                    .setReadOnly(true)
                    .setVerticalFocusSwitching(true)
                    .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));

            box.setText(figlet);
        } catch (Exception e) {
            box = new TextBox().setReadOnly(true)
                    .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
            box.setText(text);
        }
        panel.addComponent(box);

        super.includeChildren(panel);

        return panel;
    }

}
