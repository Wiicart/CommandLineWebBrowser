package net.wiicart.webcli.screen.helper;

import net.wiicart.webcli.screen.PrimaryScreen;
import org.jetbrains.annotations.NotNull;

/**
 * Helps operate the backwards and forwards buttons in the Browser
 */
public final class PageManager {

    private final PrimaryScreen screen;

    private Node current;

    public PageManager(@NotNull PrimaryScreen screen, @NotNull String address) {
        this.screen = screen;
        current = new Node(address);
    }

    // Updates the nodes to reflect the browser having gone to a new page.
    public void update(String address) {
        Node newNode = new Node(address);
        newNode.previous = current;
        current.next = newNode;
        current = newNode;
    }

    // Provides the current address
    public String currentAddress() {
        return current.address;
    }

    // Moves to the next page if it exists
    public void toNext() {
        Node next = current.next;
        if(next != null) {
            current = next;
            screen.refresh();
        }
    }

    // Moves to the previous page if it exists
    public void toPrevious() {
        Node previous = current.previous;
        if(previous != null) {
            current = previous;
            screen.refresh();
        }
    }


    private static final class Node {

        private final String address;

        private Node previous;
        private Node next;

        public Node(String address) {
            this.address = address;
        }

    }

}
