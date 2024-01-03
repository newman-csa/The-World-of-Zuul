import java.util.ArrayList;

public class Player {
    private ArrayList<Item> inventory;

    public Player() {
        inventory = new ArrayList<Item>();
    }

    /**
     * 
     * Adds Items to the player's inventory. If the player already has that item
     * do not store it and return false.
     * 
     * @param item
     * @return true if operation is successful; false if operation is not successful
     */
    public boolean addItemToInventory(Item itemToAdd) {
        for (Item item : inventory) {
            if (item.getName().equals(itemToAdd.getName())) return false;
        }

        inventory.add(itemToAdd);
        return true;

    }

    public boolean checkInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) return true;
        }

        return false;
    }

    /**
     * Precondition: Item has been checked for if it exists in inventory.
     * 
     * @param itemName
     */
    public void removeItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) inventory.remove(item);
            break;
        }
    }
}
