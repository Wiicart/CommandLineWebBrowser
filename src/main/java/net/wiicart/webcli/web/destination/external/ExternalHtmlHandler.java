package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.UnknownHostException;

// https://htmlunit.sourceforge.io/gettingStarted.html
class ExternalHtmlHandler implements Handler {

    private final @NotNull String address;

    private Document document;

    ExternalHtmlHandler(@NotNull String address) throws LoadFailureException {
        this.address = address;
        load();
    }

    private void load() throws LoadFailureException {
        Connection connection = Jsoup.connect(address)
                .timeout(10000)
                .followRedirects(true);

        try {
            document = connection.get();
        } catch(final HttpStatusException e) {
            throw new LoadFailureException(e.getStatusCode(), e.getCause());
        } catch(final UnknownHostException e) {
            throw new LoadFailureException(700, address); // 700 signifies unknown host
        } catch(final IOException e) {
            throw new LoadFailureException(-1, e);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        new PrimitiveTextBoxRenderer(document).applyContent(panel);
    }

    @Override
    public @NotNull String getTitle() {
        return document.title();
    }
}
