package net.wiicart.karatasi.web.renderer.image;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.karatasi.web.renderer.Renderer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FullPageImageRenderer implements Renderer {

    private final List<String> content;

    public FullPageImageRenderer(List<String> content) {
        this.content = content;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {

    }
}
