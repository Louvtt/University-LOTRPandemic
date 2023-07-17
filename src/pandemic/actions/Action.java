package pandemic.actions;

import pandemic.*;

/**
 * Class for the actions
 */
public abstract class Action {

    /**
     * Create an action
     */
    public Action() {}
    
    /**
     * Returns the name of the action
     * @return the name of the action
     */
    public abstract String getName();

    /** Returns true if it can be executed, false otherwise 
     * @param player the player that execute this action
     * @return true if it can be executed, false otherwise
     */
    public abstract boolean canBeExecuted(Player player);

    /**
     * The execute method
     * @param player the player that execute this action
     */
    public abstract void execute(Player player);

    /**
     * Returns true if there are from the same class, false otherwise
     * @param o other object
     * @return true if there are from the same class, false otherwise 
     */
    public boolean equals(Object o) {
        return o.getClass() == this.getClass();
    }
}
