package net.wiicart.webcli.web;

import net.wiicart.webcli.screen.WebPageScreen;
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

    private final WebPageScreen screen;

    public WebManager(@NotNull WebPageScreen screen) {
        this.screen = screen;
    }

    public @NotNull CompletableFuture<WebPage> loadPage(@NotNull String address, @Nullable Progress<Connection.Response> progress) {
        CompletableFuture<WebPage> future = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                // constructor will also attempt to load the page.
                WebPage page = new WebPage(address);
                page.load(progress);
                future.complete(page);
            } catch(Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public void close() {
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
