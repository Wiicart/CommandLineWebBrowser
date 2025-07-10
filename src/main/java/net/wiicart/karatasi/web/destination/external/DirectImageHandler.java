package net.wiicart.karatasi.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.karatasi.ErrorStatus;
import net.wiicart.karatasi.config.Option;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.PrimaryScreen;
import net.wiicart.karatasi.util.ImageConverterUtil;
import net.wiicart.karatasi.util.StringUtils;
import net.wiicart.karatasi.util.URLUtil;
import net.wiicart.karatasi.web.destination.Destination;
import net.wiicart.karatasi.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

final class DirectImageHandler implements Destination.Handler {

    private final String address;

    private final PrimaryScreen screen;

    private final String title;

    private final int timeout;

    private String content;

    DirectImageHandler(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.address = address;
        this.screen = screen;

        title = StringUtils.getFileName(address);
        timeout = screen.getBrowser().config().getInt(Option.Int.TIMEOUT);

        load();
    }

    @ErrorStatus(codes={710})
    private void load() throws LoadFailureException {
        try {
            URL url = new URL(URLUtil.normalizeURL(address));
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);

            try(InputStream input = connection.getInputStream()) {
                BufferedImage image = ImageIO.read(input);
                content = ImageConverterUtil.imageToString(
                        image,
                        screen.getRowCount() - 5
                );
            }
        } catch(Exception e) {
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
