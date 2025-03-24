package a6;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.print.event.PrintServiceAttributeEvent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Summarises and handles a CSV file, including downloading
 */
public class Summary {

    public static final String URL = "https://people.math.sc.edu/Burkardt/datasets/csv/biostats.csv";

    /**
     * Downloads a file from the specified URL and saves it to the given output
     * file.
     *
     * @param link    The URL of the file to be downloaded.
     * @param outFile The path where the downloaded file should be saved.
     * @throws IOException          If an I/O error occurs while reading from the
     *                              URL or
     *                              writing to the file.
     * @throws InterruptedException If the operation is interrupted while
     *                              waiting for the HTTP response.
     */
    public static void downloadFile(String link, String outFile) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            try (PrintWriter outputStream = new PrintWriter(outFile)) {
                outputStream.print(response.body());
            }
        } else {
            throw new IOException("Failed to download file, HTTP response code: "
                    + response.statusCode());
        }
    }


    public static void main(String[] args) throws IOException {

        switch (args[0]) {
            case "download" -> {
                try {
                    downloadFile(URL, args[1]);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                System.out.println("Downloaded successully: " + args[1]);
            }

            case "summary" -> {

                String[] csvContentArray = readFullTextContents(args[1]);

                Person[] people = parsePersonCSV(csvContentArray);

                // Compute summary data

                int numPeople = people.length; // Outputted

                int minAge = Integer.MAX_VALUE; // Outputted
                int maxAge = Integer.MIN_VALUE; // Outputted
                int sumAge = 0; // Temporary
                String youngestPersonsName = ""; // Outputted
                String oldestPersonsName = ""; // Outputted
                
                int maxHeight = Integer.MIN_VALUE; // Outputted
                String tallestPersonsName = ""; // Outputted
                
                int minWomansHeight = Integer.MAX_VALUE; // Temporary
                String shortestWomansName = ""; // Outputted

                for (Person person : people){
                    
                    int age = person.getAge();

                    if (age < minAge){
                        minAge = age;
                        youngestPersonsName = person.getName();
                    }

                    if (age > maxAge){
                        maxAge = age;
                        oldestPersonsName = person.getName();
                    }

                    sumAge += age;

                    int height = person.getHeight();

                    if (height > maxHeight){
                        maxHeight = height;
                        tallestPersonsName = person.getName();
                    }

                    if (person.getSex() == 'F' && height < minWomansHeight){
                        minWomansHeight = height;
                        shortestWomansName = person.getName();
                    }
                }

                float averageAge = sumAge / (float)numPeople; // Outputted

                System.out.println("Summary:");
                System.out.println(String.format("Total number of records: %i", numPeople));
                System.out.println(String.format("Youngest person: %s (%i years old)", youngestPersonsName, minAge));
                System.out.println(String.format("Oldest person: %s (%i years old)", oldestPersonsName, maxAge));
                System.out.println(String.format("Average age: %f", averageAge));
                System.out.println(String.format("Tallest person: %s (%i inches)", tallestPersonsName, maxHeight));
                System.out.println(String.format("Shortest female: %s", shortestWomansName));

            }

            case "print" -> {
                //TODO

            }
            
            default -> {
                System.out.println("Unknown command");
            }

        }

    }

    /**
     * Returns an array of all lines of text within a file
     * @param filepath The path to the file
     * @return The lines of the file, in an array
     */
    public static String[] readFullTextContents(String filepath){

        Scanner fileReader = new Scanner(filepath);

        ArrayList<String> fileContent = new ArrayList<String>();

        while (fileReader.hasNextLine()){
            fileContent.add(fileReader.nextLine());
        }

        fileReader.close();

        // Cast back to Array for use with static array'd methods

        Object[] fileObjectArray = fileContent.toArray();

        String[] fileContentArray = new String[fileObjectArray.length];

        for (int i = 0; i < fileObjectArray.length; i++){
            fileContentArray[i] = fileObjectArray[i].toString();
        }

        return fileContentArray;
    }

    /**
     * Parses a csv string into an array of type Person
     * 
     * @param csv A string of the CSV contents
     * @return the array of Persons
     */
    public static Person[] parsePersonCSV(String[] csv){

        Person[] output;

        output = new Person[csv.length];

        //Debug
        for (String line : csv){
            System.out.println(line);
        }
        // Somehow blank?
        // TODO: Fix this

        for (int i = 1; i < csv.length; i++)
        {

            String[] parts = csv[i].split(",\\w*");
            // In order:
            // Name, Sex, Age, Height (in), Weight (lbs)

            // Whilst technically unnecessary to unpack like this, it clarifies what's happening

            String name = parts[0];
            char sex = parts[1].charAt(0);
            int age = Integer.parseInt(parts[2]);
            int height = Integer.parseInt(parts[3]);
            int weight = Integer.parseInt(parts[4]);

            //Debug
            System.out.println(String.format("%s, %c, %i, %i, %i", name, sex, age, height, weight));

            output[i] = new Person(name, sex, age, height, weight);

        }

        return output;

    }

}
                                      
                
                