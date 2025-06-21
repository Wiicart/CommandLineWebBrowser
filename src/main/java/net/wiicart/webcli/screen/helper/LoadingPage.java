package net.wiicart.webcli.screen.helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public final class LoadingPage {

    private LoadingPage() {}

    public static final @Unmodifiable @NotNull List<String> CONTENT;
    static {
        List<String> list = new ArrayList<>();
        list.add("   _                                _    __                     ");
        list.add("  FJ         ____      ___ _     ___FJ   LJ   _ ___      ___ _  ");
        list.add(" J |        F __ J    F __` L   F __  L      J '__ J    F __` L ");
        list.add(" | |       | |--| |  | |--| |  | |--| |  FJ  | |__| |  | |--| | ");
        list.add(" F L_____  F L__J J  F L__J J  F L__J J J  L F L  J J  F L__J J ");
        list.add("J________LJ\\______/FJ\\____,__LJ\\____,__LJ__LJ__L  J__L )-____  L");
        list.add("|________| J______F  J____,__F J____,__F|__||__L  J__|J\\______/F");
        list.add("                                                       J______F ");
        list.add("");
        list.add("                              ,-._");
        list.add("                           _.-'  '--.");
        list.add("                         .'      _  -`\\_");
        list.add("                        / .----.`_.'----'");
        list.add("                        ;/     `");
        list.add("                 jgs   /_;");
        list.add("");
        list.add("                    ._      ._      ._      ._");
        list.add("                _.-._)`\\_.-._)`\\_.-._)`\\_.-._)`\\_.-._");
        list.add("");
        list.add("Art and text credit: www.asciiart.eu");
        CONTENT = List.copyOf(list);
    }

}
