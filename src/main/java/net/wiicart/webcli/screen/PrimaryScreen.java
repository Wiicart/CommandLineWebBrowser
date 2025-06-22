package net.wiicart.webcli.screen;

import com.googlecode.lanterna.SGR;
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
import com.googlecode.lanterna.gui2.TextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.WindowListenerAdapter;
import com.googlecode.lanterna.input.KeyStroke;
import net.wiicart.webcli.CLIBrowser;
import net.wiicart.webcli.exception.LoadFailureException;
import net.wiicart.webcli.screen.helper.LoadingPage;
import net.wiicart.webcli.screen.helper.PageManager;
import net.wiicart.webcli.screen.helper.ToolBar;
import net.wiicart.webcli.screen.helper.UnreachablePage;
import net.wiicart.webcli.web.destination.Destination;
import net.wiicart.webcli.web.DestinationManager;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Progress;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PrimaryScreen {

    private static final Set<Window.Hint> ENTRY_HINTS = Set.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW);

    private final CLIBrowser browser;

    private final WindowBasedTextGUI gui;

    private final DestinationManager engine = new DestinationManager(this);

    private final PageManager pages = new PageManager(this, "");

    private final Panel content = new Panel();

    private final Label title = loadTitle();

    private final ToolBar toolBar;

    private final EmptySpace emptySpace;

    private ProgressBar progress;

    private final Window window;

    public PrimaryScreen(@NotNull CLIBrowser browser, @NotNull WindowBasedTextGUI gui) {
        this.browser = browser;
        this.gui = gui;
        toolBar = new ToolBar(this, "Title", " ".repeat(60)); // must be after gui assignment
        emptySpace = new EmptySpace(TextColor.ANSI.WHITE, new TerminalSize(getColumnCount(), 1));
        emptySpace.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill, LinearLayout.GrowPolicy.CanGrow));
        window = createWindow();

        goToAddress("jar://welcome.html", false);
        gui.addWindowAndWait(window);
    }

    private @NotNull Window createWindow() {
        BasicWindow window1 = new BasicWindow("Web Browser");

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
        progress = new ProgressBar(0, 100, getColumnCount());
        progress.setValue(0);
        content.addComponent(progress);
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

    public int getColumnCount() {
        return gui.getScreen().getTerminalSize().getColumns();
    }

    public int getRowCount() {
        return gui.getScreen().getTerminalSize().getRows();
    }

    @Contract(pure = true)
    private @NotNull Progress<Connection.Response> generateProgressUpdate() {
        return (processed, total, percent, context) -> {
            int processedPercent = Float.valueOf(percent).intValue();
            progress.setValue(processedPercent);
        };
    }

    @SuppressWarnings("unused")
    private static final class EscapeListener implements TextGUI.Listener {

        @Override
        public boolean onUnhandledKeyStroke(TextGUI textGUI, KeyStroke keyStroke) {
            return false;
        }
    }

    public CLIBrowser getBrowser() {
        return browser;
    }
}
