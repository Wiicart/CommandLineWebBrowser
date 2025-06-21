package net.wiicart.webcli.web.renderer.primitivetext;

import io.korhner.asciimg.image.AsciiImgCache;
import io.korhner.asciimg.image.character_fit_strategy.StructuralSimilarityFitStrategy;
import io.korhner.asciimg.image.converter.AsciiToStringConverter;
import net.wiicart.webcli.util.URLUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

final class ImageASCIIRenderer implements PrimitiveTextBoxRenderer.ElementRenderer {

    private final ImageRenderer renderer = new ImageRenderer();

    private final AsciiImgCache cache = AsciiImgCache.create(new Font("Courier",Font.BOLD, 6));

    private final AsciiToStringConverter converter = new AsciiToStringConverter(cache, new StructuralSimilarityFitStrategy());

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<String> getContent(@NotNull Element element) {
        String src = element.attr("src");
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(URLUtil.normalizeURL(src)));
            return List.of(converter.convertImage(bufferedImage).toString());
        } catch(Exception ignored) {

        }

        return renderer.getContent(element);
    }

}
