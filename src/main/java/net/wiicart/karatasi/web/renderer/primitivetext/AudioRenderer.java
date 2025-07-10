package net.wiicart.karatasi.web.renderer.primitivetext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jsoup.nodes.Element;

import java.util.List;

final class AudioRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    @Override
    public @NotNull @Unmodifiable List<String> getContent(@NotNull Element element) {
        return List.of();
    }
}
