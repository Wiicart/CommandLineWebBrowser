package net.wiicart.karatasi.web.renderer.simple.element;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import net.wiicart.karatasi.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

// TODO, support paths such as "/ex/page.html", which implies same site
final class HyperlinkRenderer extends AbstractElementRenderer {

    private final PrimaryScreen screen;

    private final Element element;

    HyperlinkRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        super(screen, element);
        this.screen = screen;
        this.element = element;
    }

    @Override
    public @NotNull Component getContent() {
        final String text = element.ownText().strip().trim();
        final String href = element.attr("href");

        if(href.isBlank() || href.equals("#")) {
            return new Button(text);
        } else {
            return new Button(text, () -> {
                screen.getGui().getActiveWindow().setFocusedInteractable(null);
                screen.goToAddress(href, true);
            });
        }
    }
}
