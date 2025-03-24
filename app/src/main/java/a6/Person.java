package a6;

/**
 * A class representing a person -- suitable for extending into interesting
 * kinds of people -- like students or truck-drivers.
 */
public class Person {

    // ********** instance variables ********** //
    private String name;
    private char sex;
    private int age;
    private int height;
    private int weight;

    // ********** constructors ********** //
    /**
     * Create a Person with the given name.
     *
     * @param initialName this Person's name
     * @param 
     */
    public Person(String initialName, char initialSex, 
        int initialAge, int initialHeight, int initialWeight) {
        name = initialName;
        sex = initialSex;
        age = initialAge;
        height = initialHeight;
        weight = initialWeight;
    }

    /**
     * Create a person with place holder data.
     */
    public Person() {
        this("Baby Human", 'X', 0, 20, 7);
    }

    // ********** accessors and mutators ********** //
    /**
     * Change this Person's name.
     *
     * @param newName this Person's new name.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Return this Person's name.
     *
     * @return this Person's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Change this Person's sex.
     * 
     * @param newSex this Person's sex.
     */
    public void setSex(char newSex){
        sex = newSex;
    }

    /**
     * Return this Person's sex.
     * 
     * @return this Person's sex.
     */
    public char getSex(){
        return sex;
    }

    /**
     * Change this Person's age.
     * 
     * @param newAge this Person's age.
     */
    public void setAge(int newAge){
        age = newAge;
    }

    /**
     * Return this Person's age.
     * 
     * @return this Person's age.
     */
    public int getAge(){
        return age;
    }

    /**
     * Change this Person's height.
     * 
     * @param newHeight this Person's height.
     */
    public void setHeight(int newHeight){
        height = newHeight;
    } 

    /**
     * Return this Person's height.
     * 
     * @return this Person's height.
     */
    public int getHeight(){
        return height;
    }

    /**
     * Change this Person's weight.
     * 
     * @param newWeight this Person's weight.
     */
    public void setWeight(int newWeight){
        weight = newWeight;
    } 

    /**
     * Return this Person's weight.
     * 
     * @return this Person's weight.
     */
    public int getWeight(){
        return weight;
    }

    // ********** instance methods ********** //

    /**
     * Provide a simple report on this Person.
     */
    public void writeOutput() {
        System.out.println("Name: " + name);
    }

    /**
     * Check whether this Person has the same name as another.
     *
     * @param other the other Person (who possibly has the same name).
     * @return true if these people's names are identical; false otherwise.
     */
    public boolean hasSameName(Person other) {
        if (other == null) return false;
        return name.equalsIgnoreCase(other.name);
    }

    /**
     * Create a String representing this Person.
     *
     * @return a String with this Person's name.
     */
    @Override
    public String toString() {
        return "Person: " + name;
    }

}

