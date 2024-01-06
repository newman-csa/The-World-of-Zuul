package src.Objects;

import java.util.ArrayList;

public class Player {
    private ArrayList<Item> inventory;

    public Player() {
        inventory = new ArrayList<Item>();
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public String getInventoryString() {
        String outStr = "";
        for (Item item : inventory)
            outStr += item.getName() + " " + item.getDescription() + "\n";

        return outStr;
    }

    /**
     * Adds Items to the player's inventory. If the player already has that item
     * do not store it and return false.
     * 
     * @param item
     * @return true if operation is successful; false if operation is not successful
     */
    public boolean addItemToInventory(Item itemToAdd) {
        for (Item item : inventory)
            if (item.getName().equals(itemToAdd.getName()))
                return false;

        inventory.add(itemToAdd);
        return true;

    }

    /**
     * Checks Inventory for if an item exist within it
     * 
     * @param itemName
     * @return
     */
    public boolean checkInInventory(String itemName) {
        for (Item item : inventory)
            if (item.getName().equals(itemName))
                return true;

        return false;
    }

    /**
     * Precondition: Item has been checked for if it exists in inventory.
     * 
     * @param itemName
     */
    public void removeItemInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName))
                inventory.remove(item);

            break;
        }
    }
}
