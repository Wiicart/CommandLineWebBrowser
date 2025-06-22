package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import org.jetbrains.annotations.NotNull;

class PlainTextHandler implements Handler {

    private final String address;

    public PlainTextHandler(@NotNull String address) { //TODO implement
        this.address = address;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {

    }

    @Override
    public @NotNull String getTitle() {
        return "";
    }
}
