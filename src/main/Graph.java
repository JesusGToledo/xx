
package main;

import java.util.*;

public class Graph {
    private Map<Integer, Node> idToNode;
    private Map<Integer, Set<Integer>> parentToChild;

    public Graph() {
        idToNode = new HashMap<>();
        parentToChild = new HashMap<>();
    }

    public void addEdge(int parentID, int childID) {
        Node parentNode = idToNode.get(parentID);
        Node childNode = idToNode.get(childID);
        Set<Integer> relationship = parentToChild.get(parentID);

        if (parentNode == null) {
            parentNode = new Node(parentID);
            idToNode.put(parentID, parentNode);
            System.out.print(parentNode.setWords);
            System.out.print("parentNode.children");

            System.out.print(parentNode.children);

        }

        if (childNode == null) {
            childNode = new Node(childID);
            idToNode.put(childID, childNode);
        }
        if (relationship == null) {
            relationship = new HashSet<>();
            parentToChild.put(parentID, relationship);
        }
        relationship.add(childID);
    }


    public Set<Integer> getNeighbors(int nodeId) {
        return parentToChild.getOrDefault(nodeId, new HashSet<>());
    }

    private class Node {
        private int nodeId;
        private Set<String> setWords;
        private Set<Integer> children;

        public Node(int nodeId) {
            this.nodeId = nodeId;
            this.setWords = new HashSet<>();
            this.children = new HashSet<>();
        }
    }
}