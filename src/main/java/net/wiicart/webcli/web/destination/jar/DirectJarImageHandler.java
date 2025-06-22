package net.wiicart.webcli.web.destination.jar;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.ResourceManager;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.screen.PrimaryScreen;
import net.wiicart.webcli.util.ImageConverterUtil;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.web.destination.Destination;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

final class DirectJarImageHandler implements Destination.Handler {

    private final PrimaryScreen screen;

    private final String address;

    private final String title;

    private String content;

    DirectJarImageHandler(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.address = address;
        this.screen = screen;
        title = StringUtils.getFileName(address);
        load();
    }

    private void load() throws LoadFailureException {
        String path = JarDestination.normalizePath(address);
        try {
            InputStream stream = ResourceManager.loadResource(path);
            BufferedImage image = ImageIO.read(stream);
            content = ImageConverterUtil.imageToString(image, screen.getRowCount());
        } catch(final Exception e) {
            throw new LoadFailureException(710, e);
        }
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
}
