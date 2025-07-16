package net.wiicart.karatasi.screen;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.ProgressBar;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import net.wiicart.karatasi.Karatasi;
import net.wiicart.karatasi.exception.LoadFailureException;
import net.wiicart.karatasi.screen.helper.LoadingPage;
import net.wiicart.karatasi.screen.helper.PageManager;
import net.wiicart.karatasi.screen.helper.ScrollablePanel;
import net.wiicart.karatasi.screen.helper.ToolBar;
import net.wiicart.karatasi.screen.helper.UnreachablePage;
import net.wiicart.karatasi.screen.listener.ScrollablePanelHelper;
import net.wiicart.karatasi.web.destination.Destination;
import net.wiicart.karatasi.web.DestinationManager;
import net.wiicart.karatasi.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PrimaryScreen {

    private static final Set<Window.Hint> ENTRY_HINTS = Set.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW);

    private final Karatasi browser;

    private final WindowBasedTextGUI gui;

    private final DestinationManager engine = new DestinationManager(this);

    private final PageManager pages = new PageManager(this, "");

    private final ScrollablePanel content;

    private final Label title = loadTitle();

    private final ToolBar toolBar;

    private final EmptySpace emptySpace;

    private ProgressBar progress;

    private final Window window;

    public PrimaryScreen(@NotNull Karatasi browser, @NotNull WindowBasedTextGUI gui) {
        this.browser = browser;
        this.gui = gui;
        toolBar = new ToolBar(this, "Title", " ".repeat(60)); // must be after gui assignment
        content = new ScrollablePanel(gui);
        emptySpace = new EmptySpace(TextColor.ANSI.WHITE, new TerminalSize(getColumnCount(), 1));
        emptySpace.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill, LinearLayout.GrowPolicy.CanGrow));
        window = createWindow();

        goToAddress("karatasi://welcome.html", false);
        gui.addWindowAndWait(window);
    }

    private @NotNull Window createWindow() {
        BasicWindow window1 = new BasicWindow("Karatasi");

        Panel root = new Panel();
        root.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        root.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        root.addComponent(title);
        loadToolBar(root);

        content.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        root.addComponent(content);

        window1.setComponent(root);
        window1.setHints(ENTRY_HINTS);

        window1.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
                emptySpace.setSize(new TerminalSize(newSize.getColumns(), 1));
                toolBar.setPanelSize(newSize.getColumns());
            }
        });

        window1.addWindowListener(new ScrollablePanelHelper(content));
        return window1;
    }

    private void loadToolBar(@NotNull Panel root) {
        Panel wrapper = new Panel();
        wrapper.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        wrapper.setSize(new TerminalSize(getColumnCount(), 1));
        wrapper.setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT));
        wrapper.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center, LinearLayout.GrowPolicy.CanGrow));

        toolBar.setPanelSize(getColumnCount());
        wrapper.addComponent(toolBar.getPanel());
        root.addComponent(wrapper);
        root.addComponent(emptySpace);
    }

    public void backwards() {
        pages.toPrevious();
    }

    public void forwards() {
        pages.toNext();
    }

    public void refresh() {
        goToAddress(pages.currentAddress(), false);
    }

    public void goToAddress(String address, boolean updateNode) {
        if(updateNode) {
            pages.update(address);
        }

        content.removeAllComponents();
        // Remove
        progress = new ProgressBar(0, 100, getColumnCount());
        progress.setValue(0);
        // End remove
        content.setScrollIndex(0);
        resetCursorPosition();

        toLoadingPage();
        toolBar.setAddress(address);

        CompletableFuture<? extends Destination> future = engine.loadPage(address, generateProgressUpdate());
        future.thenAccept(destination -> {
            content.removeAllComponents();
            destination.applyContent(content);
            title.setText(destination.getTitle());
        }).exceptionally(e -> {
            Throwable cause = e.getCause();
            if(cause instanceof LoadFailureException ex) {
                toErrorPage(ex.code(), ex.getCause().getMessage());
            } else {
                toErrorPage(000, e.getCause().getMessage());
            }
            return null;
        });
    }

    private void toLoadingPage() {
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        for(String string : LoadingPage.CONTENT) {
            box.addLine(string);
        }

        content.addComponent(box);
    }

    public void toErrorPage(int code, @Nullable String message) {
        content.removeAllComponents();
        TextBox box = PrimitiveTextBoxRenderer.generateFullBodyTextBox();
        for(String string : UnreachablePage.withCode(code)) {
            box.addLine(string);
        }

        if(message != null) {
            box.addLine(message);
        }

        title.setText("Error " + code);
        content.addComponent(box);
    }

    public void exit() {
        window.close();
        engine.close();
    }

    private @NotNull Label loadTitle() {
        Label title1 = new Label("Blank Page");
        title1.setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.WHITE));
        title1.addStyle(SGR.BOLD);
        title1.addStyle(SGR.FRAKTUR);
        title1.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        title1.setSize(new TerminalSize(100, 100));
        return title1;
    }

    private void resetCursorPosition() {
        try {
            browser.lanternaScreen().setCursorPosition(new TerminalPosition(0, 0));
        } catch(Exception ignored) {}
    }

    public int getColumnCount() {
        return gui.getScreen().getTerminalSize().getColumns();
    }

    public int getRowCount() {
        return gui.getScreen().getTerminalSize().getRows();
    }

    public WindowBasedTextGUI getGui() {
        return gui;
    }

    @Contract(pure = true)
    private @NotNull Progress<Connection.Response> generateProgressUpdate() {
        return (processed, total, percent, context) -> {
            int processedPercent = Float.valueOf(percent).intValue();
            progress.setValue(processedPercent);
        };
    }

    public Karatasi getBrowser() {
        return browser;
    }
}
