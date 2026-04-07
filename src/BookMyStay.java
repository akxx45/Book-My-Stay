import java.util.*;

class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingProcessor extends Thread {

    private Queue<BookingRequest> bookingQueue;
    private Map<String, Integer> inventory;
    private Map<String, Queue<String>> availableRooms;

    BookingProcessor(Queue<BookingRequest> bookingQueue,
                     Map<String, Integer> inventory,
                     Map<String, Queue<String>> availableRooms) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.availableRooms = availableRooms;
    }

    public void run() {
        while (true) {
            BookingRequest request;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            processBooking(request);
        }
    }

    private void processBooking(BookingRequest request) {
        synchronized (inventory) {
            if (!inventory.containsKey(request.roomType) || inventory.get(request.roomType) == 0) {
                System.out.println(Thread.currentThread().getName() +
                        " - Booking failed for " + request.guestName + " (No rooms)");
                return;
            }

            String roomId = availableRooms.get(request.roomType).poll();
            inventory.put(request.roomType, inventory.get(request.roomType) - 1);

            System.out.println(Thread.currentThread().getName() +
                    " - Booking confirmed for " + request.guestName +
                    " | Room: " + roomId);
        }
    }
}

public class BookMyStay {

    public static void main(String[] args) throws InterruptedException {

        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);

        Map<String, Queue<String>> availableRooms = new HashMap<>();
        availableRooms.put("Deluxe", new LinkedList<>(Arrays.asList("D1", "D2")));
        availableRooms.put("Suite", new LinkedList<>(Arrays.asList("S1")));

        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        bookingQueue.add(new BookingRequest("Guest1", "Deluxe"));
        bookingQueue.add(new BookingRequest("Guest2", "Deluxe"));
        bookingQueue.add(new BookingRequest("Guest3", "Deluxe"));
        bookingQueue.add(new BookingRequest("Guest4", "Suite"));
        bookingQueue.add(new BookingRequest("Guest5", "Suite"));

        BookingProcessor t1 = new BookingProcessor(bookingQueue, inventory, availableRooms);
        BookingProcessor t2 = new BookingProcessor(bookingQueue, inventory, availableRooms);
        BookingProcessor t3 = new BookingProcessor(bookingQueue, inventory, availableRooms);

        t1.setName("Thread-1");
        t2.setName("Thread-2");
        t3.setName("Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nFinal Inventory: " + inventory);
    }
}