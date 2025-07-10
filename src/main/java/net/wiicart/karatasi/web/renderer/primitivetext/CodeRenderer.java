package net.wiicart.karatasi.web.renderer.primitivetext;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

// inserts ">" at the start of the line. wraps w/PrimitiveTextBoxRenderer constant.
final class CodeRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    @Override
    public @NotNull List<String> getContent(@NotNull Element element) {
        String text = element.ownText().strip().trim();
        if(text.isBlank()) {
            return List.of();
        }

        final List<String> list = new ArrayList<>();

        StringBuilder builder = new StringBuilder("> ");
        String[] words = text.split(" ");
        for(String word : words) {
            if(builder.length() + word.length() < PrimitiveTextBoxRenderer.ROW_SIZE) {
                builder.append(word);
            } else {
                list.add(builder.toString());
                builder = new StringBuilder("> " + word);
            }
        }

        if(!builder.isEmpty()) {
            list.add(builder.toString());
        }

        return list;
    }
}
