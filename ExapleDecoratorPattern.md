One approach to handle this situation is to use the **Decorator Pattern**, which allows you to attach new behaviors or responsibilities to objects without altering their code. In your case, you can create a `RoomDecorator` that extends `Room` and contains additional features specific to `ExitRoom`. This way, you can add or modify functionalities without changing the existing `Room` class.

Here is a simplified example:

```java
// The common interface for all rooms
public interface Room {
    String getDescription();
    // Add other common methods if needed
}

// The basic implementation of a room
public class BasicRoom implements Room {
    private String description;

    public BasicRoom(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

// The decorator for ExitRoom
public class ExitRoomDecorator implements Room {
    private Room decoratedRoom;

    public ExitRoomDecorator(Room decoratedRoom) {
        this.decoratedRoom = decoratedRoom;
    }

    @Override
    public String getDescription() {
        // Add or modify behavior for ExitRoom
        return "Exit " + decoratedRoom.getDescription();
    }

    // Add or override other methods specific to ExitRoom
    public boolean isDoorOpened() {
        // Implementation for checking if the door is opened
        return true;
    }
}
```

Now, when you need to represent an `ExitRoom`, you can use `ExitRoomDecorator`:

```java
Room currentRoom = new BasicRoom("Some room");
// Decorate the basic room as an ExitRoom
currentRoom = new ExitRoomDecorator(currentRoom);

// Access ExitRoom-specific functionality
if (currentRoom instanceof ExitRoomDecorator) {
    boolean isDoorOpened = ((ExitRoomDecorator) currentRoom).isDoorOpened();
    System.out.println("Door opened: " + isDoorOpened);
}

// Access common functionality
String description = currentRoom.getDescription();
System.out.println("Description: " + description);
```

This way, you can keep the fields and behaviors of `ExitRoom` while still treating it as a `Room`.