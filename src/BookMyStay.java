import java.io.*;
import java.util.*;

class Booking implements Serializable {
    String bookingId;
    String roomType;
    String roomId;
    boolean isActive;

    Booking(String bookingId, String roomType, String roomId, boolean isActive) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = isActive;
    }
}

class SystemState implements Serializable {
    Map<String, Integer> inventory;
    Map<String, Queue<String>> availableRooms;
    Map<String, Booking> bookings;

    SystemState(Map<String, Integer> inventory,
                Map<String, Queue<String>> availableRooms,
                Map<String, Booking> bookings) {
        this.inventory = inventory;
        this.availableRooms = availableRooms;
        this.bookings = bookings;
    }
}

public class BookMyStay{

    private static final String FILE_NAME = "system_state.ser";

    private static Map<String, Integer> inventory = new HashMap<>();
    private static Map<String, Queue<String>> availableRooms = new HashMap<>();
    private static Map<String, Booking> bookings = new HashMap<>();

    public static void main(String[] args) {

        loadState();

        if (inventory.isEmpty()) {
            initializeSystem();
        }

        createBooking("B201", "Deluxe");
        createBooking("B202", "Suite");

        System.out.println("\nCurrent System State:");
        printState();

        saveState();

        System.out.println("\nSimulating restart...\n");

        inventory = new HashMap<>();
        availableRooms = new HashMap<>();
        bookings = new HashMap<>();

        loadState();

        System.out.println("Recovered System State:");
        printState();
    }

    private static void initializeSystem() {
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        availableRooms.put("Deluxe", new LinkedList<>(Arrays.asList("D1", "D2")));
        availableRooms.put("Suite", new LinkedList<>(Arrays.asList("S1")));
    }

    private static void createBooking(String bookingId, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) == 0) {
            System.out.println("Booking failed for " + bookingId);
            return;
        }

        String roomId = availableRooms.get(roomType).poll();
        inventory.put(roomType, inventory.get(roomType) - 1);

        bookings.put(bookingId, new Booking(bookingId, roomType, roomId, true));

        System.out.println("Booking confirmed: " + bookingId + " | Room: " + roomId);
    }

    private static void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            SystemState state = new SystemState(inventory, availableRooms, bookings);
            oos.writeObject(state);
            System.out.println("\nState saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving state.");
        }
    }

    private static void loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            inventory = state.inventory;
            availableRooms = state.availableRooms;
            bookings = state.bookings;
            System.out.println("State loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading state. Starting with empty system.");
            inventory = new HashMap<>();
            availableRooms = new HashMap<>();
            bookings = new HashMap<>();
        }
    }

    private static void printState() {
        System.out.println("Inventory: " + inventory);

        System.out.println("Available Rooms:");
        for (String type : availableRooms.keySet()) {
            System.out.println(type + " -> " + availableRooms.get(type));
        }

        System.out.println("Bookings:");
        for (Booking b : bookings.values()) {
            System.out.println(b.bookingId + " | " + b.roomType + " | " + b.roomId + " | Active: " + b.isActive);
        }
    }
}