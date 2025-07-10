package net.wiicart.karatasi.web.renderer.simple.element;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

final class UnimplementedRenderer extends AbstractElementRenderer {

    private final Element element;

    UnimplementedRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        super(screen, element);
        this.element = element;
    }

    @Override
    public @NotNull Panel getContent() {
        Panel panel = new Panel();
        panel.withBorder(Borders.singleLine(element.tagName()));
        panel.addComponent(new Label(element.ownText()));
        super.includeChildren(panel);

        return panel;
    }
}
