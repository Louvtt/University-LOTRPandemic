package pandemic.roles;

import java.util.*;

import pandemic.actions.Action;
import pandemic.actions.DoNothingAction;

/**
 * Reprensent the Role
 */
public abstract class Role {

    /** the list of actions */
    protected List<Action> actions;

    /** the name of the role */
    protected String name;

    /**
     * Create a role
     * @param name name of the role
     * @param actions actions available for this role
     */
    public Role(String name, List<Action> actions){
        this.name    = name;
        this.actions = actions;
    }

    /**
     * Create a role with some default actions
     * @param name name of the role
     */
    public Role(String name){
        this(name, new ArrayList<Action>());
        this.actions.add(new DoNothingAction());
    }

    /**
     * Return the name of the role
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the list of action of the role
     * @return the list of action
     */
    public List<Action> getActions() {
        return this.actions;
    }

    /**
     * Returns the string representation of this role
     * @return the string representation of this role
     */
    public String toString() {
        String res= "This player has the following role : " + this.name + " they have the followign actions :";
        for ( Action c : this.actions){
            res += ' ' + c.getName(); // method not,implemented yet
        }
        return res;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Role))
            return false;        

        Role role = (Role)obj;
        return role.getName().equals(this.name);
    }
}

