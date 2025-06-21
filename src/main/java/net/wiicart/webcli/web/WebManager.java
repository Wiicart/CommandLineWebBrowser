package net.wiicart.webcli.web;

import net.wiicart.webcli.Debug;
import net.wiicart.webcli.screen.WebPageScreen;
import net.wiicart.webcli.web.destination.Destination;
import net.wiicart.webcli.web.destination.external.ExternalDestination;
import net.wiicart.webcli.web.destination.JARDestination;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// https://www.baeldung.com/java-executor-service-tutorial
public final class WebManager {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final WebPageScreen screen; //todo add escape button

    public WebManager(@NotNull WebPageScreen screen) {
        this.screen = screen;
    }

    public @NotNull CompletableFuture<? extends Destination> loadPage(@NotNull String address, @Nullable Progress<Connection.Response> progress) {
        CompletableFuture<Destination> future = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                future.complete(load(address, progress));
            } catch(Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    private @NotNull Destination load(@NotNull String address, @Nullable Progress<Connection.Response> progress) throws Exception {
        if(address.startsWith("http://") || address.startsWith("https://")) {
            if(address.endsWith(".png") || address.endsWith(".jpg")) {
                Destination img = new FullPageImage(screen, address);
                img.load(null);

                return img;
            }

            Destination page = new ExternalDestination(address, screen);
            page.load(progress);
            return page;
        } else if(address.startsWith("jar:/")) {
            Destination resource = new JARDestination(address);
            resource.load(null);
            return resource;
        }
        return new ExternalDestination(address, screen);
    }

    // Release the Threads, invoke at shutdown
    public void close() {
        Debug.log("Shutting down ExecutorService");
        executor.shutdown();
        try {
            if(!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch(InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
