package net.wiicart.karatasi.web.renderer;

import com.googlecode.lanterna.gui2.Panel;
import org.jetbrains.annotations.NotNull;

public interface Renderer {
    void applyContent(@NotNull Panel panel);
}
