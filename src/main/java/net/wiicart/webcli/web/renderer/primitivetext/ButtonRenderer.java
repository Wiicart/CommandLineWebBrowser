package net.wiicart.webcli.web.renderer.primitivetext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jsoup.nodes.Element;

import java.util.List;

final class ButtonRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    private static final String FORMAT = "[BUTTON {data}]";

    @Override
    public @NotNull @Unmodifiable List<String> getContent(@NotNull Element element) {
        String string = getAttributes(element);
        if(!string.isEmpty()) {
            return List.of(FORMAT.replace("{data}", string));
        }

        return List.of("[BUTTON]");
    }

    private @NotNull String getAttributes(@NotNull Element element) {
        StringBuilder builder = new StringBuilder();

        String name = element.attr("name");
        if(!name.isEmpty()) {
            builder.append("name=\"").append(name).append("\"");
        }

        String type = element.attr("type");
        if(!type.isEmpty()) {
            builder.append("type=\"").append(type).append("\"");
        }

        String value = element.attr("text");
        if(!value.isEmpty()) {
            builder.append("text=\"").append(value).append("\"");
        }

        String text = element.attr("text");
        if(!text.isEmpty()) {
            builder.append("text=\"").append(text).append("\"");
        }

        return builder.toString();
    }
}
