import java.util.*;

class InvalidBookingException extends Exception {
    InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    int getAvailability(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        return inventory.get(roomType);
    }

    void reduceAvailability(String roomType) throws InvalidBookingException {
        int available = getAvailability(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }
        inventory.put(roomType, available - 1);
    }
}

class BookingService {
    private RoomInventory inventory;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    void bookRoom(String guestName, String roomType) {
        try {
            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty");
            }

            int available = inventory.getAvailability(roomType);

            if (available <= 0) {
                throw new InvalidBookingException("No availability for " + roomType);
            }

            inventory.reduceAvailability(roomType);

            System.out.println("Booking Confirmed for " + guestName + " | Room Type: " + roomType);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v9.1\n");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        service.bookRoom("Akshaya", "Single Room");
        service.bookRoom("", "Double Room");
        service.bookRoom("Ravi", "Suite Room");
        service.bookRoom("Priya", "Luxury Room");
    }
}