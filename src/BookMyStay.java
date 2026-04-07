import java.util.*;

class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType);
    }
}

class BookingHistory {
    private List<Reservation> history;

    BookingHistory() {
        history = new ArrayList<>();
    }

    void addReservation(Reservation r) {
        history.add(r);
    }

    List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    void displayAllBookings(List<Reservation> reservations) {
        System.out.println("Booking History:\n");
        for (Reservation r : reservations) {
            r.display();
        }
    }

    void generateSummary(List<Reservation> reservations) {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            summary.put(r.roomType, summary.getOrDefault(r.roomType, 0) + 1);
        }

        System.out.println("\nBooking Summary:");
        for (String type : summary.keySet()) {
            System.out.println(type + " : " + summary.get(type));
        }
    }
}

public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v8.1\n");

        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("RS101", "Akshaya", "Single Room"));
        history.addReservation(new Reservation("RS102", "Ravi", "Double Room"));
        history.addReservation(new Reservation("RS103", "Priya", "Suite Room"));
        history.addReservation(new Reservation("RS104", "Kiran", "Single Room"));

        BookingReportService reportService = new BookingReportService();

        reportService.displayAllBookings(history.getAllReservations());
        reportService.generateSummary(history.getAllReservations());
    }
}