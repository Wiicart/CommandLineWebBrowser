package net.wiicart.webcli.web.renderer.primitivetext;

import io.korhner.asciimg.image.AsciiImgCache;
import io.korhner.asciimg.image.character_fit_strategy.StructuralSimilarityFitStrategy;
import io.korhner.asciimg.image.converter.AsciiToStringConverter;
import net.wiicart.webcli.util.ImageConverterUtil;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.util.URLUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

final class ImageASCIIRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    private final ImageRenderer fallbackRenderer = new ImageRenderer();

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<String> getContent(@NotNull Element element) {
        String src = element.attr("src");
        try {
            final BufferedImage bufferedImage = ImageIO.read(new URL(URLUtil.normalizeURL(src)));
            return ImageConverterUtil.imageToList(bufferedImage, 30);
        } catch(Exception ignored) {}

        return fallbackRenderer.getContent(element);
    }

    private @NotNull String toAsciiString(@NotNull final BufferedImage bufferedImage) {
        return "";
    }

}
