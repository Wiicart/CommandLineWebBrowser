package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.screen.PrimaryScreen;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.destination.AbstractDestination;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// https://www.baeldung.com/java-with-jsoup
public final class ExternalDestination extends AbstractDestination {

    private final @NotNull String address;

    private final @NotNull PrimaryScreen screen;

    private final Handler handler;

    public ExternalDestination(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.address = URLUtil.normalizeURL(address);
        this.screen = screen;
        handler = load();
    }

    @Contract(" -> new")
    private @NotNull Handler load() throws LoadFailureException {
        if(isImage(address)) {
            return new DirectImageHandler(address, screen);
        } else if(isHtml(address)) {
            return new ExternalHtmlHandler(address, screen);
        } else {
            return new ExternalPlainTextHandler(address, screen);
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

}
