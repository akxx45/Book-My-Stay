import java.util.HashMap;

class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

public class UseCase3InventorySetup {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v3.1\n");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        System.out.println("\nChecking availability for Single Room: "
                + inventory.getAvailability("Single Room"));

        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter updating availability:\n");
        inventory.displayInventory();
    }
}