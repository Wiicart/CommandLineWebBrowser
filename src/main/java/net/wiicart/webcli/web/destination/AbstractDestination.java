package net.wiicart.webcli.web.destination;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.web.renderer.Renderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

public class AbstractDestination implements Destination {

    protected AbstractDestination() {

    }

    @Override
    public void load(@Nullable Progress<Connection.Response> progress) throws Exception {

    }

    @Override
    public void applyContent(@NotNull Panel panel) {

    }

    @Override
    public @NotNull String getTitle() {
        return "";
    }

    protected Renderer determineRenderer() {
        return null;
    }
}
