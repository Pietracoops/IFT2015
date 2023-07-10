import java.util.*;

public class Graph 
{
    ArrayList<String> vertices;
    HashMap<    , ArrayList<Edge>> adjacencyList;

    public Graph(ArrayList<String> vertices) 
    {
        this.vertices = vertices;
        adjacencyList = new HashMap<String, ArrayList<Edge>>();

        System.out.println(vertices.size());

        for (String vertex : this.vertices) 
        {
            //System.out.println(vertex);
            adjacencyList.put(vertex, new ArrayList<Edge>());
        }
    }

    public void addEdge(String name, String vertex0, String vertex1, Integer weight) 
    {
        System.out.println(vertex0);
        System.out.println(vertex1);

        for (Map.Entry<String, ArrayList<Edge>> entry : adjacencyList.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue().size());
        }

        ArrayList<Edge> e0 = adjacencyList.get(vertex0);
        System.out.println(e0.size());
        e0.add(new Edge(name, vertex0, vertex1, weight));
        adjacencyList.get(vertex1).add(new Edge(name, vertex1, vertex0, weight));
    }

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
}