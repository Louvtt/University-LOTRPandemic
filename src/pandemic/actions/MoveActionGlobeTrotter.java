package pandemic.actions;

import java.util.List;
import java.util.ArrayList;

import pandemic.City;
import pandemic.Player;
import pandemic.game.Game;
import pandemic.game.Log;

/**
 * Class for the move action but for the globe trotter
 */
public class MoveActionGlobeTrotter extends MoveAction {

    /** 
     * Create a move action globetrotter
     */
    public MoveActionGlobeTrotter() {
        super();
    }

    /**
     * Execute the move action for a player
     * @param p the player that executes this action
     */
    public void execute(Player p) {
        List<City> cities = Game.GetInstance().getBoard().getCities();
        // choose among cities
        List<String> citiesName = new ArrayList<String>();
        for (City city : cities) {
            citiesName.add(city.getName());
        }
        int chosenCity = p.choose("Choose which city to travel to", citiesName);

        // move to a random city
        p.setCity(cities.get(chosenCity));
        Log.Get().log(p.getName() + " moved to " + p.getCity().getName());
    }
}
