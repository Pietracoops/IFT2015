import java.util.Date;
import java.util.*;
        
public class Carte
{
    Graph graph;

    public Carte(ArrayList<String> vertices) 
    { 
        //System.out.println(vertices.size());
        this.graph = new Graph(vertices);
    }

    /**
     * Adds a rue to the graph.
     *
     * @param  rue  the edge to be added
     */
    void addRue(Edge rue)
    {
        graph.addEdge(rue.name, rue.vertex0, rue.vertex1, rue.weight);
    }

    /**
     * Calculates the minimum spanning tree of a graph using Prim-Jarnik algorithm.
     *
     * @return         	The list of edges in the minimum spanning tree.
     */
    public List<Edge> primJarnikAlgorithm()
    {

        Set<String> visitedNodes = new HashSet<>(); // O(1)
        PriorityQueue<Edge> priorityQ = new PriorityQueue<>((edge1, edge2) -> { // O(1)
            if (edge1.getWeight() != edge2.getWeight()) {
                return Integer.compare(edge1.getWeight(), edge2.getWeight());
            } else {
                return edge1.getVertex0().compareTo(edge2.getVertex0());
            }
        });

        List<Edge> minimumSpanningTree = new ArrayList<>(); // O(1)

        String startVertex = graph.getVertices().get(0); // O(1)
        visitedNodes.add(startVertex); // O(1)

        List<Edge> edges = graph.getAdjacencyList().get(startVertex); // O(1)
        priorityQ.addAll(edges); // O(Elogn) where E is the number of edges and n is the number of entries in queue


        while (!priorityQ.isEmpty()) // Worst case O(E)
        {
            Edge minEdge = priorityQ.remove(); // O(1)
            String v0 = minEdge.getVertex0(); // O(1)
            String v1 = minEdge.getVertex1(); // O(1)

            // Skip if both vertices are already visitedNodes
            if (visitedNodes.contains(v0) && visitedNodes.contains(v1)) // O(1)
            {
                continue;
            }

            // Add the edge to the minimum spanning tree
            minimumSpanningTree.add(minEdge); // O(1)

            // Mark the new vertex as visited
            String newVertex = visitedNodes.contains(v0) ? v1 : v0; // O(1)
            visitedNodes.add(newVertex); // O(1)

            // Add all edges incident to the new vertex to the priority queue
            edges = graph.getAdjacencyList().get(newVertex); // O(1) because of hashmap
            priorityQ.addAll(edges); // O(Elogn) where k is the number of edges and n is the number of entries in queue
        }

        return minimumSpanningTree;
    }

    /**
     * Prints the carte by invoking the `printGraph()` method of the `graph` object.
     *
     * 
     */
    public void printCarte()
    {
        graph.printGraph();
    }


}
