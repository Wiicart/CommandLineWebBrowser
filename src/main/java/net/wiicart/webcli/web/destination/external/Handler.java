package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

interface Handler {

    void load(@Nullable Progress<Connection.Response> progress) throws Exception;

    void applyContent(@NotNull Panel panel);

    @NotNull String getTitle();
}
