package src.Rooms;

import java.util.HashMap;

import src.Objects.Item;

public class BasicRoom implements Room {
    private String description;
    private Item roomItem;
    private HashMap<String, Room> exits;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard". an item can something like a key
     * which the player can pick up.
     * 
     * @param roomItem    An item inside of the room
     * @param description The room's description.
     */
    public BasicRoom(Item roomItem, String description) {
        this.description = description;
        this.roomItem = roomItem;
        exits = new HashMap<String, Room>();
    }

    /**
     * Create a room with a description.
     * roomItem defaults to null.
     * 
     * @param description
     */
    public BasicRoom(String description) {
        this(null, description);
    }

    @Override
    public String getDescription() {
        if (roomItem != null) {
            return description + "\n" + "This Room has a " + roomItem.getName();
        }
        return description;
    }

    @Override
    public String getExitString() {
        String returnString = "";
        for (String exit : exits.keySet()) {
            returnString += " " + exit;
        }
        return returnString;
    }

    @Override
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    @Override
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    @Override
    public void setExits(Room north, Room east, Room south, Room west) {
        if (north != null) {
            exits.put("north", north);
        }
        if (east != null) {
            exits.put("east", east);
        }
        if (south != null) {
            exits.put("south", south);
        }
        if (west != null) {
            exits.put("west", west);
        }
    }

    @Override
    public Item getRoomItem() {
        return roomItem;
    }

}
