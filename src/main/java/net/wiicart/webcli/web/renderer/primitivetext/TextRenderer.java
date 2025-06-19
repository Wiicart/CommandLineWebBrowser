package net.wiicart.webcli.web.renderer.primitivetext;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

final class TextRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    private static final String START_AND_END = "-".repeat(PrimitiveTextBoxRenderer.ROW_SIZE);

    @Override
    public @NotNull List<String> getContent(@NotNull Element element) {
        String text = element.ownText().strip().trim();

        if(text.isEmpty()) {
            return List.of();
        }

        List<String> list = new ArrayList<>();
        list.add(START_AND_END);

        StringBuilder builder = new StringBuilder();
        String[] words = text.split(" ");
        for(String word : words) {
            if(builder.length() + word.length() < PrimitiveTextBoxRenderer.ROW_SIZE) {
                builder.append(word);
                builder.append(" ");
            } else {
                list.add(builder.toString());
                builder = new StringBuilder(word + " ");
            }
        }

        if(!builder.isEmpty()) {
            list.add(builder.toString());
        }

        list.add(START_AND_END);

        return list;
    }
}
