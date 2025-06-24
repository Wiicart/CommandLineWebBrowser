package net.wiicart.webcli.screen.listener;

import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import net.wiicart.webcli.screen.helper.ScrollablePanel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class ArrowUpAndDownListener extends WindowListenerAdapter {

    private final ScrollablePanel panel;

    public ArrowUpAndDownListener(ScrollablePanel panel) {
        this.panel = panel;
    }

    @Override
    public void onInput(Window basePane, @NotNull KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
        final int index = panel.getScrollIndex();
        switch(keyStroke.getKeyType()) {
            case ArrowUp -> {
                if(index > 0) {
                    panel.setScrollIndex(index - 1);
                    hasBeenHandled.set(true);
                }
            }
            case ArrowDown -> {
                panel.setScrollIndex(index + 1);
                hasBeenHandled.set(true);
            }
            default -> {}
        }
    }
}
