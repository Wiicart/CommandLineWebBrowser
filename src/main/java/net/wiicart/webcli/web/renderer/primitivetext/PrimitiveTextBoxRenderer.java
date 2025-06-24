package net.wiicart.webcli.web.renderer.primitivetext;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import net.wiicart.webcli.util.StringUtils;
import net.wiicart.webcli.web.renderer.Renderer;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// Fully text-based renderer that renders all text in to a single TextBox.
public class PrimitiveTextBoxRenderer implements Renderer {

    static final int ROW_SIZE = 60;

    private final Document document;

    public PrimitiveTextBoxRenderer(@NotNull Document document) {
        this.document = document;
    }

    private @NotNull TextBox applyTextBox(@NotNull Panel panel) {
        TextBox textBox = generateFullBodyTextBox();
        panel.addComponent(textBox);
        textBox.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT));
        return textBox;
    }

    @Override
    public void applyContent(@NotNull Panel panel) {
        TextBox box = applyTextBox(panel);
        for(Element element : document.body().children()) {
            List<String> content = getContent(element);
            for(String string : content) {
                box.addLine(string);
            }
        }
    }

    // Recursively generate a List of the content of an Element and it's children.
    private @NotNull List<String> getContent(@NotNull final Element element) {
        List<String> content = new ArrayList<>(Type.getRenderer(element).getContent(element));

        // Don't box if there's nothing in the div other than children
        boolean box = StringUtils.checkIfListIsBlank(content) && element.childrenSize() < 2;
        if(!element.children().isEmpty()) { // add children if the element is not a leaf
            for(Element child : element.children()) {
                content.addAll(getContent(child));
            }
        }

        if(box) {
            content = StringUtils.box(content);
        }

        return content;
    }

    public static @NotNull TextBox generateFullBodyTextBox() {
        TextBox box = new TextBox(new TerminalSize(100, 100), TextBox.Style.MULTI_LINE);
        box.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill));
        //box.setSize(new TerminalSize(100, 100));
        box.setTheme(new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        box.setReadOnly(true);
        return box;
    }

    enum Type {
        HEADER(Set.of("h1", "h2", "h3", "h4", "h5", "h6", "header"), new ASCIIHeaderRenderer()),
        TEXT(Set.of("p", "span", "div", "textarea", "label"), new SimpleTextRenderer()),
        IMAGE(Set.of("img"), new ImageASCIIRenderer()),
        LIST(Set.of("menu", "ul", "ol"), new TextRenderer()),
        BREAK(Set.of("br"), new BreakRenderer()),
        LINK(Set.of("a"), new LinkRenderer()),
        LINE(Set.of("hr"), new LineRenderer()),
        BUTTON(Set.of("button"), new ButtonRenderer()),
        LIST_ITEM(Set.of("li"), new ListItemRenderer()),
        SMALL_SCRIPT(Set.of("sup", "sub"), new ScriptRenderer()),
        STRONG(Set.of("strong", "b"), new StrongRenderer()),
        CODE(Set.of("code"), new CodeRenderer()),;

        private final Set<String> tags;
        private final ElementRenderer renderer;

        private static final UnimplementedElementRenderer UNIMPLEMENTED = new UnimplementedElementRenderer();

        Type(@NotNull Set<String> tags, @NotNull ElementRenderer renderer) {
            this.tags = tags;
            this.renderer = renderer;
        }

        static @NotNull ElementRenderer getRenderer(@NotNull Element element) {
            for(Type type : Type.values()) {
                if(type.tagMatches(element.tagName())) {
                    return type.renderer;
                }
            }
            return UNIMPLEMENTED;
        }

        boolean tagMatches(@NotNull String tag) {
            return tags.contains(tag.toLowerCase(Locale.ROOT));
        }
    }

    interface ElementRenderer {

        @NotNull List<String> getContent(@NotNull Element element);

    }
}
