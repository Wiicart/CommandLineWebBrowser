package net.wiicart.webcli.web.destination.external;

import com.googlecode.lanterna.gui2.Panel;
import net.wiicart.webcli.screen.WebPageScreen;
import net.wiicart.webcli.util.URLUtil;
import net.wiicart.webcli.web.destination.AbstractDestination;
import net.wiicart.webcli.web.renderer.primitivetext.PrimitiveTextBoxRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Progress;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;

// https://www.baeldung.com/java-with-jsoup
public class ExternalDestination extends AbstractDestination {

    private final @NotNull String address;

    private final @NotNull WebPageScreen screen;

    private String content;

    public ExternalDestination(@NotNull String address, @NotNull WebPageScreen screen) {
        this.address = URLUtil.normalizeURL(address);
        this.screen = screen;
    }

    @Override
    public void load(@Nullable Progress<Connection.Response> progress) throws IOException {
        HtmlUnit
        WebDriver driver = new FirefoxDriver();
        driver.get(address);

        String raw = driver.getPageSource();
        if (raw != null) {
            document = Jsoup.parse(raw);
        }

        driver.close();
    }

    public @NotNull String getTitle() {
        return ""; // todo fix
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        if(document == null) {
            try {
                load(null);
            } catch(Exception e) {
                screen.toErrorPage(0, e.getMessage());
                return;
            }
        }

        new PrimitiveTextBoxRenderer(document).applyContent(panel);
    }


}
