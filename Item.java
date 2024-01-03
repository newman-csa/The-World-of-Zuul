// TODO: Make this into an abstract class for differnt types of items.
public class Item {
    private String name;
    private String description;

    public Item (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Item (String name) {
        this(name, null);
    }

    public String getDescription() {
        return description;
    }
    
    public String getName() {
        return name;
    }

    // TODO: Make this functionality
    public static Item craftItem(Item item1, Item item2) {

        return item1;
    }

}
