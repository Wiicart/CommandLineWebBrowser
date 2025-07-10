package net.wiicart.karatasi.web.renderer.primitivetext;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jsoup.nodes.Element;

import java.util.List;

final class StrongRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {


    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<String> getContent(@NotNull Element element) {
        String text = element.ownText();
        return List.of("=" + text + "=");
    }
}
