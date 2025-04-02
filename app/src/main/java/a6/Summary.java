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

    /**
     * Summarises a csv file to the standard output
     * @param filePath The path of the pile
     */
    public static void summarise(String filePath){
        String[] csvContentArray = readFullTextContents(filePath);

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
        System.out.println(String.format("Total number of records: %d", numPeople));
        System.out.println("Youngest person: " + youngestPersonsName + String.format(" (%d years old)", minAge));
        System.out.println("Oldest person: " + oldestPersonsName + String.format(" (%d years old)", maxAge));
        System.out.println(String.format("Average age: %f", averageAge));
        System.out.println("Tallest person: " + tallestPersonsName + String.format(" (%d inches)", maxHeight));
        System.out.println("Shortest female: " + shortestWomansName);
    }

    /**
     * Prints the contents of an array of Person[]
     * @param people the array to print
     */
    public static void printPersonArray(Person[] people){
        for (Person person : people){
            System.out.println(
                padString(person.getName(), 11) + 
                padString(((Character)person.getSex()).toString(), 11) +
                padString(((Integer)person.getAge()).toString(), 11) +
                padString(((Integer)person.getHeight()).toString(), 11) +
                padString(((Integer)person.getWeight()).toString(), 11)
            );
        }
    }

    /**
     * Prints the sorted array of Person[] according to the sort mode and reversal
     * @param people The array of Person[] to sort and print
     * @param sortMode The mode to sort by - "n" is name, "a" is age, "h" is height
     * @param reversed Whether to reverse the order
     */
    public static void printSortedPersonArray(Person[] people, String sortMode, boolean reversed){

        Comparator<Person> requiredComparator;

        switch (sortMode){
            case "n" -> {
                requiredComparator = Person.BY_NAME;
            }
            case "a" -> {
                requiredComparator = Person.BY_AGE;
            }
            case "h" -> {
                requiredComparator = Person.BY_HEIGHT;
            }
            default -> {
                throw new IllegalArgumentException("Did not recognise sorting mode \"" + sortMode + "\"");
            }
        }

        Arrays.sort(people, requiredComparator);

        if (reversed){
            Person temp;
            for (int i = 0; i < people.length / 2; i++){
                temp = people[people.length-i-1];
                people[people.length-i-1] = people[i];
                people[i] = temp;
            }
        }

        printPersonArray(people);

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
                summarise(args[1]);
            }

            case "print" -> {

                String[] csvContentArray = readFullTextContents(args[1]);

                Person[] people = parsePersonCSV(csvContentArray);

                if (args.length > 2){
                    boolean reversed = args.length > 3 && args[3] == "true";
                    printSortedPersonArray(people, args[2], reversed);
                }

                else{
                    printPersonArray(people);
                }
                
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

        ArrayList<String> fileContent = new ArrayList<String>();

        try (Scanner fileReader = new Scanner(new File(filepath))) {

            while (fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                if (line != "") fileContent.add(line);
            }

        } catch (FileNotFoundException e) {

            System.out.println("Error opening the file " + filepath);

        }

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

        output = new Person[csv.length-1];

        for (int i = 1; i < csv.length; i++)
        {

            String[] parts = csv[i].split(",");
            for (int j = 1; j < parts.length; j++){
                // Remove whitespace
                parts[j] = parts[j].strip();
            }
            // In order:
            // Name, Sex, Age, Height (in), Weight (lbs)

            // Whilst technically unnecessary to unpack like this, it clarifies what's happening

            String name = parts[0];
            name = name.substring(1, name.length()-1); // Trim quotes
            char sex = parts[1].charAt(1);
            int age = Integer.parseInt(parts[2]);
            int height = Integer.parseInt(parts[3]);
            int weight = Integer.parseInt(parts[4]);

            output[i-1] = new Person(name, sex, age, height, weight);

        }

        return output;

    }

    public static String padString(String inp, int space){
        return inp + getPadding(space - inp.length());
    }

    public static String getPadding(int space){
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < space; i++) out.append(" ");

        return out.toString();
    }

}
                                      
                
                