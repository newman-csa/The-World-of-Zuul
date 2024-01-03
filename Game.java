
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

import java.util.LinkedHashMap;
import java.util.function.ToDoubleFunction;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private LinkedHashMap<String, Room> map;

    /**
     * Create the game and initialize its internal map.
     */
    public Game() {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room auditoriumLobby, centerWestHallway, centerEastHallway, fortGreenePlace,
                toNorthWestEntrance, toSouthWestEntrance, auditorium, toNorthEastEntrance,
                toSouthEastEntrance, southEliot, mural, secretRoomBelowAuditorium;

        // create the rooms
        auditoriumLobby = new Room("in lobby outside the auditorium");
        centerWestHallway = new Room("in the center west hallway");
        centerEastHallway = new Room("in the center east hallway");
        fortGreenePlace = new Room("outside center west on Fort Greene Place");
        toNorthWestEntrance = new Room("looking toward the north west entrance");
        toSouthWestEntrance = new Room("looking toward the south west entrance");
        toNorthEastEntrance = new Room("looking toward the north east entrance");
        toSouthEastEntrance = new Room("looking toward the south east entrance");
        southEliot = new Room("outside center east on South Elliot");
        mural = new Room("at the mural in the lobby");
        auditorium = new Room("in the auditorium");
        secretRoomBelowAuditorium = new Room("secret room below the auditorium");

        // initialize room exits (north, east, south, west)
        auditoriumLobby.setExits(mural, centerEastHallway, auditorium, centerWestHallway);
        centerWestHallway.setExits(toNorthWestEntrance, auditoriumLobby, toSouthWestEntrance, fortGreenePlace);
        centerEastHallway.setExits(toNorthEastEntrance, southEliot, toSouthEastEntrance, auditoriumLobby);

        fortGreenePlace.setExits(null, centerWestHallway, null, null);
        toNorthWestEntrance.setExits(null, null, centerWestHallway, null);
        toSouthWestEntrance.setExits(centerWestHallway, null, null, null);
        auditorium.setExits(auditoriumLobby, null, null, null);
        mural.setExits(null, null, auditoriumLobby, null);
        southEliot.setExits(null, centerEastHallway, null, null);
        toNorthEastEntrance.setExits(null, null, centerEastHallway, null);
        toSouthEastEntrance.setExits(centerEastHallway, null, null, null);

        auditorium.setExit("downstairs", secretRoomBelowAuditorium);
        secretRoomBelowAuditorium.setExit("upstairs", auditorium);

        currentRoom = auditoriumLobby; // start game outside

        // The Letters are the the columns and the numbers the rows, just like algebraic
        // notation
        map = new LinkedHashMap<String, Room>();
        map.put("a1", null);
        map.put("a2", fortGreenePlace);
        map.put("a3", null);
        map.put("b1", toSouthWestEntrance);
        map.put("b2", centerWestHallway);
        map.put("b3", toNorthWestEntrance);
        map.put("c1", auditorium);
        map.put("c2", auditoriumLobby);
        map.put("c3", mural);
        map.put("d1", toSouthEastEntrance);
        map.put("d2", centerEastHallway);
        map.put("d3", toNorthEastEntrance);
        map.put("e1", null);
        map.put("e2", southEliot);
        map.put("e2", null);
    }

    private void printMap() {
        for (String i : map.keySet()) {
            System.out.println(i + " : " + map.get(i));
        }

    }

    private void printLocationInfo() {
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        System.out.print(currentRoom.getExitString());
        System.out.println();
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    // TODO: Implement this
    private void collectItem() {}

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true; // signal that we want to quit
        }
    }
}
