// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.text.DecimalFormat;
import java.math.RoundingMode;

public class Main {

    private static final double EARTH_RADIUS = 6371000; // Radius of the Earth in meters
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return distance;
    }
    public static List<Path> getInputFiles(String dataDirectory) {

        // Create a list to store the files
        List<Path> fileList = new ArrayList<>();

        try {

            // Get a stream of all files in the directory
            Files.walk(Paths.get(dataDirectory))
                    .filter(Files::isRegularFile)
                    .forEach(fileList::add);

            // Print the list of files
            for (Path file : fileList) {
                System.out.println(file);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileList;
    }

    public static CargoInfo processFile(String filePath)
    {
        CargoInfo cargoobj = new CargoInfo();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            Pattern pattern = Pattern.compile("\\((-?\\d+\\.\\d+),(-?\\d+\\.\\d+)\\)");
            int largestBoxStorage = 0;
            List<StorageInfo> storageLocationsObj = new ArrayList<>();
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line); // Print out the line
                if (counter == 0)
                {
                    String[] parts = line.split(" ");
                    cargoobj.boxesToTransport = Integer.parseInt(parts[0]);
                    cargoobj.remainingBoxes = Integer.parseInt(parts[0]);
                    cargoobj.truckCapacity = Integer.parseInt(parts[1]);
                }
                else
                {
                    String[] parts = line.split(" ");
                    for (int i = 0; i < parts.length; i += 2)
                    {
                        StorageInfo storageobj = new StorageInfo();
                        storageobj.boxStorage = Integer.parseInt(parts[i]);
                        if (storageobj.boxStorage > largestBoxStorage)
                        {
                            largestBoxStorage = storageobj.boxStorage;
                            cargoobj.indexLargestStorage = storageLocationsObj.size();
                        }
                        Matcher matcher = pattern.matcher(parts[i+1]);
                        if (matcher.find())
                        {
                            storageobj.latitude = Double.parseDouble(matcher.group(1));
                            storageobj.longitude = Double.parseDouble(matcher.group(2));
                        }
                        // Store into destination list
                        storageLocationsObj.add(storageobj);

                    }
                }
                counter += 1;
            }

            cargoobj.storageLocations = storageLocationsObj;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cargoobj;
    }


    public static List<StorageInfo> swap(List<StorageInfo> storageInfoArray, int index1, int index2)
    {
        if (index1 == index2) {
            return storageInfoArray; // No need to swap if the indices are the same
        }

        StorageInfo temp = storageInfoArray.get(index1); // Store element at index 1 temporarily

        storageInfoArray.set(index1, storageInfoArray.get(index2));
        storageInfoArray.set(index2, temp);

        return storageInfoArray;
    }

    public static String printData(double distance, int number_of_boxes, double latitude, double longitude)
    {
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String roundedNumber = df.format(distance);
        String text = "Distance:" + roundedNumber + "\tNumber of boxes:" + number_of_boxes + "\tPosition:(" + latitude + "," + longitude +")\n";
        System.out.println(text);
        return text;
    }

    public static int calculateRemainingStorage(CargoInfo cargoobj)
    {
        if (cargoobj.remainingBoxes >= 0)
        {
            return 0;
        }
        else
        {
            return (-cargoobj.remainingBoxes);
        }
    }


    public static void algorithm(CargoInfo cargoobj)
    {

        try {
            // Create a FileWriter object with the file path
            FileWriter fileWriter = new FileWriter(cargoobj.outputFile);

            // Create a BufferedWriter object to wrap the FileWriter
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Start by swapping the largest index to slot 1
            cargoobj.storageLocations = swap(cargoobj.storageLocations, 0, cargoobj.indexLargestStorage);
            cargoobj.remainingBoxes -= cargoobj.storageLocations.get(0).boxStorage;

            String text = "Truck Position: (" + cargoobj.storageLocations.get(0).latitude + "," + cargoobj.storageLocations.get(0).longitude + ")\n";
            System.out.println(text);
            bufferedWriter.write(text);
            text = printData(0.0, calculateRemainingStorage(cargoobj), cargoobj.storageLocations.get(0).latitude, cargoobj.storageLocations.get(0).longitude);
            bufferedWriter.write(text);
            if (calculateRemainingStorage(cargoobj) > 0)
            {
                bufferedWriter.close();
                return;
            }

            // Nearest Neighbor Algorithm
            for (int i = 0; i < cargoobj.storageLocations.size(); i++)
            {
                double distance = 99999999; // Arbitrarily large number
                int index = 0;
                for (int j = i+1; j < cargoobj.storageLocations.size(); j++)
                {
                    // Calculate distance between current node and secondary nodes
                    double calculatedDistance = calculateDistance(cargoobj.storageLocations.get(0).latitude, cargoobj.storageLocations.get(0).longitude,
                            cargoobj.storageLocations.get(j).latitude, cargoobj.storageLocations.get(j).longitude);

                    // System.out.println("Distance Calculated = " + calculatedDistance + " between coordinate ("+cargoobj.storageLocations.get(i).latitude + "," + cargoobj.storageLocations.get(i).longitude+ ") and ("+cargoobj.storageLocations.get(j).latitude+","+cargoobj.storageLocations.get(j).longitude+")");
                    if (calculatedDistance < distance)
                    {
                        // Update distance and index
                        distance = calculatedDistance;
                        index = j;
                    }
                    else if(calculatedDistance == distance)
                    {
                        // We must take one with lower latitude
                        if (cargoobj.storageLocations.get(i).latitude == cargoobj.storageLocations.get(j).latitude)
                        {
                            // If latitudes are same, we must take lower longitude
                            if (cargoobj.storageLocations.get(i).longitude == cargoobj.storageLocations.get(j).longitude)
                            {
                                // Update the index
                                index = j;
                            }

                        }
                        // Latitude of new point is smaller, update the index
                        else if (cargoobj.storageLocations.get(i).latitude > cargoobj.storageLocations.get(j).latitude)
                        {
                            index = j;
                        }

                    }
                }
                cargoobj.remainingBoxes -= cargoobj.storageLocations.get(index).boxStorage;
                if (distance == 99999999 && cargoobj.remainingBoxes != 0)
                {
                    bufferedWriter.close();
                    return;
                }
                text = printData(distance, calculateRemainingStorage(cargoobj), cargoobj.storageLocations.get(index).latitude, cargoobj.storageLocations.get(index).longitude);
                bufferedWriter.write(text);
                if (calculateRemainingStorage(cargoobj) > 0)
                {
                    bufferedWriter.close();
                    return;
                }
                // Perform the swap
                cargoobj.storageLocations = swap(cargoobj.storageLocations, i+1, index);
            }

            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
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


//        double dist1 = calculateDistance(45.4383, -73.8205,45.4977 ,-73.714);
//        double dist2 = calculateDistance(45.4383, -73.8205,45.5092 ,-73.5682);

        long startTime = System.nanoTime(); // Start timer
        // Begin file processing
        CargoInfo cargoobj = new CargoInfo();
        cargoobj = processFile(fileFullPath.toString());
        cargoobj.outputFile = outputFileStr;

        algorithm(cargoobj);

        long endTime = System.nanoTime(); // End timer
        long elapsedTime = endTime - startTime;
        double elapsedTimeInSeconds = elapsedTime / 1_000_000_000.0;

        System.out.println("Terminated in " + elapsedTimeInSeconds + " seconds");
    }
}