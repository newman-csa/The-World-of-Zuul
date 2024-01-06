package src.Rooms;

import src.Objects.Item;
import src.Objects.Player;

public class ExitRoomDecorator implements Room {
    private Room room;
    private boolean doorOpened;

    public ExitRoomDecorator(Room room) {
        this.room = room;
        doorOpened = false;
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ". This is an exit room!" + ((doorOpened) ? "\nThe door is unlocked!"
                : "\nThe door is currently locked");
    }

    @Override
    public String getExitString() {
        return room.getExitString();
    }

    @Override
    public Room getExit(String direction) {
        return room.getExit(direction);
    }

    @Override
    public void setExit(String direction, Room neighbor) {
        room.setExit(direction, neighbor);
    }

    @Override
    public void setExits(Room north, Room east, Room south, Room west) {
        room.setExits(north, east, south, west);
    }

    @Override
    public Item getRoomItem() {
        return room.getRoomItem();
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public void unlockDoor(Player player) {
        if (player.checkInInventory("key")) {
            player.removeItemInInventory("key");
            System.out.println("Door has been unlocked");
            System.out.println("Key has been removed from your inventory");
            doorOpened = true;
        } else {
            System.out.println("You do not have a key to unlock this door.");
        }
    }

}
