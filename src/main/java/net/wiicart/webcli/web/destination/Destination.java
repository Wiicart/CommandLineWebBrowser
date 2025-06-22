package net.wiicart.webcli.web.destination;

import com.googlecode.lanterna.gui2.Panel;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Destination on the web, on the jar, or a local file.
 * Connections & reading should be processed on instantiation.
 */
public interface Destination {

    /**
     * Applies content from the page to the panel.
     * @param panel The Panel to apply the content to.
     */
    void applyContent(@NotNull Panel panel);

    //void applyErrorPage(@NotNull Panel panel, @NotNull LoadFailureException ex);

    /**
     * Provides the title of the document
     * @return A String
     */
    @NotNull String getTitle();

    interface Handler {

        void applyContent(@NotNull Panel panel);

        @NotNull String getTitle();

    }

}
