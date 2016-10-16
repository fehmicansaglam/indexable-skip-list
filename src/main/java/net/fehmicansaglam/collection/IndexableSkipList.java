package net.fehmicansaglam.collection;

import java.security.SecureRandom;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class IndexableSkipList<T extends Comparable<T>> {

    static class Node<T extends Comparable<T>> {
        public final T value;
        private Node<T> right;
        private Node<T> bottom;

        Node(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                "value=" + value +
                ", right=" + right +
                ", bottom=" + bottom +
                '}';
        }
    }

    /**
     * Head is the top left node which is the entry point of this skip list.
     */
    private Node<T> head;

    /**
     * The very bottom level is 0. This number represents the top level.
     */
    private int currLevel;

    /**
     * Used to decide upgrading to new levels.
     */
    private Random random;

    public IndexableSkipList() {
        this.head = new Node<>(null);
        this.random = new SecureRandom();
        this.currLevel = 0;
    }

    private int ensureLevels() {
        int levels = 0;
        while (this.random.nextBoolean()) levels++;

        if (levels > this.currLevel) {
            for (int i = levels; i > this.currLevel; --i) {
                Node<T> newHead = new Node<>(null);
                newHead.bottom = this.head;
                this.head = newHead;
            }

            this.currLevel = levels;
        }

        return levels;
    }

    /**
     * Finds the path to the node containing the value or the nearest value.
     */
    private Deque<Node<T>> find(final T value) {
        final Deque<Node<T>> nodes = new LinkedList<>();

        Node<T> curr = head;

        while (curr != null) {
            while (curr.right != null && curr.right.value.compareTo(value) <= 0) curr = curr.right;

            nodes.push(curr);

            if (Objects.equals(curr.value, value)) {
                return nodes;
            }

            curr = curr.bottom;
        }

        return nodes;
    }

    public boolean insert(T value) {
        if (contains(value)) {
            return false;
        }

        int levels = ensureLevels();

        System.out.println(this.currLevel);

        Deque<Node<T>> nodes = find(value);

        Node<T> bottom = null;
        for (int i = 0; i <= levels; i++) {
            Node<T> node = nodes.pop();
            Node<T> newNode = new Node<>(value);
            newNode.right = node.right;
            newNode.bottom = bottom;
            node.right = newNode;
            bottom = newNode;
        }

        return true;
    }

    public boolean contains(T value) {
        Node node = find(value).peek();
        return node != null && Objects.equals(node.value, value);
    }

    @Override
    public String toString() {
        Node head = this.head;

        StringBuilder builder = new StringBuilder();

        while (head != null) {
            builder.append("HEAD ");
            Node curr = head.right;
            while (curr != null) {
                builder.append(curr.value).append(" ");
                curr = curr.right;
            }
            builder.append("\n");
            head = head.bottom;
        }

        return builder.toString();
    }
    
}
