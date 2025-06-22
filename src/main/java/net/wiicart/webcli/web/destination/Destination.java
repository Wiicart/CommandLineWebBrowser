package net.wiicart.webcli.web.destination;

import com.googlecode.lanterna.gui2.Panel;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Progress;

public interface Destination {

    /**
     * Applies content from the page to the panel.
     * Ensure {@link Destination#load(Progress)} has been called before invoking.
     * @param panel The Panel to apply the content to.
     */
    void applyContent(@NotNull Panel panel);

    //void applyErrorPage(@NotNull Panel panel, @NotNull LoadFailureException ex);

    /**
     * Provides the title of the document
     * @return A String
     */
    @NotNull String getTitle();

}
