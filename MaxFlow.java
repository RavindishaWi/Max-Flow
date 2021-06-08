package com.project.networkflow;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MaxFlow {

    public static void main(String[] args) {

        long startingTime = System.nanoTime(); //get starting time in nano seconds

        //set file path to networkflow.txt in user desktop
        //read the text file from user desktop
        try (BufferedReader textFileReader = Files.newBufferedReader(Paths.get(System.getProperty("user.home") + "/Desktop/bridge_3.txt"))) {
            String line;
            String column;

            //skip empty lines until reading number of nodes from the text file
            while ((line = textFileReader.readLine()) != null) {

                int totalNodes = Integer.parseInt(line.trim()); //read number of nodes from text file as an integer number

                Graph graph = new Graph(totalNodes);

                if (graph.getNumberOfNodes() != 0) {
                    //skip empty lines between the edge values text file
                    while ((column = textFileReader.readLine()) != null) {
                        String[] values = column.split(" ");
                        int source = Integer.parseInt(values[0]);
                        int target = Integer.parseInt(values[1]);
                        float capacity = Float.parseFloat(values[2]);

                        graph.addEdgeFromFile(source, target, capacity); //add edge

                    }
                    System.out.println("Graph:");
                    graph.displayGraph(); //print graph

                    //perform Ford-Fulkerson Algorithm to find max flow
                    System.out.println(FordFulkerson(graph, 0, (totalNodes - 1)));
                }

                long finishingTime = System.nanoTime(); //get finishing time in nano seconds
                double elapsedTimeInMilliseconds = (finishingTime - startingTime) / 1000000; //count time in milliseconds
                double elapsedTimeInSeconds = elapsedTimeInMilliseconds / 1000; //count time in seconds

                System.out.println("\nTime elapsed = " + elapsedTimeInSeconds + " seconds");
            }
        } catch (IOException ex) {
            System.out.println("Could not find text file");
        }
    }

    //Ford Fulkerson Algorithm
    public static float FordFulkerson(Graph graph, int source, int target) {
        // error checking
        if (source == target) {
            return 0;
        }
        int nodes = graph.getNumberOfNodes();

        // create residual graph
        Graph rGraph = new Graph(nodes);
        for (int u = 0; u < nodes; u++) {
            for (int v = 0; v < nodes; v++) {
                rGraph.getAdjacencyMatrix()[u][v] = graph.getAdjacencyMatrix()[u][v];
            }
        }

        int[] parent = new int[nodes]; // filled by BFS

        float maxFlow = 0; // max flow value

        System.out.println("\nAugmenting paths: ");

        // while there is a path from source to target loop
        while (breadthFirstSearch(rGraph, source, target, parent)) {
            List augmentingPath = new ArrayList();

            //store path flow
            float pathFlow = Float.MAX_VALUE;

            // find max flow of path filled by the breadthFirstSearch method
            for (int v = target; v != source; v = parent[v]) {
                augmentingPath.add(v);
                int u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph.getAdjacencyMatrix()[u][v]);
            }

            // update capacities of the residual graph
            // reverse edges
            for (int v = target; v != source; v = parent[v]) {
                int u = parent[v];
                rGraph.getAdjacencyMatrix()[u][v] -= pathFlow;
                rGraph.getAdjacencyMatrix()[v][u] += pathFlow;
            }

            augmentingPath.add(source);
            Collections.reverse(augmentingPath);

            //print augmenting paths
            for (int i = 0; i < augmentingPath.size(); i++) {
                System.out.print(augmentingPath.get(i) + " -> ");
            }
            System.out.println();

            // add path flow to the max flow
            maxFlow += pathFlow;
        }

        System.out.println();
        System.out.print("\nThe maximum possible flow = ");

        return maxFlow;
    }

    //Breadth-First Search
    public static boolean breadthFirstSearch(Graph residualGraph, int source, int target, int[] parent) {
        //an array to store the visited nodes
        boolean[] isVisited = new boolean[residualGraph.getNumberOfNodes()];
        for (int k = 0; k < residualGraph.getNumberOfNodes(); k++) {
            isVisited[k] = false;
        }

        LinkedList<Integer> queue = new LinkedList<>();

        // visit source
        queue.add(source);
        isVisited[source] = true;
        parent[source] = -1;

        // loop through all nodes
        while (!queue.isEmpty()) {
            int i = queue.poll();
            // check neighbours of the node i
            for (Integer j : residualGraph.neighbours(i)) {
                //visit if not visited
                if ((!isVisited[j]) && (residualGraph.getAdjacencyMatrix()[i][j] > 0)) {
                    queue.add(j);
                    isVisited[j] = true;
                    parent[j] = i;
                }
            }
        }

        // return boolean that tells us if we ended up at the destination node
        return isVisited[target];
    }

}