import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// --- Domain Model ---
class Reservation {
  private String bookingId;
  private String customerName;
  private double amount;

  public Reservation(String bookingId, String customerName, double amount) {
    this.bookingId = bookingId;
    this.customerName = customerName;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return String.format("ID: %s | Customer: %s | Amount: $%.2f", bookingId, customerName, amount);
  }

  public double getAmount() { return amount; }
}

// --- Storage Layer (Booking History) ---
class BookingHistory {
  // List preserves insertion order for chronological tracking
  private List<Reservation> history = new ArrayList<>();

  public void addRecord(Reservation reservation) {
    history.add(reservation);
  }

  public List<Reservation> getAllRecords() {
    // Returning a copy to ensure reporting does not modify original data
    return new ArrayList<>(history);
  }
}

// --- Reporting Layer (Booking Report Service) ---
class BookingReportService {
  private BookingHistory bookingHistory;

  public BookingReportService(BookingHistory history) {
    this.bookingHistory = history;
  }

  public void generateSummaryReport() {
    List<Reservation> records = bookingHistory.getAllRecords();

    double totalRevenue = records.stream()
            .mapToDouble(Reservation::getAmount)
            .sum();

    System.out.println("\n--- OPERATIONAL SUMMARY REPORT ---");
    System.out.println("Total Bookings Processed: " + records.size());
    System.out.println("Total Revenue Generated: $" + totalRevenue);
    System.out.println("----------------------------------\n");
  }

  public void showDetailedHistory() {
    System.out.println("--- DETAILED AUDIT TRAIL (Insertion Order) ---");
    bookingHistory.getAllRecords().forEach(System.out::println);
  }
}

// --- Main Application ---
public class Main {
  public static void main(String[] args) {
    // Initialize Components
    BookingHistory historyStore = new BookingHistory();
    BookingReportService reportService = new BookingReportService(historyStore);

    // 1. Simulate Bookings being confirmed
    System.out.println("System: Processing bookings...");

    Reservation res1 = new Reservation("BK001", "Alice", 150.00);
    historyStore.addRecord(res1);

    Reservation res2 = new Reservation("BK002", "Bob", 200.50);
    historyStore.addRecord(res2);

    Reservation res3 = new Reservation("BK003", "Charlie", 120.00);
    historyStore.addRecord(res3);

    // 2. Admin Actor requests visibility
    System.out.println("Admin: Requesting reports...");

    reportService.showDetailedHistory();
    reportService.generateSummaryReport();
  }
}