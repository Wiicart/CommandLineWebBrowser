package net.wiicart.karatasi.web.renderer.simple.element;

import com.googlecode.lanterna.gui2.Component;
import net.wiicart.karatasi.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

public interface ElementRenderer {

    static @NotNull ElementRenderer getAppropriate(@NotNull PrimaryScreen screen, @NotNull Element element) {
        System.out.println(element.tagName());
        System.out.println(element.ownText());
        return switch (element.tagName()) {
            case "h1", "h2", "h3", "h4", "h5", "h6", "header" -> new HeadingRenderer(screen, element);
            case "a" -> new HyperlinkRenderer(screen, element);
            case "p" -> new ParagraphRenderer(screen, element);
            case "div" -> new DivisionRenderer(screen, element);
            default -> new UnimplementedRenderer(screen, element);
        };
    }

    @NotNull Component getContent();

}
