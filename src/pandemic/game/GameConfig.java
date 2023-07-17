package pandemic.game;

import java.util.*;

import pandemic.*;
import pandemic.map.Board;
import pandemic.cards.*;
import pandemic.exception.MissingBoardException;
import pandemic.exception.NotAValidNumberOfPlayers;
import pandemic.exception.NotEnoughRolesException;
import pandemic.roles.*;

/**
 * Configuration builder for a Game
 */
public class GameConfig {
    /** Maximum players allowed to create a game */
    private static final int MAX_PLAYERS = 4;
    /** Minimum players allowed to create a game */
    private static final int MIN_PLAYERS = 2;

    /** List of player names */
    private List<String> playerNames;
    /** List of human player name */
    private List<String> humanPlayerNames;
    /** List of player roles */
    private List<Role> roles;
    /** Board of the game */
    private Board board;

    /** Player deck */
    private Deck<Card> playerDeck;
    /** Infection Deck */
    private Deck<InfectionCard> infectionDeck;

    /** Random utils for getting random attributes */
    private static final Random rand = new Random();

    /**
     * Create a Game Config 
     */
    public GameConfig() {
        this.playerNames = new ArrayList<String>();
        this.humanPlayerNames = new ArrayList<String>();
        this.roles = new ArrayList<Role>();
        this.board = null;
    }

    /**
     * Add a role to the list of available roles
     * @param role a role to add (must be unique)
     * @return the current game config
     */
    public GameConfig addRole(Role role) {
        // two players cannot have the same role
        if(!this.roles.contains(role)) {
            this.roles.add(role);
        }
        return this;
    }

    /**
     * Set the board of the game
     * @param board the board to play
     * @return the current game config
     */
    public GameConfig setBoard(Board board) {
        this.board = board;
        return this;
    }

    /**
     * Add a player to the list
     * @param name the player to add
     * @return the current game config
     */
    public GameConfig addPlayer(String name) {
        this.playerNames.add(name);
        return this;
    }

    /**
     * Add a player to the list
     * @param name the player to add
     * @return the current game config
     */
    public GameConfig addHumanPlayer(String name) {
        this.humanPlayerNames.add(name);
        return this;
    }

    /**
     * Returns a random role between the available roles
     * @return a random role between the available roles
     */
    private Role getRandomRole() {
        return this.roles.remove(rand.nextInt(this.roles.size()));
    }

    /**
     * Returns a random city in the board
     * @return a random city in the board
     */
    private City getRandomCity() {
        if(board == null) return null; 
        List<City> cities = this.board.getCities();
        City randCity = cities.get(rand.nextInt(cities.size()));
        return randCity;
    }

     /**
     * Generate a new player deck
     */
    protected void generatePlayerDeck() {
        List<Card> playerDeckCards = new LinkedList<Card>();
        // generate player cards
        int i = 0;
        for(City c : this.board.getCities()) {
            playerDeckCards.add(new PlayerCard(i, c, c.getInfectionType()));
            i++;
        }

        for(Disease d : this.board.getDiseases()) {
            playerDeckCards.add(new EpidemicCard(i, d));
            i++;
        }

        this.playerDeck = new Deck<Card>(playerDeckCards);
    }

    /**
     * Generate a new infection deck
     */
    protected void generateInfectionDeck() {
        List<InfectionCard> infectionDeckCards = new LinkedList<InfectionCard>();
        // generate player cards
        int i = 0;
        for(City c : this.board.getCities()) {
            infectionDeckCards.add(new InfectionCard(i, c, c.getInfectionType()));
            i++;
        }

        this.infectionDeck = new Deck<InfectionCard>(infectionDeckCards);
    }

    /** Check if the configuration is valid
     * @throws MissingBoardException the board is null
     * @throws NotAValidNumberOfPlayers the number of player is &lt; 2 or &gt; 4
     * @throws NotEnoughRolesException the number of roles is &lt; the number of players
     */
    protected void checkConfiguration()
    throws MissingBoardException,NotAValidNumberOfPlayers,NotEnoughRolesException {
        // check validity
        if(this.board == null) 
            throw new MissingBoardException();
        
        int playerCount = this.playerNames.size() + this.humanPlayerNames.size();
        // player count validity
        if(playerCount < GameConfig.MIN_PLAYERS) 
            throw new NotAValidNumberOfPlayers("Not enough players to play (min: " + GameConfig.MIN_PLAYERS + ")");
        if(playerCount > GameConfig.MAX_PLAYERS) 
            throw new NotAValidNumberOfPlayers("Too many players to play (max: " + GameConfig.MAX_PLAYERS + ")");

        // role count validity
        if(playerCount > this.roles.size())
            throw new NotEnoughRolesException();
    }

    /**
     * Create the game based on the configuration
     * @return the game based on the configuration
     * @throws MissingBoardException the board is null
     * @throws NotAValidNumberOfPlayers the number of player is &lt; 2 or &gt; 4
     * @throws NotEnoughRolesException the number of roles is &lt; the number of players
     */
    public Game build()
    throws MissingBoardException,NotAValidNumberOfPlayers,NotEnoughRolesException {
        this.checkConfiguration();
    
        // Creation
        Game game = Game.Create(board);
        for(String name : this.playerNames) {
            game.addPlayer(new Player(name, this.getRandomRole(), this.getRandomCity()));
        }
        for(String name : this.humanPlayerNames) {
            game.addPlayer(new Human(name, this.getRandomRole(), this.getRandomCity()));
        }
        
        this.generatePlayerDeck();
        playerDeck.shuffle();
        game.setPlayerDeck(playerDeck);

        this.generateInfectionDeck();
        infectionDeck.shuffle();
        game.setInfectionDeck(infectionDeck);

        return game;
    }
    
    /**
     * Create the game render based on the configuration
     * @return the game render based on the configuration
     * @throws MissingBoardException the board is null
     * @throws NotAValidNumberOfPlayers the number of player is &lt; 2 or &gt; 4
     * @throws NotEnoughRolesException the number of roles is &lt; the number of players
     */
    public GameRender buildGraphical()
    throws MissingBoardException,NotAValidNumberOfPlayers,NotEnoughRolesException {
        this.checkConfiguration();

        // Creation
        GameRender game = GameRender.Create(board);
        for(String name : this.playerNames) {
            game.addPlayer(new Player(name, this.getRandomRole(), this.getRandomCity()));
        }
        for(String name : this.humanPlayerNames) {
            game.addPlayer(new Human(name, this.getRandomRole(), this.getRandomCity()));
        }
        
        this.generatePlayerDeck();
        playerDeck.shuffle();
        game.setPlayerDeck(playerDeck);

        this.generateInfectionDeck();
        infectionDeck.shuffle();
        game.setInfectionDeck(infectionDeck);

        return game;
    }

    /**
     * Returns the list of roles in this game config
     * @return the list of roles in this game config
     */
    public List<Role> getRoles() {
        return roles;
    }

}
