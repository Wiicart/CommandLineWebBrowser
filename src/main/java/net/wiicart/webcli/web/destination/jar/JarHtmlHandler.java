package net.wiicart.webcli.web.destination.jar;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.ErrorStatus;
import net.wiicart.webcli.ResourceManager;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.web.destination.Destination;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

final class JarHtmlHandler implements Destination.Handler {

    private final String address;

    private String title;

    private Document document;

    JarHtmlHandler(@NotNull String address) throws LoadFailureException {
        this.address = address;
        load();
    }

    @ErrorStatus(codes={0})
    private void load() throws LoadFailureException {
        String path = JarDestination.normalizePath(address);
        try {
            String content = ResourceManager.loadLocalResource(path);
            document = Jsoup.parse(content);
            title = document.title();
        } catch(Exception e) {
            throw new LoadFailureException(0, e);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        new PrimitiveTextBoxRenderer(document).applyContent(panel);
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }
}
