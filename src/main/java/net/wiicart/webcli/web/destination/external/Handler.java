package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import org.jetbrains.annotations.NotNull;

interface Handler {

    void applyContent(@NotNull Panel panel);

    @NotNull String getTitle();
}
