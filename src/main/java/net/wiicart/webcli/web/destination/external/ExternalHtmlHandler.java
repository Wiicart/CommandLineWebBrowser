package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import com.sun.webkit.WebPage;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

// https://htmlunit.sourceforge.io/gettingStarted.html
class ExternalHtmlHandler implements Handler {

    private final @NotNull String address;

    private String title;

    ExternalHtmlHandler(@NotNull String address) {
        this.address = address;
    }

    @Override
    public void load(@Nullable Progress<Connection.Response> progress) throws Exception {
        try (final WebClient client = new WebClient()) {
            HtmlPage page = client.getPage(address);

        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {

    }

    @Override
    public @NotNull String getTitle() {
        return "";
    }
}
