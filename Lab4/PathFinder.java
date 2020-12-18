
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.function.Function;


/**
 * This is a class that can find paths in a given graph.
 * 
 * There are several methods for finding paths, 
 * and they all return a PathFinder.Result object.
 */

public class PathFinder<Node> {

    private DirectedGraph<Node> graph;
    private long startTimeMillis;


    /**
     * Creates a new pathfinder for the given graph.
     * @param graph  the graph to search
     */
    public PathFinder(DirectedGraph<Node> graph) {
        this.graph = graph;
    }


    /**
     * The main search method, taking the search algorithm as input.
     * @param  algorithm  "random", "ucs" or "astar"
     * @param  start  the start node
     * @param  goal   the goal node
     */
    public Result search(String algorithm, Node start, Node goal) {
        startTimeMillis = System.currentTimeMillis();
        switch (algorithm) {
        case "random": return searchRandom(start, goal);
        case "ucs":    return searchUCS(start, goal);
        case "astar":  return searchAstar(start, goal);
        }
        throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
    }


    /**
     * Perform a random walk in the graph, hoping to reach the goal.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchRandom(Node start, Node goal) {
        int iterations = 0;
        LinkedList<Node> path = new LinkedList<>();
        double cost = 0.0;
        Random random = new Random();

        Node current = start;
        path.add(current);
        while (iterations < 1e6 && current != null) {
            iterations++;
            if (current.equals(goal)) {
                return new Result(true, start, current, cost, path, iterations);
            }

            List<DirectedEdge<Node>> neighbours = graph.outgoingEdges(start);
            if (neighbours == null || neighbours.size() == 0) {
                break;
            } else {
                DirectedEdge<Node> edge = neighbours.get(random.nextInt(neighbours.size()));
                cost += edge.weight();
                current = edge.to();
                path.add(current);
            }
        }
        return new Result(false, start, goal, -1, null, iterations);
    }


    /**
     * Run the Uniform Cost Search algorithm for finding the shortest path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchUCS(Node start, Node goal) {
        int iterations = 0;
        // a queue that prioritze the cheapest nodes
        Queue<PQEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble((entry) -> entry.costToHere));
        // a set storing the nodes already visited
        Set<Node> visited = new HashSet<>();
        // add the starting node to the queue
        pqueue.add(new PQEntry(start, 0, null));
        // loop until we find our way or the queue is empty
        while (!pqueue.isEmpty()){
            iterations ++;
            // check if next queue entry represents the goal, if so return the result                                        
            PQEntry entry = pqueue.remove();
            if (entry.node.equals(goal)){                        
                List<Node> path = extractPath(entry);
                return new Result(true, start, goal, entry.costToHere, path, iterations);
                
            }
            // if we are first time visitors to the node, we add the nodes edging to this as entries in our queue
            if (!visited.contains(entry.node)){
                for(DirectedEdge<Node> edge : graph.outgoingEdges(entry.node)){
                    // the costToHere for the edging node is the cost to the current + the edge weight
                    double costToNext = entry.costToHere + edge.weight();              
                    pqueue.add(new PQEntry(edge.to(), costToNext, entry));
                }
            }
            visited.add(entry.node); 
            if (iterations == 1000000) break;
        }
        // if the loop ends, there's no way from start to goal
        return new Result(false, start, goal, -1, null, iterations);
    }

    /**
     * Run the A* algorithm for finding the shortest path. TODO A bit faster than teacher example, why?
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchAstar(Node start, Node goal) {
        int iterations = 0;
        Queue<PQEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble((entry) -> entry.costToHere));
        // accurate records of best costs
        Map<Node, Double> knownCosts = new HashMap<>();
        //Map<Node, Double> guessedCosts = new HashMap<>();
        
        pqueue.add(new PQEntry(start, Double.MAX_VALUE, null));
        knownCosts.put(start, 0.0);

        while (!pqueue.isEmpty()){
            iterations ++;
            PQEntry entry = pqueue.remove();
            if (entry.node.equals(goal)){
                List<Node> path = extractPath(entry);
                return new Result(true, start, goal, knownCosts.get(entry.node), path, iterations);
                
            }
            
            for(DirectedEdge<Node> edge : graph.outgoingEdges(entry.node)){
                // this is where a* search differ from ucs search
                // TODO knownHere is the accurate cost from start to entry node
                double knownHere = knownCosts.get(edge.from());
                // newThere is an accurate cost from start to edging node
                double newThere = knownHere + edge.weight();

                // if this new accurate path is not shorter than the old one, don't store it!
                /*if (knownCosts.containsKey(edge.to())){
                    if (newThere >= knownCosts.get(edge.to())){
                        break;
                    }
                }*/

                if (!knownCosts.containsKey(edge.to())
                || newThere < knownCosts.get(edge.to())){

                    knownCosts.put(edge.to(), newThere);

                    //guessedCosts.put(edge.to(), newThere + graph.guessCost(edge.to(), goal));



                    // we add the edging node to the queue with the distance of the shortest distance from start node to edging node
                    // + the guess of the distance edging node -> goal
                    pqueue.add(new PQEntry(edge.to(), newThere + graph.guessCost(edge.to(), goal), entry));
                }


            }
            //if (iterations == 1000000) break;
        }

        return new Result(false, start, goal, -1, null, iterations);
    }


    /**
     * Extract the final path from start to goal, from the final priority queue entry.
     * @param entry  the final priority queue entry
     * @return the path from start to goal as a list of nodes
     */
    private List<Node> extractPath(PQEntry entry) {
        LinkedList<Node> path = new LinkedList<>(); //create path to add to and later return
        PQEntry current = entry; //set the current node
        while (current.backPointer != null){ //we loop until we come to the first node, which has backpointer null
            path.addFirst(current.node); //We add the current node FIRST to the path
            current = current.backPointer; //Set the new current to the last current's backpointer (we move a step "back")
        }
        path.addFirst(current.node); //We add the first node as well!
        return path; //return the path :)
    }


    /**
     * Entries to put in the priority queues in {@code searchUCS} and {@code searchAstar}.
     */
    private class PQEntry {
        public final Node node;
        public final double costToHere;
        public final PQEntry backPointer;

        public final double estimatedCostToGoal;

        PQEntry(Node n, double c, PQEntry bp) {
            node = n;
            costToHere = c;
            backPointer = bp;
        }
    }


    /**
     * The internal class for search results.
     */
    public class Result {
        public final boolean success;
        public final Node start;
        public final Node goal;
        public final double cost;
        public final List<Node> path;
        public final int iterations;
        public final double elapsedTime;

        public Result(boolean success, Node start, Node goal, double cost, List<Node> path, int iterations) {
            this.success = success;
            this.start = start;
            this.goal = goal;
            this.cost = cost;
            this.path = path;
            this.iterations = iterations;
            this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        }

        @Override
        public String toString() {
            String s = "";
            if (iterations <= 0) {
                s += String.format("ERROR: You have to iterate through at least some nodes!\n");
            }
            s += String.format("Loop iterations: %d\n", iterations);
            s += String.format("Elapsed time: %s seconds\n", elapsedTime);
            if (success) {
                int len = path.size();
                s += String.format("Total cost from %s -> %s: %s\n", start, goal, cost);
                s += String.format("Total path length: %d\n", len);
                Function<List<Node>, String> joinPath = (path) ->
                    path.stream().map(Node::toString).collect(Collectors.joining(" -> "));
                if (len < 10) {
                    s += String.format("Path: %s",
                                       joinPath.apply(path));
                } else {
                    s += String.format("Path: %s -> ... -> %s",
                                       joinPath.apply(path.subList(0, 5)),
                                       joinPath.apply(path.subList(len-5, len)));
                }
            } else {
                s += String.format("No path found from %s to %s", start, goal);
            }
            return s;
        }
    }

}
