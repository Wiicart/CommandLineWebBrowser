package net.wiicart.webcli.web.renderer.simple.element;

import com.googlecode.lanterna.gui2.Component;
import net.wiicart.webcli.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

public interface ElementRenderer {

    static @NotNull ElementRenderer getAppropriate(@NotNull PrimaryScreen screen, @NotNull Element element) {
        return switch (element.tagName()) {
            case "h1", "h2", "h3", "h4", "h5", "h6" -> new HeadingRenderer(screen, element);
            case "a" -> new HyperlinkRenderer(screen, element);
            default -> new UnimplementedRenderer(screen, element);
        };
    }

    @NotNull Component getContent();

}
