package net.wiicart.webcli.web.destination.local;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.screen.PrimaryScreen;
import net.wiicart.webcli.web.destination.Destination;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a "local destination" on the System's disk.
 * The starting point directory is "user.home"
 */
public final class LocalDestination implements Destination {

    private final PrimaryScreen screen;

    private final Handler handler;

    public LocalDestination(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.screen = screen;
        handler = load(address);
    }

    @Contract(pure = true)
    private @Nullable Handler load(@NotNull String address) throws LoadFailureException {
        return null;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        handler.applyContent(panel);
    }

    @Override
    public @NotNull String getTitle() {
        return handler.getTitle();
    }
}
