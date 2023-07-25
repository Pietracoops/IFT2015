import java.util.Comparator;
import java.util.Arrays;

public class Edge implements Comparable<Edge> 
{
    String name;
    String vertex0;
    String vertex1;
    int weight;

    // Comparator to compare edges based on vertex0 and vertex1 in alphabetical order
    public static Comparator<Edge> vertexComparator = new Comparator<Edge>() 
    {
        @Override
        public int compare(Edge edge1, Edge edge2) {
            int vertex0Comparison = edge1.getVertex0().compareTo(edge2.getVertex0());
            if (vertex0Comparison != 0) {
                return vertex0Comparison;
            }
            return edge1.getVertex1().compareTo(edge2.getVertex1());
        }
    };

    @Override
    public int compareTo(Edge other) 
    {
        return this.getVertex0().compareTo(other.getVertex0());
    }

    public Edge(String name, String vertex0, String vertex1, Integer weight) 
    {
        this.name = name;
        this.vertex0 = vertex0;
        this.vertex1 = vertex1;
        this.weight = weight;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getVertex0() 
    {
        return vertex0;
    }

    public void setVertex0(String vertex0) 
    {
        this.vertex0 = vertex0;
    }

    public String getVertex1() 
    {
        return vertex1;
    }

    public void setVertex1(String vertex1) 
    {
        this.vertex1 = vertex1;
    }

    public int getWeight() 
    {
        return weight;
    }

    public void setWeight(int weight) 
    {
        this.weight = weight;
    }

}