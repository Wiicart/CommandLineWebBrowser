package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.screen.WebPageScreen;
import net.wiicart.webcli.util.ImageConverterUtil;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

class DirectImageHandler implements Handler {

    private final WebPageScreen screen;

    private final String address;

    private final String title;

    private String content;

    DirectImageHandler(@NotNull String address, @NotNull WebPageScreen screen) throws LoadFailureException {
        this.address = address;
        this.title = StringUtils.getFileName(address);
        this.screen = screen;
        load();
    }


    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(box);

        box.addLine(content);
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    private void load() throws LoadFailureException {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(URLUtil.normalizeURL(address)));
            content = ImageConverterUtil.imageToString(bufferedImage, screen.getRowCount() - 5);
        } catch(Exception e) {
            throw new LoadFailureException(-501, e);
        }
    }
}
