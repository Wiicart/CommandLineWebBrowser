package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.screen.WebPageScreen;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.destination.AbstractDestination;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// https://www.baeldung.com/java-with-jsoup
public class ExternalDestination extends AbstractDestination {

    private final @NotNull String address;

    private final @NotNull WebPageScreen screen;

    private final Handler handler;

    public ExternalDestination(@NotNull String address, @NotNull WebPageScreen screen) throws LoadFailureException {
        this.address = URLUtil.normalizeURL(address);
        this.screen = screen;
        handler = load();
    }

    @Contract(" -> new")
    private @NotNull Handler load() throws LoadFailureException {
        if(isImage()) {
            return new DirectImageHandler(address, screen);
        } else if(isHtml()) {
            return new ExternalHtmlHandler(address);
        } else {
            return new PlainTextHandler(address);
        }
    }

    @Override
    public @NotNull String getTitle() {
        return handler.getTitle();
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        handler.applyContent(panel);
    }

    private boolean isHtml() {
        return address.endsWith(".html")
                || address.endsWith(".htm")
                || address.endsWith(".html/")
                || address.endsWith(".htm/");
    }

    private boolean isImage() {
        return address.endsWith(".png")
                || address.endsWith(".jpg")
                || address.endsWith(".jpeg");
    }

}
