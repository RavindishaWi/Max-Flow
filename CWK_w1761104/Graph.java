package com.project.networkflow;

import java.util.List;
import java.util.ArrayList;

public class Graph {
    private final int numberOfNodes;
    private final float[][] adjacencyMatrix;

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public float[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    //create graph
    public Graph(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        adjacencyMatrix = new float[numberOfNodes][numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    public void addEdgeFromFile(int u, int v, float weightedGraph) {
        adjacencyMatrix[u][v] = weightedGraph;
    }

    public boolean hasEdge(int u, int v) {
        return adjacencyMatrix[u][v] != 0;
    }

    public List<Integer> neighbours(int node) {
        List<Integer> edgesList = new ArrayList<>();
        for (int i = 0; i < numberOfNodes; i++) {
            if (hasEdge(node, i)) {
                edgesList.add(i);
            }
        }
        return edgesList;
    }

    public void displayGraph() {
        for (int j = 0; j < numberOfNodes; j++) {
            List<Integer> edges = neighbours(j);
            System.out.print(j + ": ");
            for (Integer edge : edges) {
                System.out.print(edge + " ");
            }
            System.out.println();
        }
    }
}