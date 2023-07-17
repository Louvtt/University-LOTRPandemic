package pandemic;

import pandemic.game.InteractiveChoicesSelectionCallback;
import pandemic.roles.Role;

/**
 * Represent an Human Player
 */
public class Human extends Player {

    /**
     * Create a player
     * @param name name of the player
     * @param role role of the player
     * @param position position of the player
     */
    public Human(String name, Role role, City position) {
        super(name, role, position);

        this.choicesSelectionCallback = new InteractiveChoicesSelectionCallback();
    }

}