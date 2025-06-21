package net.wiicart.webcli.web;

import com.googlecode.lanterna.gui2.Panel;
import javafx.scene.web.WebEngine;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Progress;
import org.jsoup.nodes.Document;

import java.io.IOException;

// https://www.baeldung.com/java-with-jsoup
@SuppressWarnings("unused")
public final class WebPage {

    private final @NotNull String address;

    private Document document;

    WebPage(@NotNull String address) {
        this.address = URLUtil.normalizeURL(address);
    }

    public void load(@Nullable Progress<Connection.Response> progress) throws IOException {
        Connection connection = Jsoup.connect(address)
                .followRedirects(true)
                .timeout(10000);

        if(progress != null) {
            connection.onResponseProgress(progress);
        }

        document = connection.get();
    }

    public @NotNull String getTitle() {
        if(document == null) {
            return "";
        }
        return document.title();
    }

    // Ensure load() has been called before this.
    @SuppressWarnings("unused")
    public @Nullable Document getDocument() {
        return document;
    }

    public void applyContent(@NotNull Panel panel) {
        if(document == null) {
            try {
                load(null);
            } catch(Exception ignored) {

            }
        }
        PrimitiveTextBoxRenderer renderer = new PrimitiveTextBoxRenderer(document);
        renderer.applyContent(panel);
    }

}
