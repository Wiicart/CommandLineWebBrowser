package net.wiicart.webcli.screen.helper;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import net.wiicart.webcli.Debug;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ScrollablePanel extends Panel {

    private final WindowBasedTextGUI gui;

    // For use reflecting into super for private fields.
    private static final Class<Panel> CLAZZ = Panel.class;

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
        this.scrollIndex = scrollIndex;
        super.invalidate();
        gui.getScreen().setCursorPosition(new TerminalPosition(0, 0));
    }

    protected void superLayout(TerminalSize size) {
        synchronized(this.components) {
            super.getLayoutManager().doLayout(size, this.components);
        }
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
            final int offset = 5; // ToolBar & other Components at the top create an offset of 5
            final int screenRows = gui.getScreen().getTerminalSize().getRows();
            return scrollIndex + screenRows - offset;
        }

        @Contract("_ -> new")
        private @NotNull TerminalPosition convertPosition(@NotNull TerminalPosition position) {
            return new TerminalPosition(position.getColumn(), position.getRow() - ScrollablePanel.this.scrollIndex);
        }
    }

}
