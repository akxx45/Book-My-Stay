import java.util.*;

class AddOnService {
    String name;
    double cost;

    AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.get(reservationId);
        if (services != null) {
            for (AddOnService s : services) {
                total += s.cost;
            }
        }
        return total;
    }

    void displayServices(String reservationId) {
        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        List<AddOnService> services = serviceMap.get(reservationId);
        if (services != null) {
            for (AddOnService s : services) {
                System.out.println(s.name + " - " + s.cost);
            }
        } else {
            System.out.println("No services selected");
        }
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v7.1\n");

        String reservationId = "RS101";

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservationId, new AddOnService("Breakfast", 200));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 500));
        manager.addService(reservationId, new AddOnService("Extra Bed", 300));

        manager.displayServices(reservationId);

        double totalCost = manager.calculateTotalCost(reservationId);
        System.out.println("\nTotal Add-On Cost: " + totalCost);
    }
}