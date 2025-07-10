package net.wiicart.karatasi.web;

import net.wiicart.karatasi.Debug;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.PrimaryScreen;
import net.wiicart.karatasi.web.destination.Destination;
import net.wiicart.karatasi.web.destination.external.ExternalDestination;
import net.wiicart.karatasi.web.destination.jar.JarDestination;
import net.wiicart.karatasi.web.destination.local.LocalDestination;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// https://www.baeldung.com/java-executor-service-tutorial
public final class DestinationManager {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final PrimaryScreen screen;

    public DestinationManager(@NotNull PrimaryScreen screen) {
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

    private @NotNull Destination load(@NotNull String address, @Nullable Progress<Connection.Response> progress) throws LoadFailureException {
        if(address.startsWith("karatasi:/") || address.startsWith("jar:/")) {
            return new JarDestination(address, screen);
        } else if(address.startsWith("file:/") || address.startsWith("local:/")) {
            return new LocalDestination(address, screen);
        } else {
            return new ExternalDestination(address, screen);
        }
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
