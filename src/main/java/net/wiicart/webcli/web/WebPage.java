package net.wiicart.webcli.web;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.renderer.PrimitiveRenderer;
import net.wiicart.webcli.web.renderer.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

// https://www.baeldung.com/java-with-jsoup
public final class WebPage {

    private final @NotNull String title;

    private final @NotNull Document document;


    @Contract("_ -> new")
    public static @NotNull WebPage fromAddress(@NotNull String address) throws IOException {
        address = URLUtil.normalizeURL(address);
        return new WebPage(address);
    }


    private WebPage(@NotNull String address) throws IOException {
        document = Jsoup.connect(address)
                .timeout(10000)
                .get();

        title = document.title();
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void applyContent(@NotNull Panel panel) {
        PrimitiveTextBoxRenderer renderer = new PrimitiveTextBoxRenderer(document);
        renderer.applyContent(panel);
    }

}
