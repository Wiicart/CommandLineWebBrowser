package net.wiicart.karatasi.util;

import io.korhner.asciimg.image.AsciiImgCache;
import io.korhner.asciimg.image.character_fit_strategy.StructuralSimilarityFitStrategy;
import io.korhner.asciimg.image.converter.AsciiToStringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

public final class ImageConverterUtil {

    private ImageConverterUtil() {}

    private static final double FONT_SCALING_FACTOR = 1556.0 / (10.0 * 129);

    /**
     * Converts an image to an ASCII art representation
     * @param image The BufferedImage
     * @param height The desired height of the ASCII art
     * @return A String
     */
    public static @NotNull String imageToString(@NotNull final BufferedImage image, int height) {
        int imgHeight = image.getHeight();
        AsciiImgCache cache = AsciiImgCache.create(
                new Font("Courier", Font.BOLD, calculateFontSize(imgHeight, height)));
        AsciiToStringConverter converter = new AsciiToStringConverter(cache, new StructuralSimilarityFitStrategy());

        return converter.convertImage(image).toString();
    }

    /**
     * Converts an image to an ASCII art representation
     * @param image The BufferedImage
     * @param height The desired height of the ASCII art
     * @return A List
     */
    public static @NotNull @Unmodifiable List<String> imageToList(@NotNull final BufferedImage image, int height) {
        return StringUtils.convertToListByNewLine(imageToString(image, height));
    }

    // Calculate what font size to use to generate the desired img size
    private static int calculateFontSize(int imgHeight, int newHeight) {
        return (int) Math.round(imgHeight / (newHeight * FONT_SCALING_FACTOR));
    }
}
