import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.*;

public class Tp3 {

    /**
     * Processes a file line by line and performs operations on each line.
     *
     * @param filePath the path of the file to be processed
     * @param outputPath the output path of the processed file
     */
    public static void processFile(String filePath, String outputPath)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            FileWriter fileWriter = new FileWriter(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            ArrayList<String> vertices = new ArrayList<String>();
            ArrayList<Edge> edges = new ArrayList<Edge>();
            String line;
            int mode = 0;
            while ((line = reader.readLine()) != null) 
            {
                line = line.replace(";", "");
                if (line.contains("---"))
                {
                    if (mode == 1) break;
                    mode++;
                    continue;
                }

                switch (mode)
                {
                    case 0:
                        line = line.replaceAll("\\s", "");
                        vertices.add(line);
                        break;
                    case 1:
                        // Parse the line
                        // rue0 : a b 4 ; 
                        String[] parts = line.split(" ");
                        // Remove all empty spaces
                        List<String> tmpList = new ArrayList<>(Arrays.asList(parts));
                        tmpList.removeIf(String::isEmpty);
                        parts = tmpList.toArray(new String[0]);
                        Edge edgeObj = new Edge(parts[0], parts[2], parts[3], Integer.parseInt(parts[4]));
                        edges.add(edgeObj);
                        break;
                }
            }

            Carte carte = new Carte(vertices);
            for (Edge edge : edges) carte.addRue(edge);

            List<Edge> mst = carte.primJarnikAlgorithm();
            Collections.sort(mst, Edge.vertexComparator);

            for (String vertex : vertices)
            {
                bufferedWriter.write(vertex + "\n");
            }

            // Print the minimum spanning tree
            int totalWeight = 0;
            for (Edge edge : mst)
            {
                bufferedWriter.write(edge.getName() + "\t" + edge.getVertex0() + "\t" + edge.getVertex1() + "\t" + edge.getWeight() + "\n");
                totalWeight += edge.getWeight();
            }
            bufferedWriter.write("---\n");
            bufferedWriter.write(totalWeight + "\n");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Perform program input checking
     *
     * @param  args    the program arguments
     * @return         nothing
     */
    public static void main(String[] args) {
        // Perform program input checking
        if (args.length != 2)
        {
            System.out.println("Incorrect number of arguments. Exiting");
            return;
        }
        String inputFileStr =  args[0];
        String outputFileStr = args[1];

        // Get Current Working Directory
        String currentDirectory = System.getProperty("user.dir");
        Path existingDirectory = Paths.get(currentDirectory);
        Path fileFullPath = existingDirectory.resolve(inputFileStr);
        File inputFileObj = new File(fileFullPath.toString());

        // Check if input file exists
        if (!inputFileObj.exists())
        {
            System.out.println(inputFileStr + " does not exist. Exiting");
            return;
        }

        processFile(fileFullPath.toString(), outputFileStr);

    }
}