package net.wiicart.karatasi.web.destination.jar;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.ErrorStatus;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.PrimaryScreen;

import net.wiicart.karatasi.web.destination.AbstractDestination;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class JarDestination extends AbstractDestination {

    private final String address;

    private final PrimaryScreen screen;

    private final Handler handler;

    public JarDestination(@NotNull String address, @NotNull PrimaryScreen screen) throws LoadFailureException {
        this.address = address;
        this.screen = screen;
        handler = load();
    }

    @Contract(" -> new")
    @ErrorStatus(codes={750, 0})
    private @NotNull Handler load() throws LoadFailureException {
        if(address.equals("jar://config.yml") || address.equals("karatasi://config.yml")) {
            return new JarConfigurationHandler(screen);
        } else if(isImage(address)) {
            return new DirectJarImageHandler(address, screen);
        } else if(isHtml(address)) { //todo refactor to better encompass html
            return new JarHtmlHandler(address);
        } else {
            return new JarPlainTextHandler(address);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        handler.applyContent(panel);
    }

    @Override
    public @NotNull String getTitle() {
        return handler.getTitle();
    }

    public static @NotNull String normalizePath(@NotNull String path) {
        if(path.startsWith("jar:/")) {
            path = path.substring(5);
        }

        if(path.startsWith("karatasi:/")) {
            path = path.substring(10);
        }

        return path;
    }

}
