package src;

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

import src.Objects.Item;
import src.Objects.Player;
import src.Rooms.BasicRoom;
import src.Rooms.ExitRoomDecorator;
import src.Rooms.Room;
import src.Utils.Command;
import src.Utils.Parser;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private LinkedHashMap<String, Room> map;
    
    // Basic Rooms
    private Room auditoriumLobby, centerWestHallway, centerEastHallway, fortGreenePlace,
            auditorium, southEliot, mural, secretRoomBelowAuditorium;

    // ExitRooms
    private Room toNorthWestEntrance, toSouthWestEntrance, toNorthEastEntrance, toSouthEastEntrance;

    /**
     * Create the game and initialize its internal map.
     */
    public Game() {
        createRooms();
        parser = new Parser();
        player = new Player();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {

        // create the rooms
        auditoriumLobby = new BasicRoom("in lobby outside the auditorium");
        fortGreenePlace = new BasicRoom("outside center west on Fort Greene Place");
        southEliot = new BasicRoom("outside center east on South Elliot");
        mural = new BasicRoom("at the mural in the lobby");
        auditorium = new BasicRoom("in the auditorium");
        toNorthWestEntrance = new BasicRoom("looking toward the north west entrance");
        toSouthWestEntrance = new BasicRoom("looking toward the south west entrance");
        toNorthEastEntrance = new BasicRoom("looking toward the north east entrance");
        toSouthEastEntrance = new BasicRoom("looking toward the south east entrance");
        Item key = new Item("key", "Can be used to unlock a door");
        secretRoomBelowAuditorium = new BasicRoom(key, "secret room below the auditorium");

        // create exit rooms
        centerWestHallway = new ExitRoomDecorator(new BasicRoom("in the center west hallway"));
        centerEastHallway = new ExitRoomDecorator(new BasicRoom("in the center east hallway"));

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
        // TODO: put this in private function
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

    // TODO: Implement this
    private void printMap() {
        for (String i : map.keySet()) {
            System.out.println(i + " : " + map.get(i));
        }

    }

    private void printLocationInfo() {
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        if (currentRoom.getRoomItem() != null) {
            System.out.println("You can take: " + currentRoom.getRoomItem().getName());
        }
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
        } else if (commandWord.equals("take")) {
            takeItem(command);
        } else if (commandWord.equals("use")) {
            useItem(command);
        } else if (commandWord.equals("print")) {
            printStats(command);
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
        System.out.println("   go quit use take help");
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
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else if (currentRoom instanceof ExitRoomDecorator && nextRoom == southEliot || nextRoom == fortGreenePlace) {
            if (((ExitRoomDecorator) currentRoom).isDoorOpened() == false) {
                System.out.println("The door outside is locked!");
            } else {
                System.out.println("\n_-_-_YOU  WIN!_-_-_");
                System.exit(21);
            }

        } else if (nextRoom instanceof ExitRoomDecorator) {
            currentRoom = (ExitRoomDecorator) nextRoom;
            printLocationInfo();

        } else {
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("Take what?");
            return;
        }

        String itemName = command.getSecondWord();

        if (currentRoom.getRoomItem() == null) {
            System.out.println("There is no item in this room");
        } else if (currentRoom.getRoomItem().getName() == itemName) {
            System.out.println("This room has no " + itemName);
        } else if (player.checkInInventory(itemName)) {
            System.out.println("You already have a " + itemName);
        } else {
            System.out.println("A " + itemName + " has been added to your inventory");
            player.addItemToInventory(currentRoom.getRoomItem());
        }
        // TODO: Make it so if you pick a word that we don't know print some message saying so 
        // Something is wrong here
    }

    private void useItem(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to use...
            System.out.println("Use what?");
            return;
        } else if (!(currentRoom instanceof ExitRoomDecorator)) {
            System.out.println("There is nothing to use an item on");
            return;
        }

        String itemName = command.getSecondWord();

        if (!(player.checkInInventory(itemName))) {
            System.out.println("You do not have a " + itemName);
        } else {
            ((ExitRoomDecorator) currentRoom).unlockDoor(player);
            System.out.println("You have unlocked the exit door!");
        }
    }
    
    private void printStats(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to print...
            System.out.println("Print what?");
            System.out.println("Type \"print help\n for commands");
            return;
        }

        String toPrint = command.getSecondWord();

        if(toPrint == "help") {
            
        } else if(toPrint == "map") {
            
        } else if(toPrint == "inventory") {
            
        } else {
            System.out.println("You can't print " + toPrint);
        }

    }

    

    /**
     * TODO: Implement this and maps
     * Can be used to statistics about the player such as what is in their
     * inventory or a map of the current game.
     * @param command
     */
    private void viewStats(Command command) {}

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
