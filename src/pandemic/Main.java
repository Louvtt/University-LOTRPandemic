package pandemic;

import pandemic.map.*;
import pandemic.roles.*;

import java.io.FileNotFoundException;

import pandemic.exception.*;
import pandemic.game.*;

/**
 * Main class
 * @hidden
 */
public class Main {

    private static String file = "carte2.json";
    private static boolean enableGraphics = false;

    public static void main(String[] args) {
        parseArguments(args);
        
        try {
            Log.Get().log("Creating game with board : " + file);
            Game game;
            GameConfig gc = new GameConfig()
                .addPlayer("Robert")
                .addPlayer("Michel")
                // .addPlayer("Patrick")
                // .addPlayer("Denis")
                .addRole(new Globetrotter())
                .addRole(new Doctor())
                .addRole(new Scientist())
                .addRole(new Expert());

            if(enableGraphics) {
                Log.Get().debug("Enabling graphic mode");
                gc.setBoard(new JSONRenderableBoard(file));
                game = (Game)gc.buildGraphical();
            } else {
                gc.setBoard(new JSONBoard(file));
                game = gc.build();
            }
            game.run();   
        } catch(FileNotFoundException e) {
            Log.Get().error("File not found : " + Main.file);
        } catch (NotAValidNumberOfPlayers|NotEnoughRolesException|MissingBoardException e) {
            Log.Get().error("An error occured while creating the game : " + e);
        }
    }


    private static void parseArguments(String[] args) {
        for(String arg : args) {
            switch(arg) {
                case "--graphical-mode":
                    Main.enableGraphics = true;
                    break;
                default: {
                    if(arg.charAt(0) != '-') break;
                    String paramName = arg.substring(0, arg.indexOf('='));
                    String paramArg  = arg.substring(arg.indexOf('=')+1);
                    switch(paramName) {
                        case "--file":
                        Main.file = paramArg;
                            break;
                        default: break;
                    }
                    break;
                }
            }
        }
        Main.file = "assets/maps/" + Main.file;
    }
}