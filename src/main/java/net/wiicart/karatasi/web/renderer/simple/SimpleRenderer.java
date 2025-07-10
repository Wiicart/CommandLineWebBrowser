package net.wiicart.karatasi.web.renderer.simple;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.screen.PrimaryScreen;
import net.wiicart.karatasi.web.renderer.Renderer;
import net.wiicart.karatasi.web.renderer.simple.element.ElementRenderer;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import org.jsoup.nodes.Document;

public final class SimpleRenderer implements Renderer {

    private final PrimaryScreen screen;

    private final Document document;

    public SimpleRenderer(@NotNull PrimaryScreen screen, @NotNull Document document) {
        this.screen = screen;
        this.document = document;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        // panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        for(Element element : document.body().children()) {
            panel.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT));
            panel.addComponent(
                    ElementRenderer.getAppropriate(screen, element).getContent()
            );
        }
    }
}
