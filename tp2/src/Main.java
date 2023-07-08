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
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.*;

public class Main {

    static class MedicationComparator implements Comparator<String> {
        @Override
        public int compare(String entry1, String entry2) {
            String[] parts1 = entry1.split(" ");
            String[] parts2 = entry2.split(" ");

            String name1 = parts1[0];
            String name2 = parts2[0];
            String date1 = parts1[2];
            String date2 = parts2[2];

            if (name1.equals(name2)) {
                return date1.compareTo(date2);
            } else {
                return name1.compareTo(name2);
            }
        }
    }

    public static void processFile(String filePath, String outputPath)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            FileWriter fileWriter = new FileWriter(outputPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            BinaryTree btsStorage = new BinaryTree();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            List<Commande> commandeList = new ArrayList<Commande>();
            Map<String, Integer> commandeMap = new HashMap<>();
            Date currentDate = calendar.getTime();
            int prescriptionNum = 1;
            String line;
            int mode = -1;
            while ((line = reader.readLine()) != null) {


                if (line.contains("APPROV"))
                {
                    mode = 0;
                    continue;
                }
                else if (line.contains("DATE"))
                {
                    // Accept Current Date Change
                    mode = -1;
                    String[] parts = line.split(" ");
                    try
                    {
                        currentDate = format.parse(parts[1]);
                        String formattedDate = format.format(currentDate);
                        // Remove all expired nodes
                        btsStorage.removeExpiredNodes(currentDate);
                        if (commandeMap.size() == 0)
                        {
                            bufferedWriter.write(formattedDate + " OK\n");
                        }
                        else
                        {
                            bufferedWriter.write(formattedDate + " COMMANDES :\n");
                            Map<String, Integer> sortedMap = new TreeMap<>(commandeMap);
                            for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) 
                            {
                                bufferedWriter.write(entry.getKey() + " " + entry.getValue() + "\n");
                            }
                            // Clear the Command Map
                            commandeMap.clear();
                        }

                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }

                }
                else if (line.contains("STOCK"))
                {
                    mode = 2;
                    // Traverse tree and print out all information (Name - Quantity - Expire date)
                    String strOutput = "";
                    strOutput = btsStorage.traverseInOrder(btsStorage.root, strOutput, currentDate);
                    bufferedWriter.write("STOCK " + format.format(currentDate) + "\n");
                    String[] splitArray = strOutput.split("\n");
                    Arrays.sort(splitArray);
                    Arrays.sort(splitArray, new MedicationComparator());
                    for (String element : splitArray) {
                        bufferedWriter.write(element + "\n");
                    }
                    

                }
                else if (line.contains("PRESCRIPTION"))
                {
                    mode = 3;
                    bufferedWriter.write("PRESCRIPTION " + prescriptionNum + "\n");
                    prescriptionNum++;
                    continue;
                }
                
                if (line.contains(";"))
                {
                    if (mode == 0)
                    {
                        bufferedWriter.write("APPROV OK");
                    }
                    bufferedWriter.write("\n");
                    mode = -1;
                    continue;
                }

                if (mode == 3)
                {
                    // Prescription
                    line = line.replaceAll("\t", " ");
                    String[] parts = line.split(" ");
                    List<String> tmpList = new ArrayList<>(Arrays.asList(parts));
                    tmpList.removeIf(String::isEmpty);
                    parts = tmpList.toArray(new String[0]);

                    
                    String name = parts[0];
                    int Quantity = Integer.parseInt(parts[1]) * Integer.parseInt(parts[2]);
                    Commande commObj = new Commande(name, Quantity, currentDate);

                    // check if it is in binary tree structure
                    if (btsStorage.processNode(commObj.Name, commObj.Quantity, commObj.RequiredDate, currentDate))
                    {
                        // If in tree, subtract the current amount from
                        bufferedWriter.write(commObj.Name + " " + parts[1] + " " + parts[2] + "  " + "OK\n");
                    }
                    else
                    {
                        // Check if name already in list and add onto it
                        if (commandeMap.containsKey(commObj.Name))
                        {
                            int newCommandeValue = commandeMap.get(commObj.Name) + commObj.Quantity;
                            commandeMap.put(commObj.Name, newCommandeValue);
                        }
                        else
                        {
                            commandeMap.put(commObj.Name, commObj.Quantity);
                        }

                        bufferedWriter.write(commObj.Name + " " + parts[1] + " " + parts[2] + "  " + "COMMANDE\n");
                    }
                }

                if (mode == 0)
                {
                    // Approv
                    line = line.replaceAll("\t", " ");
                    String[] parts = line.split(" ");

                    String name = parts[0];
                    int Quantity = Integer.parseInt(parts[1]);
                    Date ExpireDate = currentDate;

                    // Adding to the stock
                    try 
                    {
                        ExpireDate = format.parse(parts[2]);
                    } catch (ParseException e) 
                    {
                        e.printStackTrace();
                    }

                    Node medObj = new Node(ExpireDate, name, Quantity);

                    // check if it is in binary tree structure
                    Node foundNode = btsStorage.containsNode(ExpireDate);
                    if (foundNode != null)
                    {
                        // If in tree, append the Medication to the medinfo object
                        foundNode.medicationStores.add(medObj.medicationStores.get(0));
                        // Sort the list of medObj objects based on the Date parameter
                        //foundNode.medicationStores.sort(new Comparator<Medication>()
                        // {
                        //     @Override
                        //     public int compare(Medication obj1, Medication obj2) {
                        //         return obj1.ExpireDate.compareTo(obj2.ExpireDate);
                        //     }
                        // });
                    }
                    else
                    {
                        //btsStorage.add(medinfoObj);
                        btsStorage.addBalanced(medObj);
                    }


                }

            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
        if (!inputFileObj.exists()) {
            System.out.println(inputFileStr + " does not exist. Exiting");
            return;
        }

        // long startTime = System.nanoTime(); // Start timer (uncomment this for timer)

        processFile(fileFullPath.toString(), outputFileStr);


        // // Uncomment lines below for timer
        // long endTime = System.nanoTime(); // End timer
        // long elapsedTime = endTime - startTime;
        // double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0;
        // System.out.println("Terminated in " + elapsedTimeInSeconds + " seconds");
    }
}