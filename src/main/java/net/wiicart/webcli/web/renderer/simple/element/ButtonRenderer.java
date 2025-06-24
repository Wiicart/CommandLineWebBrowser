package net.wiicart.webcli.web.renderer.simple.element;

import com.googlecode.lanterna.gui2.Component;
import net.wiicart.webcli.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

final class ButtonRenderer extends AbstractElementRenderer {

    protected ButtonRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        super(screen, element);
    }

    @Override
    public @NotNull Component getContent() {
        return null;
    }
}
