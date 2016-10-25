package net.fehmicansaglam.collection;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class IndexableSkipList<T extends Comparable<T>> {

    private class Node {
        public final T value;
        private Node right;
        private Node bottom;
        private int distance;

        Node(T value) {
            this.value = value;
            this.distance = 0;
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

    private class NodeDistance {
        public final Node node;
        public final int distance;

        public NodeDistance(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    /**
     * Head is the top left node which is the entry point of this skip list.
     */
    private Node head;

    public int size;

    /**
     * The very bottom level is 0. This number represents the top level.
     */
    private int currLevel;

    /**
     * Used to decide upgrading to new levels.
     */
    private Random random;

    public IndexableSkipList() {
        this.head = new Node(null);
        this.size = 0;
        this.random = new Random();
        this.currLevel = 0;
    }

    private int ensureLevels() {
        int levels = 0;
        while (this.random.nextBoolean()) levels++;

        if (levels > this.currLevel) {
            for (int i = levels; i > this.currLevel; --i) {
                Node newHead = new Node(null);
                newHead.distance = this.size;
                System.out.println("Head distance: " + newHead.distance);
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
    private Deque<NodeDistance> find(final T value) {
        final Deque<NodeDistance> nodes = new LinkedList<>();

        Node curr = head;

        while (curr != null) {
            int distance = 0;
            while (curr.right != null && curr.right.value.compareTo(value) <= 0) {
                distance += curr.distance;
                curr = curr.right;
            }

            nodes.push(new NodeDistance(curr, distance));

            if (Objects.equals(curr.value, value)) {
                return nodes;
            }

            curr = curr.bottom;
        }

        return nodes;
    }

    public T closest(final T value) {
        final NodeDistance nodeDistance = find(value).peek();
        return nodeDistance == null ? null : nodeDistance.node.value;
    }

    public boolean insert(T value) {
        if (contains(value)) {
            return false;
        }

        int levels = ensureLevels();

//        System.out.println(this.currLevel);

        Deque<NodeDistance> nodes = find(value);

        Node bottom = null;
        int prevLevelDist = 0;
        for (int i = 0; i <= levels; i++) {
            NodeDistance nodeDistance = nodes.pop();

            Node newNode = new Node(value);
            newNode.right = nodeDistance.node.right;
            newNode.bottom = bottom;

            nodeDistance.node.right = newNode;
            int oldDistance = nodeDistance.node.distance;
            System.out.println("prev dist:" + prevLevelDist);
            nodeDistance.node.distance = prevLevelDist + 1;
            newNode.distance = (oldDistance - prevLevelDist);
            prevLevelDist += nodeDistance.distance;
            bottom = newNode;
        }

        while (!nodes.isEmpty()) {
            NodeDistance nodeDistance = nodes.pop();
            System.out.println("Incrementing " + nodeDistance.node.value);
            nodeDistance.node.distance++;
        }

        this.size++;

        return true;
    }

    public boolean contains(T value) {
        NodeDistance nodeDistance = find(value).peek();
        return nodeDistance != null && Objects.equals(nodeDistance.node.value, value);
    }

    @Override
    public String toString() {
        Node head = this.head;

        StringBuilder builder = new StringBuilder();

        while (head != null) {
            builder.append("HEAD");
            for (int i = 0; i < head.distance; i++)
                builder.append("\t");
            Node curr = head.right;
            while (curr != null) {
                builder.append(curr.value);
                for (int i = 0; i < curr.distance; i++)
                    builder.append("\t");
                curr = curr.right;
            }
            builder.append("\n");
            head = head.bottom;
        }

        return builder.toString();
    }

}
