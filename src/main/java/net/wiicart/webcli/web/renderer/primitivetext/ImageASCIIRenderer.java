package net.wiicart.webcli.web.renderer.primitivetext;

import net.wiicart.webcli.util.ImageConverterUtil;
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

    private static final int DEFAULT_HEIGHT = 30;

    private final ImageRenderer fallbackRenderer = new ImageRenderer();

    @Contract(pure = true)
    @Override
    public @NotNull @Unmodifiable List<String> getContent(@NotNull Element element) {
        final String src = element.attr("src");
        int height = getHeight(element);

        try {
            final BufferedImage bufferedImage = ImageIO.read(new URL(URLUtil.normalizeURL(src)));
            return ImageConverterUtil.imageToList(bufferedImage, height);
        } catch(Exception ignored) {}

        return fallbackRenderer.getContent(element);
    }

    private int getHeight(@NotNull Element element) {
        String heightString = element.attr("height");
        if(isNumber(heightString)) {
            return Integer.parseInt(heightString);
        }

        if(heightString.endsWith("px")) {
            return handlePx(heightString);
        }

        return DEFAULT_HEIGHT;
    }

    private boolean isNumber(@NotNull String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    // Input should always be <NUM>px
    private int handlePx(@NotNull String string) {
        try {
            String stripped = string.substring(0, string.length() - 2);
            int px = Integer.parseInt(stripped);

            return Math.min(px, 50);

        } catch(Exception ignored) {}

        return DEFAULT_HEIGHT;
    }

}
