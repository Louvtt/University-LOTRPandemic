package pandemic;
import java.util.*;
import pandemic.exception.*;
import pandemic.game.ChoicesSelectionCallback;
import pandemic.game.GameSettings;
import pandemic.game.RandomChoicesSelectionCallback;
import pandemic.roles.Role;
import pandemic.cards.*;

/**
 * Represent a player
 */
public class Player {

    /** The list of player card in the player's hand */
    protected List<PlayerCard> hand;

    /** The name of the player */
    protected String name;

    /** The role of the player */
    protected Role role;

    /** the actual position of the player */
    protected City position;

    /** the Max card in the hand */
    protected int nbMaxCard;

    /** The last used card */
    protected PlayerCard lastUsedCard;

    /** The Choices selection callback */
    protected ChoicesSelectionCallback choicesSelectionCallback;

    /**
     * Create a player
     * @param name the name of the player
     * @param role the role of the player
     * @param position the position of the start
     */
    public Player(String name, Role role, City position){
        this.name = name;
        this.role = role;
        this.position = position;
        this.hand = new ArrayList<PlayerCard>();

        this.lastUsedCard = null;

        this.choicesSelectionCallback = new RandomChoicesSelectionCallback();
    }

    /**
     * Retunr the number of card int the player's hand
     * @return the number of cards in the hand
     */
    public int getCardNumber() {
        return this.hand.size();
    }

    /**
     * Draw a Card and Add in the hand
     * @param card the card to draw and add
     * @throws TooManyCardException if the player have too many cards in his hand
     */
    public void drawCard(PlayerCard card) throws TooManyCardException {
        if(this.hand.size()< GameSettings.getInstance().getMaxCards()) {
            this.hand.add(card);
        } else {
            throw new TooManyCardException();
        }
    }


    /**
     * Removes a card from this player's hand
     * @param index index of the card in the hand
     * @return the card removed from the hand
     */
    public PlayerCard discardCard(int index) {
        return this.hand.remove(index);
    }

    /**
     * Return the name of the player
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the role of the player
     * @return the role of the player
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Return the city where the player is
     * @return the position of the player
     */
    public City getCity() {
        return this.position;
    }

    /**
     * Set the city where the player is
     * @param c the city where the player is
     */
    public void setCity(City c) {
        this.position = c;
    }

    /**
     * Return the hand of the player
     * @return the list of cards in the player's hand
     */
    public List<PlayerCard> getHand() {
        return this.hand;
    }

    /**
     * Returns a string representation of the current hand
     * @return a string representation of the current hand
     */
    public String getHandToString() {
        String res = "Hand: ";
        for(PlayerCard c : this.hand) {
            res += "(" + c.toString() + ") ";
        }
        return res;
    }

    /**
     * Choose an option from a list of option
     * @param message message for this choice
     * @param options options to choose from
     * @return the chosen option (random)
     */
    public int choose(String message, List<String> options) {
       return this.choicesSelectionCallback.choose(message, options);
    }
    
    /**
     * Returns the string representation of this player
     * @return the string representation of this player
     */
    public String toString() {
        String res = this.name + " plays " + this.role + " is located in " + this.position + "their hand contains the following :";
        for ( Card c : this.hand){
            res += ' ' + c.getId();
        }
        return res;
    }

}
