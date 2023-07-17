package pandemic;


/**
 * The different types of disease
 */
public class Disease {
    /** Name of the disease (for readability) */
    private String name;
    /** Is the disease cured or not */
    private boolean cured;
    /** Cube left for the disease */
    private int cubesNumber;

    /**
     * Create a disease
     * @param name name of the disease
     * @param cubesNumber number of cubes
     */
    public Disease(String name, int cubesNumber) {
        this.name = name;
        this.cubesNumber = cubesNumber;
        this.cured = false;
    }

    /**
     * Create a disease
     * @param cubesNumber number of cubes
     */
    public Disease(int cubesNumber) {
        this("", cubesNumber);
    }

    /**
     * Returns true if there are cubes left for this disease, false otherwise 
     * @return true if there are cubes left for this disease, false otherwise
     */
    public boolean hasCubeLeft() {
        return this.cubesNumber > 0;
    }

    /**
     * Use a cube (decrease the number of usable cubes)
     */
    public void useCube() {
        this.cubesNumber--;
    }

    /**
     * Recover a cube (increase the number of usable cubes)
     */
    public void recoverCube() {
        this.cubesNumber++;
    }

    /**
     * Returns the name of this disease
     * @return the name of this disease
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns true if the disease is cured, false otherwise
     * @return true if the disease is cured, false otherwise
     */
    public boolean isCured() {
        return this.cured;
    }

    /**
     * Set the boolean cured on True
     */
    public void hasFoundCure() {
        this.cured = true;
    }

    /**
     * Return the string representation of this board
     * @return the string representation of this board
     */
    public String toString() {
        String res = "Disease " + this.name + ": ";
        res += "[is" + (this.cured? " not " : "") + " cured]";
        res += "[" + this.cubesNumber + " cubes]";
        return res;
    }
}