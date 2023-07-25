import java.util.*;

public class Graph 
{
    ArrayList<String> vertices;
    HashMap<String, ArrayList<Edge>> adjacencyList;

    public Graph(ArrayList<String> vertices) 
    {
        this.vertices = vertices;
        Collections.sort(this.vertices);
        adjacencyList = new HashMap<String, ArrayList<Edge>>();

        for (String vertex : this.vertices) 
        {
            adjacencyList.put(vertex, new ArrayList<Edge>());
        }
    }

    /**
     * Adds an edge between two vertices with the specified name, vertex0, vertex1, and weight.
     *
     * @param  name      the name of the edge
     * @param  vertex0   the starting vertex of the edge
     * @param  vertex1   the ending vertex of the edge
     * @param  weight    the weight of the edge
     */
    public void addEdge(String name, String vertex0, String vertex1, Integer weight) 
    {
        ArrayList<Edge> e0 = adjacencyList.get(vertex0);
        e0.add(new Edge(name, vertex0, vertex1, weight));    // Add edge
        adjacencyList.get(vertex1).add(new Edge(name, vertex1, vertex0, weight));   // Add edge other direction
    }

    /**
     * Prints the graph by iterating over the vertices and the adjacency list.
     *
     * @param  None
     * @return None
     */
    public void printGraph()
    {
        for (int i = 0; i < vertices.size(); i++)
        {
            System.out.println(vertices.get(i));
        }

        System.out.println("---");

        //rue0 : a b 4 ; 
        for (Map.Entry<String, ArrayList<Edge>> entry : adjacencyList.entrySet())
        {
            for (Edge edge : entry.getValue())
            {
                System.out.println(edge.name + " : " + edge.vertex0 + " " + edge.vertex1 + " " + edge.weight);
            }
        }

        System.out.println("---");
        
    }

    /**
     * Retrieves the adjacency list of the graph.
     *
     * @return  a HashMap containing the adjacency list, where the keys are vertices 
     *          represented as String objects and the values are ArrayLists of Edge 
     *          objects representing the edges connected to each vertex.
     */
    public HashMap<String, ArrayList<Edge>> getAdjacencyList()
    {
        return adjacencyList;
    }

    /**
     * Retrieves the list of vertices.
     *
     * @return  an ArrayList of strings representing the vertices
     */
    public ArrayList<String> getVertices()
    {
        return vertices;
    }
}