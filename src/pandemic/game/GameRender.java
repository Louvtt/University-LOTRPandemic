package pandemic.game;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

import pandemic.City;
import pandemic.Player;
import pandemic.actions.Action;
import pandemic.cards.PlayerCard;
import pandemic.exception.*;
import pandemic.graphics.core.*;
import pandemic.graphics.render.*;
import pandemic.graphics.scene.*;
import pandemic.graphics.scene.components.*;
import pandemic.graphics.ui.*;
import pandemic.graphics.io.*;
import pandemic.map.*;

import java.util.*;

/**
 * Graphical extension of the Game
 */
public class GameRender extends Game {
    /** Pipeline */
    protected Pipeline pipeline;
    /** Scene */
    protected Scene gameScene;

    /** Current playing player index */
    protected int currentPlayer;
    /** Current city */
    protected City targetCity;
    /** City ring */
    protected Model cityRing;
    /** State of the game */
    protected GameState gameState;
    /** city text display */
    protected Text cityTextDisplay;
    /** Player text display */
    protected Text playerDisplay;
    /** Turn text display */
    protected Text turnDisplay;
    /** Turn count */
    protected int turn;
    /** Player action count */
    protected int playerActionCount;
    /** Is the game running */
    protected boolean isRunning;
    /** Per player ui node */
    protected Map<String, SceneNode> playerUINodes;
    /** Card background */
    protected Texture cardBackground;

    /** Offset of the camera for a targetted city */
    protected static final Vector3f CAMERA_CITY_OFFSET = new Vector3f(0f, 5f, 3f);

    /**
     * Create a Game Render
     * @param board board of the game
     */
    protected GameRender(Board board) {
        super(board);

        this.pipeline = new Pipeline("LOTR Pandemic");
        this.currentPlayer = 0;

        this.gameState = GameState.IN_MENU;
    }

    /**
     * Create a Game Render Instance
     * @param board board of the game
     * @return the created game render
     */
    public static GameRender Create(Board board) {
        if (instance != null
        && instance instanceof GameRender) {
            return (GameRender)instance;
        } 
        instance = new GameRender(board);
        return (GameRender)instance;
    } 

    /**
     * Setup the scene
     */
    protected void setupScene() {
        Model plane = null;
        cityRing = null;
        try {
            plane = ModelLoader.load("assets/models/map.obj");
            cityRing = ModelLoader.load("assets/models/target.obj");
        } catch(Exception e) {
            e.printStackTrace();
        }

        cityTextDisplay = null;
        playerDisplay   = null;
        turnDisplay     = null;
        this.turn = 0;
        // Texture smileyTex = null;
        Texture title = null;
        Texture playButton = null;
        Texture quitButton = null;
        cardBackground = null;
        try {
            Font rougeScript = FontLibrary.Get().loadBitmapFont("assets/fonts/RougeScript.png", new Vector2f(50f, 50f), " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ëäû");
            Font robotoSlab = FontLibrary.Get().loadBitmapFont("assets/fonts/RobotoSlab.png", new Vector2f(30f, 30f), " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
            cityTextDisplay = new Text(rougeScript, "Default city", new Vector2f(0f, .8f));
            playerDisplay   = new Text(robotoSlab, "MainMenu", new Vector2f(-1f, .8f));
            turnDisplay     = new Text(robotoSlab, "0", new Vector2f(.9f, .8f));
            playerDisplay.setCentered(false);
            title = new ImageTexture("assets/images/title_logo-export.png");
            playButton = new ImageTexture("assets/images/play_button-export.png");
            quitButton = new ImageTexture("assets/images/quit_button-export.png");
            cardBackground = new ImageTexture("assets/images/card_background-export.png");
        } catch(Exception e) {
            Log.Get().error(e.getMessage());
            System.exit(-1);
        }

        this.gameScene = new Scene("world")
            .addNode("", new SceneNode("World"))
                .addNode("World", new SceneNode("Map").addComponent(new ModelComponent(plane)))
                // .addNode("World", new SceneNode("TargetCityRing").addComponent(new ModelComponent(cityRing)))
            .addNode("", new SceneNode("debugUI"))
                .addNode("debugUI", new SceneNode("city").addComponent(new TextComponent(cityTextDisplay)))
                .addNode("debugUI", new SceneNode("player").addComponent(new TextComponent(playerDisplay)))
                .addNode("debugUI", new SceneNode("player").addComponent(new TextComponent(turnDisplay)))
            .addNode("", new SceneNode("UI"))
                .addNode("UI", new SceneNode("Actions"))
            .addNode("", new SceneNode("MainMenu"))
                .addNode("MainMenu", new SceneNode("Play").addComponent(new ButtonComponent(new TexturedButton(playButton, new Vector2f()).registerCallback(new Button.Callback() {
                    public void onClick() {
                        GameRender.this.gameState = GameState.PLAYER_ACTIONS;
                        GameRender.this.createActionsMenuSelection();
                        GameRender.this.gameScene.getNode("MainMenu").setActive(false);
                        GameRender.this.gameScene.getNode("UI/Actions").setActive(true);
                        playerActionCount = 4;
                        GameRender.this.currentPlayer = -1; // so it starts with 0
                        GameRender.this.nextPlayerTurnSetup();
                    }
                }))))
                .addNode("MainMenu", new SceneNode("Quit").addComponent(new ButtonComponent(new TexturedButton(quitButton, new Vector2f(0f, -.2f)).registerCallback(new Button.Callback() {
                    public void onClick() {
                        GameRender.this.isRunning = false;
                    }
                }))))
                .addNode("MainMenu", new SceneNode("Logo").addComponent(new SpriteComponent(new Sprite(title, new Vector2f(0f, 0.5f)))));
        
        this.playerUINodes = new HashMap<>();
        for(Player p : this.players) {
            final SceneNode pNode = new SceneNode(p.getName() + "UI");
            this.gameScene.addNode("UI", pNode);
            this.playerUINodes.put(p.getName(), pNode);
            pNode.setActive(false);
            this.buildPlayerHand(p);
        }

        this.gameScene.getNode("UI/Actions").setActive(false);

        this.pipeline.setScene(this.gameScene);
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

        targetCity = this.players.get(currentPlayer).getCity();
        
        this.pipeline.setup();
        this.setupScene();
        this.isRunning = true;

        JSONRenderableBoard renderableBoard = (JSONRenderableBoard)board;
        Map<String, Vector3f> cityPositions = renderableBoard.getPositions();

        // main loop
        this.pipeline.getWindow().setClearColor(.2f, .3f, .9f, 1f);
        while (this.pipeline.isRunning() && this.isRunning) {
            this.pipeline.beginFrame();
            // FRAME CODE
            {
                // update
                this.pipeline.update();
                // Time timeData = this.pipeline.getTimeData();

                this.handleGameState();

                // camera handling
                if(this.gameState != GameState.WAITING) {
                    Camera cam = this.pipeline.getCamera();
                    targetCity = this.players.get(currentPlayer).getCity();
                    cityTextDisplay.setTextContent(targetCity.getName());
                    final Vector3f cityPosTmp   = cityPositions.get(targetCity.getName());
                    final Vector3f cityPos      = new Vector3f(cityPosTmp.x(), cityPosTmp.y(), cityPosTmp.z());
                    this.cityRing.setPosition(cityPos);
                    final Vector3f targetCamPos = new Vector3f(cityPos).add(CAMERA_CITY_OFFSET);
                    cam.setPosition(targetCamPos); // .lookAt(cityPos);
                }
                // render
                this.pipeline.render();

                // update game status
                this.updateGameStatus();
            }
            this.pipeline.endFrame();
        }

        this.pipeline.delete();
    }

    /**
     * Update for each game state
     */
    protected void handleGameState() 
    {
        switch(this.gameState) {
            case PLAYER_ACTIONS: {
                this.targetCity = this.players.get(this.currentPlayer).getCity();
                final SceneNode uiNode = this.gameScene.getNode("UI/Actions");

                boolean rebuildMenu = false;
                for(SceneNode node : uiNode.getChildren()) {
                    ButtonComponent buttonComp = (ButtonComponent)node.getComponents().get(0);
                    if(buttonComp.getButton().isClicked()) {
                        rebuildMenu = true;
                    }
                }
                if(rebuildMenu) {
                    this.createActionsMenuSelection();
                }

                if(this.playerActionCount <= 0) {
                    uiNode.setActive(false);
                    this.gameState = GameState.PLAYER_DRAW_CARDS;
                }
            } break;
            case PLAYER_DRAW_CARDS: {
                final Player player = this.players.get(this.currentPlayer);
                try {
                    this.drawCards(player);
                } catch (NoMoreCardException e) {}
                this.buildPlayerHand(player);
                this.gameState = GameState.PLAYER_INFECTION;
            } break;
            case PLAYER_INFECTION: {
                this.playInfector();
                this.gameState = GameState.PLAYER_ACTIONS;
                this.nextPlayerTurnSetup();
            } break;
            case WAITING: {
                if(InputManager.Get().isKeyDown(GLFW_KEY_SPACE)) {
                    isRunning = false;
                }
            } break;
            case IN_MENU:
            default:
                break;
        }
    }

    /**
     * Update game status
     */
    protected void updateGameStatus() {
        try {
            int playerDeckSize = this.playerDeck.getDiscardPile().size() + this.playerDeck.getDrawPile().size();
            int infectionDeckSize = this.infectionDeck.getDiscardPile().size() + this.infectionDeck.getDrawPile().size();
            if(hasGameEnded()) {
                this.gameState = GameState.WAITING;
                this.gameScene.getNode("debugUI").setActive(false);
                this.gameScene.getNode("UI").setActive(false);
                this.gameScene.addNode("", new SceneNode("STATUS").addComponent(
                    new TextComponent(
                        new Text(
                            FontLibrary.Get().getFont("RobotoSlab"),
                            "You won !\nthere are no diseases left",
                            new Vector2f()
                        )
                    )
                ));
                return;
            }
            if(playerDeckSize <= 0 || infectionDeckSize <= 0) {
                this.gameState = GameState.WAITING;
                this.gameScene.getNode("debugUI").setActive(false);
                this.gameScene.getNode("UI").setActive(false);
                this.gameScene.addNode("", new SceneNode("STATUS").addComponent(
                    new TextComponent(
                        new Text(
                            FontLibrary.Get().getFont("RobotoSlab"),
                            "You lost\nthere are no cards left",
                            new Vector2f()
                        )
                    )
                ));
                return;
            }
            checkCubes();
            checkCityFocus();
        } catch (NoMoreCubeException err) {
            Log.Get().error("Game Ended, You lost, there are no cube left for a disease");
            this.gameState = GameState.WAITING;
            this.gameScene.getNode("debugUI").setActive(false);
            this.gameScene.getNode("UI").setActive(false);
            this.gameScene.addNode("", new SceneNode("STATUS").addComponent(
                new TextComponent(
                    new Text(
                        FontLibrary.Get().getFont("RobotoSlab"),
                        "You lost\nthere are no cube left for a disease",
                        new Vector2f()
                    )
                )
            ));
            return;
        } catch (TooMuchInfectionSource err) {
            Log.Get().error("Game Ended, You lost, there are too many infection source");
            this.gameState = GameState.WAITING;
            this.gameScene.getNode("debugUI").setActive(false);
            this.gameScene.getNode("UI").setActive(false);
            this.gameScene.addNode("", new SceneNode("STATUS").addComponent(
                new TextComponent(
                    new Text(
                        FontLibrary.Get().getFont("RobotoSlab"),
                        "You lost\nthere are too many infection source",
                        new Vector2f()
                    )
                )
            ));
            return;
        }
    }

    protected void nextPlayerTurnSetup() {
        // Next player
        // update current player ui
        if(this.currentPlayer > 0) {
            this.playerUINodes.get(this.players.get(this.currentPlayer).getName()).setActive(false);
        }
        this.currentPlayer += 1;
        if(this.currentPlayer>= this.players.size()) {
            this.currentPlayer = 0;
            this.turn++;
            turnDisplay.setTextContent(""+this.turn);
        }
        playerDisplay.setTextContent(this.players.get(this.currentPlayer).getName());
        this.playerUINodes.get(this.players.get(this.currentPlayer).getName()).setActive(true);
        
        // update current player actions
        this.createActionsMenuSelection();
        playerActionCount = 4;
        this.gameScene.getNode("UI/Actions").setActive(true);
        
        // show player name
        playerDisplay.setTextContent(GameRender.this.players.get(GameRender.this.currentPlayer).getName());
    }

    /**
     * Update and create the action menu selection interface
     */
    protected void createActionsMenuSelection() {
        final Player player = this.players.get(this.currentPlayer);
        final SceneNode uiNode = this.gameScene.getNode("UI/Actions");
        uiNode.clearChildren();
        final Font roboto = FontLibrary.Get().getFont("RobotoSlab");
        int i = 0;
        for(Action action : player.getRole().getActions()) {
            if(!action.canBeExecuted(player)) continue;
            Button button = new TextButton(action.getName(), roboto, new Vector2f(0f, i * .1f));
            button.registerCallback(new Button.Callback(){
                public void onClick() {
                    action.execute(player);
                    GameRender.this.playerActionCount--;
                    GameRender.this.buildPlayerHand(player);
                }
            });
            uiNode.addChild(new SceneNode(action.getName()+"Button").addComponent(new ButtonComponent(button)));
            i++;
        }
    }

    /**
     * Build player hand
     * @param p player
     */
    protected void buildPlayerHand(Player p) {
        final SceneNode node = this.playerUINodes.get(p.getName());
        SceneNode hand = node.getChild("hand");
        if(hand == null) {
            hand = new SceneNode("hand");
            node.addChild(hand);
        }
        hand.clearChildren();
        final List<PlayerCard> cards = p.getHand();
        final float cardSpaceSize = 200.f / (float)Window.Get().getSize().y();
        final float cardYorigin   = cardSpaceSize * cards.size() * .5f;
        for(int i = 0; i < cards.size(); ++i) {
            final PlayerCard card = cards.get(i);
            hand.addChild(this.buildCardNode(card, cardYorigin - i * cardSpaceSize));
        }
    }

    /**
     * Build a player card scene node
     * @param card player card
     * @return the built scene node
     */
    protected SceneNode buildCardNode(PlayerCard card, float yPos) {
        final Font roboto = FontLibrary.Get().getFont("RobotoSlab");
        final String cardStr = card.getCity().getName() + "\n" + card.getDisease().getName();
        final Vector2f position = new Vector2f(-.95f, yPos);
        final Text cardText = new Text(roboto, cardStr, position);
        cardText.setCentered(false);
        cardText.setTextSize(1f);
        cardText.setColor(new Color(0,0,0,1));
        final Vector2f textSize = cardText.getComputedOutputSize();
        final Vector2i winSize  = Window.Get().getSize();
        final Vector2f spritePos = new Vector2f(500.f, textSize.y).mul(1f, .5f).div(winSize.x(), winSize.y()).add(position);
        return new SceneNode("Card"+card.getId())
            .addComponent(new SpriteComponent(new Sprite(cardBackground, spritePos)))
            .addComponent(new TextComponent(cardText));
    }
}
