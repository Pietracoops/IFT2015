public class Edge 
{
    String name;
    String vertex0;
    String vertex1;
    int weight;

    public Edge(String name, String vertex0, String vertex1, Integer weight)
    {
        this.name = name;
        this.vertex0 = vertex0;
        this.vertex1 = vertex1;
        this.weight = weight;
    }
}