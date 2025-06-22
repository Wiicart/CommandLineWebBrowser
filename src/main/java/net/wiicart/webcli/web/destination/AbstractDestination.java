package net.wiicart.webcli.web.destination;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractDestination implements Destination {

    protected AbstractDestination() {

    }

    protected boolean isHtml(@NotNull String address) {
        return address.endsWith(".html")
                || address.endsWith(".htm")
                || address.endsWith(".html/")
                || address.endsWith(".htm/");
    }

    protected boolean isImage(@NotNull String address) {
        return address.endsWith(".png")
                || address.endsWith(".jpg")
                || address.endsWith(".jpeg");
    }
}
