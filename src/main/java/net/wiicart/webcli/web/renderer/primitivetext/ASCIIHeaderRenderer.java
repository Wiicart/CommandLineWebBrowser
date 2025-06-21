package net.wiicart.webcli.web.renderer.primitivetext;

import com.github.lalyos.jfiglet.FigletFont;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

final class ASCIIHeaderRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    @Override
    public @NotNull List<String> getContent(@NotNull Element element) {
        String text = element.ownText().strip().trim();
        if(text.isBlank()) {
            return List.of();
        }

        try {
            String ascii = FigletFont.convertOneLine(text);
            String[] lines = ascii.split("\n");
            return Arrays.asList(lines);
        } catch (Exception e) {
            return new SingleLineHeaderRenderer().getContent(element);
        }
    }
}
