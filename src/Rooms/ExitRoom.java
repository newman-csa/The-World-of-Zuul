package src.Rooms;

import src.Objects.Player;

public class ExitRoom extends Room {
    private boolean doorOpened = false;

    public ExitRoom(String description) {
        super(description);
    }

    @Override
    public String getDescription() {
        if (doorOpened) {
            return super.getDescription() + "\n This is an exit room, you have already unlocked the door";
        } else {
            return super.getDescription() + "\n This is an exit room, you need a key to go outside";
        }

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
