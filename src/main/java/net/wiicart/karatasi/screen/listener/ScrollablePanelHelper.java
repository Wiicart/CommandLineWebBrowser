package net.wiicart.karatasi.screen.listener;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import net.wiicart.karatasi.screen.helper.ScrollablePanel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that helps run {@link ScrollablePanel}
 */
public class ScrollablePanelHelper extends WindowListenerAdapter {

    private final ScrollablePanel panel;

    public ScrollablePanelHelper(ScrollablePanel panel) {
        this.panel = panel;
    }

    @Override
    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
        panel.invalidate();
    }

    @Override
    public void onInput(@NotNull Window basePane, @NotNull KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
        final int index = panel.getScrollIndex();

        final Interactable focus = basePane.getFocusedInteractable();
        if(focus instanceof TextBox box) {
            switch(keyStroke.getKeyType()) {
                case ArrowUp -> {
                    if(!canScrollUp(box) && panel.canScrollUp()) {
                        panel.setScrollIndex(index - 1);
                    }
                    // Not setting hasBeenHandled to true so new elements can be focused
                }
                case ArrowDown -> {
                    if(!canScrollDown(box) && panel.canScrollDown()) {
                        panel.setScrollIndex(index + 1);
                    }
                    // Not setting hasBeenHandled to true so new elements can be focused
                }
                default -> {}
            }
            return;
        }

        switch(keyStroke.getKeyType()) {
            case ArrowUp -> {
                if(panel.canScrollUp()) {
                    panel.setScrollIndex(index - 1);
                    hasBeenHandled.set(true);
                }
            }
            case ArrowDown -> {
                if(panel.canScrollDown()) {
                    panel.setScrollIndex(index + 1);
                    hasBeenHandled.set(true);
                }
            }
            default -> {}
        }
    }

    // Same as TextBox private method canMoveCaretDown()
    private boolean canScrollDown(@NotNull TextBox box) {
        return box.getCaretPosition().getRow() < box.getLineCount() - 1;
    }

    // Same as TextBox private method canMoveCaretUp()
    private boolean canScrollUp(@NotNull TextBox box) {
        return box.getCaretPosition().getRow() > 0;
    }
}
