package net.wiicart.karatasi.web.renderer.simple.element;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

abstract class AbstractElementRenderer implements ElementRenderer {

    private final PrimaryScreen screen;

    private final Element element;

    protected AbstractElementRenderer(@NotNull PrimaryScreen screen, @NotNull Element element) {
        this.screen = screen;
        this.element = element;
    }

    /**
     * Adds all children to the current Element's Panel.
     * @param panel The Panel to add the children too.
     */
    protected void includeChildren(@NotNull Panel panel) {
        for(Element child : element.children()) {
            panel.addComponent(ElementRenderer.getAppropriate(screen, child).getContent());
        }
    }

    /**
     * Provides the Color from an attribute.
     * @param color The color itself, such as "red", #FFFFFF, or rgb(1,1,1)
     * @return The TextColor equivalent
     */
    protected @NotNull TextColor getColor(@NotNull String color) {
        return TextColor.ANSI.GREEN;
    }
}
