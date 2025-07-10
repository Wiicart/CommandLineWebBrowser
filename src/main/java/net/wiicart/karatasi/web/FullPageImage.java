package net.wiicart.karatasi.web;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.PrimaryScreen;
import net.wiicart.karatasi.util.ImageConverterUtil;
import net.wiicart.karatasi.util.StringUtils;
import net.wiicart.karatasi.util.URLUtil;
import net.wiicart.karatasi.web.destination.AbstractDestination;
import net.wiicart.karatasi.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

@Deprecated
public final class FullPageImage extends AbstractDestination {

    private final String src;

    private final PrimaryScreen screen;

    private String content;

    public FullPageImage(@NotNull PrimaryScreen screen, String src) throws LoadFailureException {
        this.screen = screen;
        this.src = src;
        load();
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(box);
        for(String string : StringUtils.convertToListByNewLine(content)) {
            box.addLine(string);
        }
    }

    @Override
    public @NotNull String getTitle() {
        return "";
    }

    private void load() throws LoadFailureException {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(URLUtil.normalizeURL(src)));
            content = ImageConverterUtil.imageToString(bufferedImage, screen.getRowCount() - 5);
        } catch(Exception e) {
            throw new LoadFailureException(-501, e);
        }
    }
}
