import java.util.*;

// Booking class to store booking details
class Booking {
    String bookingId;
    String roomType;
    String roomId;
    boolean isActive;

    Booking(String bookingId, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }
}

// Main system class
public class UseCase10BookingCancellation {

    // Inventory: roomType -> available count
    private static Map<String, Integer> inventory = new HashMap<>();

    // Available room IDs: roomType -> queue of room IDs
    private static Map<String, Queue<String>> availableRooms = new HashMap<>();

    // Booking records: bookingId -> Booking object
    private static Map<String, Booking> bookings = new HashMap<>();

    // Rollback stack (LIFO)
    private static Stack<String> rollbackStack = new Stack<>();

    public static void main(String[] args) {

        // Initialize inventory
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        // Initialize room IDs
        availableRooms.put("Deluxe", new LinkedList<>(Arrays.asList("D1", "D2")));
        availableRooms.put("Suite", new LinkedList<>(Arrays.asList("S1")));

        // Simulate booking
        createBooking("B101", "Deluxe");
        createBooking("B102", "Suite");

        System.out.println("\n--- Before Cancellation ---");
        printStatus();

        // Cancel booking
        cancelBooking("B101");

        System.out.println("\n--- After Cancellation ---");
        printStatus();

        // Try invalid cancellation
        cancelBooking("B999"); // Non-existent
        cancelBooking("B101"); // Already cancelled
    }

    // Booking creation (for simulation)
    public static void createBooking(String bookingId, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) == 0) {
            System.out.println("No rooms available for type: " + roomType);
            return;
        }

        String roomId = availableRooms.get(roomType).poll();
        inventory.put(roomType, inventory.get(roomType) - 1);

        Booking booking = new Booking(bookingId, roomType, roomId);
        bookings.put(bookingId, booking);

        System.out.println("Booking confirmed: " + bookingId + " | Room: " + roomId);
    }

    // Cancellation logic with rollback
    public static void cancelBooking(String bookingId) {
        System.out.println("\nAttempting cancellation for: " + bookingId);

        // Validation
        if (!bookings.containsKey(bookingId)) {
            System.out.println("Cancellation failed: Booking does not exist.");
            return;
        }

        Booking booking = bookings.get(bookingId);

        if (!booking.isActive) {
            System.out.println("Cancellation failed: Booking already cancelled.");
            return;
        }

        // Step 1: Push to rollback stack
        rollbackStack.push(booking.roomId);

        // Step 2: Restore inventory
        inventory.put(booking.roomType, inventory.get(booking.roomType) + 1);

        // Step 3: Release room back to pool
        availableRooms.get(booking.roomType).offer(booking.roomId);

        // Step 4: Update booking status
        booking.isActive = false;

        // Step 5: Log history
        System.out.println("Cancellation successful for booking: " + bookingId);
        System.out.println("Room " + booking.roomId + " released back to inventory.");
    }

    // Utility method to print system status
    public static void printStatus() {
        System.out.println("\nInventory: " + inventory);

        System.out.println("Available Rooms:");
        for (String type : availableRooms.keySet()) {
            System.out.println(type + " -> " + availableRooms.get(type));
        }

        System.out.println("Rollback Stack: " + rollbackStack);

        System.out.println("Bookings:");
        for (Booking b : bookings.values()) {
            System.out.println(b.bookingId + " | " + b.roomType + " | " + b.roomId + " | Active: " + b.isActive);
        }
    }
}