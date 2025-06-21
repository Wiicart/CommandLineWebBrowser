package net.wiicart.webcli.web.renderer.primitivetext;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.List;

final class CodeRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    @Override
    public @NotNull List<String> getContent(@NotNull Element element) {
        return List.of();
    }
}
