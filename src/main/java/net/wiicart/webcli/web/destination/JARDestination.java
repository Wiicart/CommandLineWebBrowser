package net.wiicart.webcli.web.destination;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.Debug;
import net.wiicart.webcli.ResourceManager;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

import java.util.List;

public final class JARDestination extends AbstractDestination {

    private final String address;

    private String title;

    private List<String> content;

    public JARDestination(@NotNull String address) {
        this.address = address;
    }

    @Override
    public void load(@Nullable Progress<Connection.Response> progress) throws Exception {
        String path = address;
        if(address.startsWith("jar:/")) {
            path = path.substring(5);
        }

        content = ResourceManager.loadLocalResourceAsString(path);
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(box);
        if(content == null) {
            Debug.log("[JarResource] applyContent called, but content is null.");
            return;
        }

        for(String line : content) {
            box.addLine(line);
        }
    }

    @Override
    public @NotNull String getTitle() {
        return "Jar Resource"; //todo find filename
    }
}
