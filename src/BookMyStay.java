import java.util.LinkedList;
import java.util.Queue;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + ", Room Type: " + roomType);
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue;

    BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request added for " + reservation.guestName);
    }

    void displayQueue() {
        System.out.println("\nCurrent Booking Requests:");
        for (Reservation r : queue) {
            r.display();
        }
    }
}

public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v5.1\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Akshaya", "Single Room"));
        bookingQueue.addRequest(new Reservation("Ravi", "Double Room"));
        bookingQueue.addRequest(new Reservation("Priya", "Suite Room"));

        bookingQueue.displayQueue();
    }
}