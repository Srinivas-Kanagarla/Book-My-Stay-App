import java.util.ArrayList;
import java.util.List;

class HotelInventory {
  private int availableRooms = 3; // Limited inventory to test contention

  // The 'synchronized' keyword ensures only one guest can book at a time
  public synchronized boolean bookRoom(String guestName) {
    System.out.println(guestName + " is attempting to book...");

    if (availableRooms > 0) {
      // Simulate a small delay in processing to highlight potential race conditions
      try { Thread.sleep(100); } catch (InterruptedException e) {}

      availableRooms--;
      System.out.println("SUCCESS: " + guestName + " secured a room. Rooms left: " + availableRooms);
      return true;
    } else {
      System.out.println("FAILED: No rooms left for " + guestName);
      return false;
    }
  }

  public int getAvailableRooms() {
    return availableRooms;
  }
}

class GuestRequest implements Runnable {
  private HotelInventory inventory;
  private String name;

  public GuestRequest(HotelInventory inventory, String name) {
    this.inventory = inventory;
    this.name = name;
  }

  @Override
  public void run() {
    inventory.bookRoom(name);
  }
}

public class Main {
  public static void main(String[] args) throws InterruptedException {
    HotelInventory sharedInventory = new HotelInventory();
    List<Thread> guests = new ArrayList<>();

    System.out.println("--- Starting Concurrent Booking Simulation ---");
    System.out.println("Initial Inventory: " + sharedInventory.getAvailableRooms() + "\n");

    // Simulate 6 guests trying to book 3 rooms simultaneously
    for (int i = 1; i <= 6; i++) {
      Thread t = new Thread(new GuestRequest(sharedInventory, "Guest-" + i));
      guests.add(t);
      t.start();
    }

    // Wait for all threads to finish
    for (Thread t : guests) {
      t.join();
    }

    System.out.println("\n--- Simulation Complete ---");
    System.out.println("Final Room Count: " + sharedInventory.getAvailableRooms());
  }
}