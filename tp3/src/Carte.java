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

    void addRue(Edge rue)
    {
        graph.addEdge(rue.name, rue.vertex0, rue.vertex1, rue.weight);
    }

    public void printCarte()
    {
        graph.printGraph();
    }
}
