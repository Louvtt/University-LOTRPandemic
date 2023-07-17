package pandemic.map;

import pandemic.*;

import org.json.JSONObject;
import org.joml.Vector3f;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * JSON Renderable board
 */
public class JSONRenderableBoard extends JSONBoard {

    /** Map of the position for each city by name */
    protected Map<String, Vector3f> positions;

    /**
     * Create a JSON Renderable board
     * @param filepath path to the json file 
     * @throws FileNotFoundException json file not found
     */
    public JSONRenderableBoard(String filepath) 
    throws FileNotFoundException {
        super(filepath);

        // Log.Get().debug("Parsed json renderable board : " + toString());
    }

    /**
     * Load and parse the json file
     * @param filepath path to the json file
     * @throws FileNotFoundException json file not found
     */
    protected void parseJson(String filepath) 
    throws FileNotFoundException {
        JSONObject jsonMap = openJsonFile(filepath);

        // output cities raw data
        Map<String, City> citiesMap = new HashMap<String, City>();
        
        // create diseases
        parseDiseases(jsonMap);
        
        // parse cities data
        parseCitiesData(jsonMap, citiesMap);
        parseCitiesPositions(jsonMap);
        
        // build final list
        buildCitiesData(jsonMap, citiesMap);
    }

    /**
     * Returns the cities position map
     * @return the cities position map
     */
    public Map<String, Vector3f> getPositions() {
        return this.positions;
    }

    /**
     * Parse the cities positions
     * @param jsonMap json map
     */
    protected void parseCitiesPositions(JSONObject jsonMap) {
        JSONObject positions = jsonMap.getJSONObject("positions");
        this.positions = new HashMap<>();
        for(String cityName : positions.keySet()) {
            JSONObject vec3d = positions.getJSONObject(cityName);
            Vector3f position = new Vector3f(
                vec3d.getFloat("x"),
                vec3d.getFloat("y"),
                vec3d.getFloat("z")
            );
            this.positions.put(cityName, position);
        }
    }

    /**
     * Parse the disease data
     * @param jsonMap json map
     */
    protected void parseDiseases(JSONObject jsonMap) {
        JSONObject cities = jsonMap.getJSONObject("cities");

        int diseaseCount = 0;
        List<Integer> diseaseCityNumber = new ArrayList<Integer>();
        for(String cityName : cities.keySet()) {
            int diseaseId = cities.getInt(cityName);
            while (diseaseId >= diseaseCount) {
                diseaseCount++;
                diseaseCityNumber.add(0);
            }
            Integer c = diseaseCityNumber.get(diseaseId);
            diseaseCityNumber.set(diseaseId, c + 1);
        }

        JSONArray diseaseNames = jsonMap.getJSONArray("diseases");
        for(int i = 0; i < diseaseCount; ++i) {
            String name = diseaseNames.getString(i);
            this.diseases.add(new Disease(name, 2 * diseaseCityNumber.get(i)));
        }
    }
    
}
