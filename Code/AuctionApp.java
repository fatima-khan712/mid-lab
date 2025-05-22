/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.auctionapp;

/**
 *
 * @author Rising Star
 */
import java.util.*;

class DatabaseConnection {
    public DatabaseConnection() {
        connect();
    }

    private void connect() {
        
        System.out.println("Connecting to the Auction database...");
        try {
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Database connection established successfully!");
    }
}

class Item {
    private String itemId;
    private String description;
    private double highestBid;
    private String highestBidder;

    public Item(String itemId, String description, double startingBid) {
        this.itemId = itemId;
        this.description = description;
        this.highestBid = startingBid;
        this.highestBidder = "No bids yet";
    }

    public boolean placeBid(String bidder, double bidAmount) {
        if (bidAmount > highestBid) {
            highestBid = bidAmount;
            highestBidder = bidder;
            return true;
        }
        return false;
    }

    public String getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    @Override
    public String toString() {
        return "ItemID: " + itemId + ", Description: " + description + 
               ", Highest Bid: " + highestBid + ", Highest Bidder: " + highestBidder;
    }
}


class BidManager {
    private Map<String, Item> itemMap;

    public BidManager() {
        itemMap = new HashMap<>();
    }

    public void addItem(Item item) {
        itemMap.put(item.getItemId(), item);
    }

    public Item getItem(String itemId) {
        return itemMap.get(itemId);
    }

    public boolean placeBid(String itemId, String bidder, double bidAmount) {
        Item item = itemMap.get(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return false;
        }
        boolean success = item.placeBid(bidder, bidAmount);
        if (success) {
            System.out.println("Bid placed successfully!");
        } else {
            System.out.println("Bid too low. Current highest bid is " + item.getHighestBid());
        }
        return success;
    }

    public void listItems() {
        if (itemMap.isEmpty()) {
            System.out.println("No items available.");
        } else {
            System.out.println("Available Items:");
            for (Item item : itemMap.values()) {
                System.out.println(item);
            }
        }
    }
}

class AuctionController {
    private BidManager bidManager;

    public AuctionController(BidManager bidManager) {
        this.bidManager = bidManager;
    }

    public void addNewItem(String itemId, String description, double startingBid) {
        Item newItem = new Item(itemId, description, startingBid);
        bidManager.addItem(newItem);
        System.out.println("Item added successfully!");
    }

    public void handleBid(String itemId, String bidder, double bidAmount) {
        bidManager.placeBid(itemId, bidder, bidAmount);
    }

    public void showItems() {
        bidManager.listItems();
    }
}

public class AuctionApp {
    public static void main(String[] args) {
        
        DatabaseConnection dbConnection = new DatabaseConnection();

        Scanner scanner = new Scanner(System.in);
        BidManager bidManager = new BidManager();
        AuctionController controller = new AuctionController(bidManager);

        System.out.println("Welcome to the Auction System!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Auction Item");
            System.out.println("2. Place Bid");
            System.out.println("3. List Items");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Item ID: ");
                    String itemId = scanner.nextLine().trim();

                    System.out.print("Enter Item Description: ");
                    String description = scanner.nextLine().trim();

                    System.out.print("Enter Starting Bid: ");
                    double startingBid;
                    try {
                        startingBid = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid bid amount. Try again.");
                        break;
                    }

                    controller.addNewItem(itemId, description, startingBid);
                    break;

                case "2":
                    System.out.print("Enter Item ID to bid on: ");
                    String bidItemId = scanner.nextLine().trim();

                    System.out.print("Enter Your Name: ");
                    String bidder = scanner.nextLine().trim();

                    System.out.print("Enter Your Bid Amount: ");
                    double bidAmount;
                    try {
                        bidAmount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid bid amount. Try again.");
                        break;
                    }

                    controller.handleBid(bidItemId, bidder, bidAmount);
                    break;

                case "3":
                    controller.showItems();
                    break;

                case "4":
                    System.out.println("Exiting... Thank you!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
