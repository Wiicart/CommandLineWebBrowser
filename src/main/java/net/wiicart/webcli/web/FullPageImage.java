package net.wiicart.webcli.web;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.Debug;
import net.wiicart.webcli.screen.WebPageScreen;
import net.wiicart.webcli.util.ImageConverterUtil;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.destination.external.ExternalDestination;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public final class FullPageImage extends ExternalDestination {

    private final String src;

    private final WebPageScreen screen;

    private String content;

    public FullPageImage(@NotNull WebPageScreen screen, String src) {
        super(src, screen);
        this.screen = screen;
        this.src = src;
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
    public void load(@Nullable Progress<Connection.Response> progress) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(URLUtil.normalizeURL(src)));
            content = ImageConverterUtil.imageToString(bufferedImage, screen.getRowCount() - 5);
        } catch(Exception e) {
            content = "Failed to load image: " + src + "\n" + e.getMessage();
            Debug.log(e.getCause());
        }
    }
}
