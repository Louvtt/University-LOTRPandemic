package pandemic.game;

import pandemic.*;
import pandemic.map.*;
import pandemic.cards.*;
import pandemic.exception.NoMoreCardException;
import pandemic.exception.NoMoreCubeException;
import pandemic.exception.TooManyCardException;
import pandemic.exception.TooMuchInfectionSource;

import java.util.*;
import pandemic.actions.*;

/**
 * Main Game class
 */
public class Game {

    /** the list of Players */
    protected List<Player> players;

    /** the game board */
    protected Board board;

    /** Player and Epidemic Deck */
    protected Deck<Card> playerDeck;

    /** Infection Deck */
    protected Deck<InfectionCard> infectionDeck;

    /** Unique instance of the Game */
    protected static Game instance;

    /** Give the state of infection or epidemic */
    protected boolean epidemic = false;

    /** global infection rate */
    protected Integer globalInfectionRate = 2;

    /**
     * Create or return the current instance
     * @param board board for the game
     * @return the created or current instance 
     */
    public static Game Create(Board board) {
        if (instance != null) return instance;
        instance = new Game(board);
        return instance;
    }

    /**
     * Returns the instance of the current Game
     * @return the instance of the current Game
     */
    public static Game GetInstance() {
        return instance;
    }

    /**
     * Initialize Game
     * @param board the board for the game
     */
    protected Game(Board board) {
        this.players = new ArrayList<Player>();
        this.board = board;
        
        // Decks
        this.playerDeck = new Deck<Card>();
        this.infectionDeck = new Deck<InfectionCard>();
       
    }

    /**
     * Add a player to the list
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if(this.players.size() < 4) {
            this.players.add(player);
        }
    }

    /**
     * Return the board of the game
     * @return the board
     */
    public Board getBoard() { 
        return this.board;
    }

    /**
     * Return a infection card in the Deck
     * @return a infection card
     */
    public Deck<InfectionCard> getInfectionDeck() {
        return infectionDeck;
    }

    /**
     * Return a player card in the Deck
     * @return a player card
     */
    public Deck<Card> getPlayerDeck() {
        return playerDeck;
    }

    /**
     * Return the list of Player
     * @return the list of Player
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Verify if the game has ended
     * @return if the game has ended
     */
    protected boolean hasGameEnded() {
        boolean allDiseaseCured = true;
        for(Disease d : this.board.getDiseases()) {
            allDiseaseCured = allDiseaseCured && d.isCured();
        }
        boolean allCitiesInfected = true;
        for(City c : this.board.getCities()) {
            allCitiesInfected = allCitiesInfected && (c.getInfectionLevel() > 0);
        }
        return allDiseaseCured && !allCitiesInfected;
    }

    /**
     * Run the Game
     */
    public void run() {
        if(this.players.size() < 2) {
            Log.Get().error("Not enough players (2-4 players)");
            return;
        }

        try {
            this.distributeToPlayers();
        } catch (NoMoreCardException e) {
            Log.Get().error(e.toString());
            return;
        }

        int turn =0;
        // main loop
        while (!hasGameEnded()) {
            Log.Get().log("------------- Turn "+ turn +" -------------");
            for(Player player : this.players) {
                try {
                    processTurn(player);
                    checkCubes();
                    checkCityFocus();
                } catch (NoMoreCardException err) {
                    Log.Get().error("Game Ended, You lost, there are no more cards :(");
                    return;
                } catch (NoMoreCubeException err) {
                    Log.Get().error("Game Ended, You lost, there are no cube left for a disease");
                    return;
                } catch (TooMuchInfectionSource err) {
                    Log.Get().error("Game Ended, You lost, there are too many infection source");
                    return;
                }
                Log.Get().log("");
            }
            turn +=1;
        }
        Log.Get().log("\nYOU WON");
    }
    
    /**
     * Check if the City infection level != 3 for all City 
     * @throws TooMuchInfectionSource there is more than 8 infection sources
     */
    protected void checkCityFocus() throws TooMuchInfectionSource{
        int foyer= 0;
        for(City c : this.board.getCities()) {
            if(c.getInfectionLevel()==3) {
                foyer +=1;
                if(foyer>=8) {
                    throw new TooMuchInfectionSource();
                }
            }
        }
    }

    /**
     * Distributes the cards to the player
     * @throws NoMoreCardException no enough cards in player deck to distribute to the players
     */
    protected void distributeToPlayers() throws NoMoreCardException {
        int toDistributePerPlayer = 6 - this.players.size();
        if(playerDeck.getDrawPile().size() < toDistributePerPlayer * this.players.size()) {
            throw new NoMoreCardException("No enought cards in deck (missing cities or deck not created).");
        }


        for(Player p : this.players) {
            int count = 0;
            while(count < toDistributePerPlayer) {
                Card c;
                try {
                    c = playerDeck.draw();
                    if (c instanceof EpidemicCard) {
                        playerDeck.discard(c);
                    } else {
                        p.drawCard((PlayerCard)c);
                        count++;
                    }
                } catch (NoMoreCardException | TooManyCardException e) {}
            }
        }
        playerDeck.reset(); // replaces epidemic cards inside the fullDeck
    }

    /**
     * Process the turn of a player
     * @param player the player who play the turn
     * @throws NoMoreCardException the exception if there are no cards in the deck
     */
    protected void processTurn(Player player) throws NoMoreCardException {
        Log.Get().log("[" + player.getName() + " turn (Starts on " + player.getCity().getName() + ") - Role: "+ player.getRole().getName() +"]");
        // Choix de 4 actions
        handleAction(player);
        drawCards(player);

        // Jouer l'infecteur
        playInfector();
        
    }
    /**
     * Check if there are cubes left for all the diseases
     * @throws NoMoreCubeException no more cubes available
     */
    protected void checkCubes() throws NoMoreCubeException {
        for(Disease d : this.board.getDiseases()) {
            if(!d.hasCubeLeft()) {
                throw new NoMoreCubeException();
            }
        }
    }

    /**
     * Draw cards for the specified player
     * @param player the current player
     * @throws NoMoreCardException there is no more card in the playerDeck
     */
    protected void drawCards(Player player) throws NoMoreCardException {
        // Tirer deux cartes
        for (int i = 0; i < GameSettings.getInstance().getCardsDrawnPerTurn(); ++i) {
            try {
                Card drawnCard = playerDeck.draw();
                Log.Get().log("Drawn " + drawnCard);
                // Carte épidémie
                if (drawnCard instanceof EpidemicCard) {
                    this.epidemic = true;
                    globalInfectionRate+=1;
                    playInfector();
                    playerDeck.discard(drawnCard);
                } else {
                    player.drawCard((PlayerCard)drawnCard);
                }
            } catch (TooManyCardException e) {
                List <String> cardList = new ArrayList<String>();
                for(PlayerCard c : player.getHand()) {
                    cardList.add(c.toString());
                }
                int index = player.choose("Choose which card you want to discard", cardList);
                PlayerCard card = player.discardCard(index);
                this.playerDeck.discard(card);
            }
        }
    }

    /**
     * Handle the action choosing of the specified player
     * @param player the current player
     */
    protected void handleAction(Player player) {
        for(int i = 0; i < GameSettings.getInstance().getMaxActions(); ++i) {
            Log.Get().log(player.getHandToString());
            List<Action> actions = player.getRole().getActions();

            List<String> options = new ArrayList<String>();
            List<Action> availableActions = new ArrayList<Action>();
            for (Action act : actions) {
                if(act.canBeExecuted(player)) {
                    options.add(act.getName());
                    availableActions.add(act);
                }
            }
            int chosenActionId = player.choose("Choose the action to perform", options);
            Action chosenAction = availableActions.get(chosenActionId);
            chosenAction.execute(player);
            Log.Get().log("Action executed : " + chosenAction.getName());
        }
    }

    /**
     * Play the infector
     */
    protected void playInfector() {
        try {
            List<City> infectedCities = new ArrayList<City>();
            if(!this.epidemic) {
                for(int i = 0; i<this.globalInfectionRate;i++) {
                    InfectionCard drawnCard = infectionDeck.draw();
                    Log.Get().log("Drawn "+drawnCard);
                    
                    drawnCard.getCity().infect(infectedCities);
                    infectionDeck.discard(drawnCard);
                }
            } else {
                InfectionCard drawnCard = infectionDeck.draw();
                Log.Get().log("Drawn "+drawnCard);
                drawnCard.getCity().infect(infectedCities);
                this.epidemic = false;
                infectionDeck.discard(drawnCard);
                infectionDeck.reset();
            }
        } catch (NoMoreCardException e) {
            Log.Get().error("No more cards in the infection deck");
        }
        
    }
    

    /**
     * Set the player deck of this game
     * @param playerDeck player deck of this game
     */
    public void setPlayerDeck(Deck<Card> playerDeck) {
        this.playerDeck = playerDeck;
    }

    /**
     * Set the infection deck of this game
     * @param infectionDeck infection deck of this game
     */
    public void setInfectionDeck(Deck<InfectionCard> infectionDeck) {
        this.infectionDeck = infectionDeck;
    }

    /**
     * Delete the instance
     */
    public static void DeleteInstance() {
        Game.instance = null;
    }
}
