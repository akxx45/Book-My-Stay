import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue;

    BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    void addRequest(Reservation reservation) {
        queue.add(reservation);
    }

    Reservation getNextRequest() {
        return queue.poll();
    }

    boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void reduceAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    void displayInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private Set<String> allRoomIds;
    private int counter = 1;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
        allRoomIds = new HashSet<>();
    }

    void processBooking(Reservation reservation) {
        String type = reservation.roomType;

        if (inventory.getAvailability(type) > 0) {
            String roomId = type.substring(0, 2).toUpperCase() + counter++;

            if (!allRoomIds.contains(roomId)) {
                allRoomIds.add(roomId);

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);

                inventory.reduceAvailability(type);

                System.out.println("Booking Confirmed for " + reservation.guestName +
                        " | Room Type: " + type +
                        " | Room ID: " + roomId);
            }
        } else {
            System.out.println("Booking Failed for " + reservation.guestName +
                    " | No rooms available for " + type);
        }
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v6.1\n");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        queue.addRequest(new Reservation("Akshaya", "Single Room"));
        queue.addRequest(new Reservation("Ravi", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room"));
        queue.addRequest(new Reservation("Kiran", "Suite Room"));

        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            service.processBooking(r);
        }

        inventory.displayInventory();
    }
}