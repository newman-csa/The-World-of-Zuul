package src.Rooms;

import src.Objects.Item;

public interface Room {

    /**
     * @return The description of the room.
     */
    public String getDescription();

    public String getExitString();

    public Room getExit(String direction);

    public void setExit(String direction, Room neighbor);

    /**
     * Define the exits of this room. Every direction either leads
     * to another room or is null (no exit there).
     * 
     * @param north The north exit.
     * @param east  The east east.
     * @param south The south exit.
     * @param west  The west exit.
     */
    public void setExits(Room north, Room east, Room south, Room west);

    public Item getRoomItem();

}
