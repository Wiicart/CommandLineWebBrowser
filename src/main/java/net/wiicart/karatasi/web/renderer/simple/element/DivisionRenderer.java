package net.wiicart.karatasi.web.renderer.simple.element;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

final class DivisionRenderer extends AbstractElementRenderer {

    private final Element element;

    DivisionRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        super(screen, element);
        this.element = element;
    }

    @Override
    public @NotNull Component getContent() {
        Panel panel = new Panel();
        final String text = element.ownText().strip().trim();
        panel.addComponent(new Label(text));
        super.includeChildren(panel);

        return panel;
    }
}
