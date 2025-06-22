package net.wiicart.webcli.web.destination;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.Debug;
import net.wiicart.webcli.ResourceManager;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public final class JARDestination extends AbstractDestination {

    private final String address;

    private String title;

    private List<String> content;

    public JARDestination(@NotNull String address) throws LoadFailureException {
        this.address = address;
        title = StringUtils.getFileName(address);
        load();
    }

    private void load() throws LoadFailureException {
        String path = address;
        if(address.startsWith("jar:/")) {
            path = path.substring(5);
        }

        try {
            content = ResourceManager.loadLocalResourceAsList(path);
        } catch (Exception e) {
            throw new LoadFailureException(-500, e);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        if(address.endsWith(".html") || address.endsWith(".htm") || address.endsWith(".html/")) {
            try {
                Document document = Jsoup.parse(StringUtils.convertListToString(content));
                title = document.title();
                new PrimitiveTextBoxRenderer(document).applyContent(panel);
                return;
            } catch (Exception e) {
                Debug.log("[JARDestination] failed to load jar html file");
                Debug.log(e.getMessage());
            }
        }

        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(box);
        if(content == null) {
            Debug.log("[JARDestination] applyContent called, but content is null.");
            return;
        }


        for(String line : content) {
            box.addLine(line);
        }
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

}
