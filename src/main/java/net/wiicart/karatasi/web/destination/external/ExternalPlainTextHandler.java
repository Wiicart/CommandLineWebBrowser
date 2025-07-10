package net.wiicart.karatasi.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.karatasi.config.Option;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.PrimaryScreen;
import net.wiicart.karatasi.util.StringUtils;
import net.wiicart.karatasi.web.destination.Destination;
import net.wiicart.karatasi.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

final class ExternalPlainTextHandler implements Destination.Handler {

    private final String address;

    private final String title;

    private final int timeout;

    private List<String> content;

    public ExternalPlainTextHandler(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.address = address;
        timeout = screen.getBrowser().config().getInt(Option.Int.TIMEOUT);
        title = StringUtils.getFileName(address);
        load();
    }

    private void load() throws LoadFailureException {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) URI.create(address).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            content = reader.lines().toList();
        } catch(final MalformedURLException e) {
            throw new LoadFailureException(700, "Malformed URL", e);
        } catch(final Exception e) {
            throw new LoadFailureException(0, e);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(box);

        for(String line : content) {
            box.addLine(line);
        }
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }
}
