package net.wiicart.webcli.screen.helper;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import net.wiicart.webcli.Debug;
import net.wiicart.webcli.screen.listener.ScrollablePanelHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A Scrollable Panel. Requires {@link ScrollablePanelHelper} to scroll.
 */
public class ScrollablePanel extends Panel {

    // Represents how many rows are taken up by the toolbar, etc.
    private static final int OFFSET = 5;

    // For use reflecting into super for private fields.
    private static final Class<Panel> CLAZZ = Panel.class;

    private final WindowBasedTextGUI gui;

    protected int scrollIndex = 0;

    protected final List<Component> components = getComponentsFromSuper();

    public ScrollablePanel(@NotNull WindowBasedTextGUI gui) {
        super();
        this.gui = gui;
        super.setRenderer(new ScrollablePanelRenderer());
    }

    public int getScrollIndex() {
        return scrollIndex;
    }

    public void setScrollIndex(final int scrollIndex) {
        if (scrollIndex > 0 && scrollIndex < maxScrollIndex()) {
            this.scrollIndex = scrollIndex;
            super.invalidate();
            gui.getScreen().setCursorPosition(new TerminalPosition(0, 0));
        }
    }

    /**
     * Tells if scrolling down would be appropriate - checking if there's any content below.
     * @return True/false
     */
    public boolean canScrollDown() {
        return scrollIndex < maxScrollIndex();
    }

    /**
     * Tells if scrolling up would be appropriate - checking if there's any content above
     * @return True/false
     */
    public boolean canScrollUp() {
        return scrollIndex > 0;
    }

    /**
     * Calculated by getting the heights of all Components.
     * @return An int
     */
    public int maxScrollIndex() {
        int contentLength = 0;
        for(Component component : components) {
            TerminalPosition pos = component.getPosition();
            int bottom = pos.getRow() + component.getSize().getRows();
            contentLength = Math.max(contentLength, bottom);
        }

        int visible = getTerminalRows();
        return Math.max(0, contentLength - (visible - OFFSET));
    }

    protected void superLayout(TerminalSize size) {
        synchronized(this.components) {
            super.getLayoutManager().doLayout(size, this.components);
        }
    }

    private int getTerminalRows() {
        return gui.getScreen().getTerminalSize().getRows();
    }

    @SuppressWarnings("unchecked")
    private List<Component> getComponentsFromSuper() {
        List<Component> temp = new ArrayList<>();
        try {
            Field field = CLAZZ.getDeclaredField("components");
            field.setAccessible(true);
            temp = (List<Component>) field.get(this);
        } catch (final NoSuchFieldException e) {
            Debug.log("Failed to access components field in superclass.");
            Debug.log(e.getMessage());
        } catch (final SecurityException e) {
            Debug.log("SecurityException: Failed to access components field in superclass.");
            Debug.log(e.getMessage());
        } catch(final Exception e) {
            Debug.log("Exception: Failed to access components field in superclass.");
            Debug.log(e.getMessage());
        }

        return temp;
    }

    public class ScrollablePanelRenderer extends DefaultPanelRenderer {

        /**
         * Copied from the superclass implementation for the most part, just modified for scrolling.
         * @param graphics The TextGUIGraphics
         * @param panel The Panel
         */
        @Override
        public void drawComponent(TextGUIGraphics graphics, Panel panel) {
            if (ScrollablePanel.this.isInvalid()) {
                ScrollablePanel.this.superLayout(graphics.getSize());
            }

            // if block in super - this.fillAreaBeforeDrawingComponents
            graphics.applyThemeStyle(ScrollablePanel.this.getThemeDefinition().getNormal());
            if (ScrollablePanel.this.getFillColorOverride() != null) {
                graphics.setBackgroundColor(ScrollablePanel.this.getFillColorOverride());
            }
            // end if

            // Components appended here, so scrolling logic must be implemented here.
            synchronized(ScrollablePanel.this.components) {
                for(Component child : ScrollablePanel.this.components) {
                    if (child.isVisible() && shouldComponentBeDrawn(child)) {
                        TextGUIGraphics componentGraphics = graphics.newTextGraphics(
                                convertPosition(child.getPosition()),
                                child.getSize()
                        );
                        child.draw(componentGraphics);
                    }
                }
            }
        }

        // Determines if the Component should be drawn, given the current index.
        private boolean shouldComponentBeDrawn(@NotNull Component child) {
            final int row = child.getPosition().getRow();
            final int height = child.getSize().getRows();
            final int top = scrollIndex;
            final int bottom = bottomIndex();

            return row + height > top && row < bottom;
        }

        private int bottomIndex() {
            final int screenRows = getTerminalRows();
            return scrollIndex + screenRows - OFFSET;
        }

        @Contract("_ -> new")
        private @NotNull TerminalPosition convertPosition(@NotNull TerminalPosition position) {
            return new TerminalPosition(position.getColumn(), position.getRow() - ScrollablePanel.this.scrollIndex);
        }
    }

}
