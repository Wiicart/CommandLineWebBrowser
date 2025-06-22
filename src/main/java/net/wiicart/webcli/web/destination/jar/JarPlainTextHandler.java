package net.wiicart.webcli.web.destination.jar;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.ErrorStatus;
import net.wiicart.webcli.ResourceManager;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.web.destination.Destination;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class JarPlainTextHandler implements Destination.Handler {

    private final String address;

    private final String title;

    private List<String> content;

    JarPlainTextHandler(@NotNull String address) throws LoadFailureException {
        this.address = address;
        title = StringUtils.getFileName(address);
        load();
    }

    @ErrorStatus(codes={750, 0})
    private void load() throws LoadFailureException {
        String path = JarDestination.normalizePath(address);
        try {
            content = ResourceManager.loadLocalResourceAsList(path);

            if(StringUtils.checkIfListIsBlank(content)) {
                throw new LoadFailureException(750);
            }
        } catch (final Exception e) {
            throw new LoadFailureException(750, e);
        }
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        panel.addComponent(box);

        for(String line : content) {
            box.addLine(line);
        }
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }
}
