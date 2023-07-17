package pandemic.map;

import pandemic.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

/**
 * Represent a Board loaded from JSON
 */
public class JSONBoard extends Board {

    /**
     * Load and create a board from a JSON
     * @param filepath path to the json file
     * @exception FileNotFoundException json file not found
     */
    public JSONBoard(String filepath) 
    throws FileNotFoundException {
        super();
        this.parseJson(filepath);
    }

    /**
     * Load and parse the json file
     * @param filepath path to the json file
     * @exception FileNotFoundException json file not found
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
        
        // build final list
        buildCitiesData(jsonMap, citiesMap);
    }

    /**
     * Return parsed json content of a json file
     * @param filepath path to json file
     * @return parsed json content of a json file
     * @throws FileNotFoundException file not found
     */
    protected JSONObject openJsonFile(String filepath) 
    throws FileNotFoundException {
        FileReader reader = new FileReader(filepath);
        return new JSONObject(new JSONTokener(reader));
    }

    /**
     * Build the cities data
     * @param jsonMap json map
     * @param citiesMap map of the cities 
     */
    protected void buildCitiesData(JSONObject jsonMap, Map<String, City> citiesMap) {
        JSONObject neighbors = jsonMap.getJSONObject("neighbors");
        for(String cityName : citiesMap.keySet()) {
            City c = citiesMap.get(cityName);
            JSONArray neighborsArray = neighbors.getJSONArray(cityName);
            for(Object neighborsName : neighborsArray) {
                c.addNeighbour(citiesMap.get((String)neighborsName));
            }
            this.cities.add(c);
        }
    }

    /**
     * Parse each cities data
     * @param jsonMap json map
     * @param citiesMap map of the cities
     */
    protected void parseCitiesData(JSONObject jsonMap, Map<String, City> citiesMap) {
        JSONObject cities = jsonMap.getJSONObject("cities");

        for(String cityName : cities.keySet()) {
            // get city data (name + disease type)
            int diseaseId = cities.getInt(cityName);
            Disease cityDiseaseType = this.getDiseases().get(diseaseId);
            // add city into hash map for referencing it later
            City city = new City(cityName, cityDiseaseType);
            citiesMap.put(cityName, city);
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
        for(int i = 0; i < diseaseCount; ++i) {
            this.diseases.add(new Disease("Disease"+i, 2 * diseaseCityNumber.get(i)));
        }
    }
}