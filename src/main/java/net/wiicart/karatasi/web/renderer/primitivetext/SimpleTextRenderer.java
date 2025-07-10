package net.wiicart.karatasi.web.renderer.primitivetext;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

final class SimpleTextRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    @Override
    public @NotNull List<String> getContent(@NotNull Element element) {
        final String text = element.ownText().strip().trim();
        if(text.isEmpty()) {
            return List.of();
        }

        final ArrayList<String> result = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        String[] words = text.split(" ");
        for (String word : words) {
            if(builder.length() + word.length() < PrimitiveTextBoxRenderer.ROW_SIZE) {
                builder.append(word);
                builder.append(" ");
            } else {
                result.add(builder.toString());
                builder = new StringBuilder(word);
            }
        }

        if(!builder.isEmpty()) {
            result.add(builder.toString());
        }

        return result;
    }
}
