package com.googlecode.lanterna.gui2;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class StaticTextGUIGraphics extends DefaultTextGUIGraphics {

    private static final TerminalSize STATIC_SIZE = new TerminalSize(100, 100);

    private final TextGUI textGUI;
    private final TextGraphics backend;

    /**
     * Constructs the class.
     * @param textGUI A WindowBasedTextGUI is acceptable
     * @param backend The TextGraphics backend
     */
    public StaticTextGUIGraphics(TextGUI textGUI, TextGraphics backend) {
        super(textGUI, backend);
        this.textGUI = textGUI;
        this.backend = backend;
    }

    @Contract("_, _ -> new")
    @Override
    public @NotNull DefaultTextGUIGraphics newTextGraphics(TerminalPosition topLeftCorner, TerminalSize size) throws IllegalArgumentException {
        return new DefaultTextGUIGraphics(this.textGUI, this.backend.newTextGraphics(topLeftCorner, STATIC_SIZE));
    }

}
