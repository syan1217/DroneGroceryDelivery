package edu.gatech.cs6310;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DeliveryService {

    private static final String url = "jdbc:mysql://localhost:3307/delivery_service_db?serverTimezone=UTC";

    private static final String username = "root";
    private static final String password = "123456";

    // [1] make_store
//    public void makeStore(Store newStore) {
//        try {
//            // Create a connection to the database
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//            // Check if store already exists
//            String checkSql = "SELECT name FROM Stores WHERE name = ?";
//            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
//            checkStatement.setString(1, newStore.getName());
//            ResultSet checkResult = checkStatement.executeQuery();
//            if (checkResult.next()) {
//                System.out.println("ERROR:store_identifier_already_exists");
//                return;
//            }
//
//            // Insert the store data into the database
//            String insertSql = "INSERT INTO Stores (name, revenue) VALUES (?, ?)";
//            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
//            insertStatement.setString(1, newStore.getName());
//            insertStatement.setInt(2, newStore.getRevenue());
//            insertStatement.executeUpdate();
//
//            System.out.println("OK:change_completed");
//
//            // Close the database connection
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void makeStore(String name, int revenue, double lat,double lng,String UIN, String passwd, String role,String phone_number) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store already exists
            String checkSql = "SELECT name FROM Stores WHERE name = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, name);
            ResultSet checkResult = checkStatement.executeQuery();
            if (checkResult.next()) {
                System.out.println("ERROR:store_identifier_already_exists");
                return;
            }

            // Insert the store data into the database
            String insertSql = "INSERT INTO Stores (name, revenue, latitude, longitude,UIN,passwd,roleName,phone_number) VALUES (?, ?, ?, ?,?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, name);
            insertStatement.setInt(2, revenue);
            insertStatement.setDouble(3, lat);
            insertStatement.setDouble(4, lng);
            //add String UIN, String passwd, String role, String phone_number
            insertStatement.setString(5, UIN);
            insertStatement.setString(6, passwd);
            insertStatement.setString(7, role);
            insertStatement.setString(8, phone_number);
            insertStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // [2] display_stores
    public void displayStores() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Query the stores table and print the results
            String sql = "SELECT * FROM Stores";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                System.out.println("name:" + result.getString("name") + ",revenue:" + result.getInt("revenue") +
                        ",addressLat:" + result.getDouble("latitude") + ",addressLng:" + result.getDouble("longitude")
                );
            }
            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // [3]sell_item
    public void sellItem(String itemName, String storeName, int weight) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store exists and fetch its data
            String checkStoreSql = "SELECT name, revenue FROM Stores WHERE name = ?";
            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            checkStoreStatement.setString(1, storeName);
            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            // Create a Store instance
            int storeRevenue = checkStoreResult.getInt("revenue");
            Store store = new Store(storeName, storeRevenue, 0,0); //assume lat/long/revenue does not matter.

            // Check if item already exists
            String checkItemSql = "SELECT item_name FROM Items WHERE item_name = ? AND store_name = ?";
            PreparedStatement checkItemStatement = connection.prepareStatement(checkItemSql);
            checkItemStatement.setString(1, itemName);
            checkItemStatement.setString(2, storeName);
            ResultSet checkItemResult = checkItemStatement.executeQuery();
            if (checkItemResult.next()) {
                System.out.println("ERROR:item_identifier_already_exists");
                return;
            }

            // Create an Item instance
            Item newItem = new Item(store, itemName, weight);

            // Insert the item data into the database
            String insertItemSql = "INSERT INTO Items (item_name, item_weight, store_name) VALUES (?, ?, ?)";
            PreparedStatement insertItemStatement = connection.prepareStatement(insertItemSql);
            insertItemStatement.setString(1, newItem.getName());
            insertItemStatement.setInt(2, newItem.getWeight());
            insertItemStatement.setString(3, newItem.getStore().getName());
            insertItemStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[4] display items:
    public void displayItems(String storeName) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store exists
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            checkStoreStatement.setString(1, storeName);
            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            // Fetch items from the database
            String selectItemsSql = "SELECT item_name, item_weight FROM Items WHERE store_name = ?";
            PreparedStatement selectItemsStatement = connection.prepareStatement(selectItemsSql);
            selectItemsStatement.setString(1, storeName);
            ResultSet itemsResult = selectItemsStatement.executeQuery();

            // Display the items
            while (itemsResult.next()) {
                String itemName = itemsResult.getString("item_name");
                int itemWeight = itemsResult.getInt("item_weight");
                System.out.println(itemName + "," + itemWeight);
            }

            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[5] make_pilot:

    public void makePilot(String account, String firstName, String lastName, String phoneNumber, String taxID, String licenseID, int level, String UIN, String passwd, String role) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the pilot's account already exists
            String checkAccountSql = "SELECT account FROM Pilots WHERE account = ?";
            PreparedStatement checkAccountStatement = connection.prepareStatement(checkAccountSql);
            checkAccountStatement.setString(1, account);
            ResultSet checkAccountResult = checkAccountStatement.executeQuery();
            if (checkAccountResult.next()) {
                System.out.println("ERROR:pilot_identifier_already_exists");
                return;
            }

            // Check if the pilot's license ID already exists
            String checkLicenseSql = "SELECT license_id FROM Pilots WHERE license_id = ?";
            PreparedStatement checkLicenseStatement = connection.prepareStatement(checkLicenseSql);
            checkLicenseStatement.setString(1, licenseID);
            ResultSet checkLicenseResult = checkLicenseStatement.executeQuery();
            if (checkLicenseResult.next()) {
                System.out.println("ERROR:pilot_license_already_exists");
                return;
            }

            // Create a new Pilot instance
            Pilot newPilot = new Pilot(account, firstName, lastName, phoneNumber, taxID, licenseID, level);

            // Insert the pilot data into the database
            String insertPilotSql = "INSERT INTO Pilots (account, first_name, last_name, phone_number, tax_id, license_id, level,UIN,passwd,roleName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertPilotStatement = connection.prepareStatement(insertPilotSql);
            insertPilotStatement.setString(1, newPilot.getAccount());
            insertPilotStatement.setString(2, newPilot.getFirstName());
            insertPilotStatement.setString(3, newPilot.getLastName());
            insertPilotStatement.setString(4, newPilot.getPhoneNumber());
            insertPilotStatement.setString(5, newPilot.getTaxID());
            insertPilotStatement.setString(6, newPilot.getLicenseID());
            insertPilotStatement.setInt(7, newPilot.getLevel());
            //add String UIN, String passwd, String role
            insertPilotStatement.setString(8, UIN);
            insertPilotStatement.setString(9, passwd);
            insertPilotStatement.setString(10, role);
            insertPilotStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // [6]display_pilots
    public void displayPilots() {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Query the Pilots table and fetch the data
            String sql = "SELECT * FROM Pilots";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            // Print the pilot data
            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String phoneNumber = result.getString("phone_number");
                String taxID = result.getString("tax_id");
                String licenseID = result.getString("license_id");
                int level = result.getInt("level");

                System.out.println("name:" + firstName + "_" + lastName + ",phone:" + phoneNumber + ","
                        + "taxID:" + taxID + "," + "licenseID:" + licenseID + ","
                        + "experience:" + level);
            }

            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // [7]make_drone:
//    public void makeDrone(String storeName, String ID, int totalCap, int tripLeft) {
//        try {
//            // Create a connection to the database
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//            // Check if store exists
//            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
//            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
//            checkStoreStatement.setString(1, storeName);
//            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
//            if (!checkStoreResult.next()) {
//                System.out.println("ERROR:store_identifier_does_not_exist");
//                return;
//            }
//
//            // Check if drone already exists
//            String checkDroneSql = "SELECT id FROM Drones WHERE id = ? AND store_name = ?";
//            PreparedStatement checkDroneStatement = connection.prepareStatement(checkDroneSql);
//            checkDroneStatement.setString(1, ID);
//            checkDroneStatement.setString(2, storeName);
//            ResultSet checkDroneResult = checkDroneStatement.executeQuery();
//            if (checkDroneResult.next()) {
//                System.out.println("ERROR:drone_identifier_already_exists");
//                return;
//            }
//
//            // Create a Drone instance and insert it into the database
//            Store store = new Store(storeName, 0,0,0); // Assuming revenue doesn't matter for this operation
//            Drone newDrone = new Drone(store, ID, totalCap, tripLeft);
//
//            String insertDroneSql = "INSERT INTO Drones (id, store_name, total_capacity, remaining_capacity, orders, trips_left) VALUES (?, ?, ?, ?, ?, ?)";
//            PreparedStatement insertDroneStatement = connection.prepareStatement(insertDroneSql);
//            insertDroneStatement.setString(1, newDrone.getID());
//            insertDroneStatement.setString(2, newDrone.getStore().getName());
//            insertDroneStatement.setInt(3, newDrone.getTotalCap());
//            insertDroneStatement.setInt(4, newDrone.getRemainCap());
//            insertDroneStatement.setInt(5, newDrone.getOrders());
//            insertDroneStatement.setInt(6, newDrone.getTripLeft());
//            insertDroneStatement.executeUpdate();
//
//            System.out.println("OK:change_completed");
//
//            // Close the database connection
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void makeDrone(String storeName, String ID, int totalCap) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store exists
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            checkStoreStatement.setString(1, storeName);
            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            // Check if drone already exists
            String checkDroneSql = "SELECT id FROM Drones WHERE id = ? AND store_name = ?";
            PreparedStatement checkDroneStatement = connection.prepareStatement(checkDroneSql);
            checkDroneStatement.setString(1, ID);
            checkDroneStatement.setString(2, storeName);
            ResultSet checkDroneResult = checkDroneStatement.executeQuery();
            if (checkDroneResult.next()) {
                System.out.println("ERROR:drone_identifier_already_exists");
                return;
            }

            // Create a Drone instance and insert it into the database
            Store store = new Store(storeName, 0, 0, 0); // Assuming revenue doesn't matter for this operation
            Drone newDrone = new Drone(store, ID, totalCap);
            //newDrone.setCurrentBattery(currentBattery);

            String insertDroneSql = "INSERT INTO Drones (id, store_name, total_capacity, remaining_capacity, orders, current_battery, max_battery) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertDroneStatement = connection.prepareStatement(insertDroneSql);
            insertDroneStatement.setString(1, newDrone.getID());
            insertDroneStatement.setString(2, newDrone.getStore().getName());
            insertDroneStatement.setInt(3, newDrone.getTotalCap());
            insertDroneStatement.setInt(4, newDrone.getRemainCap());
            insertDroneStatement.setInt(5, newDrone.getOrders());
            insertDroneStatement.setInt(6, newDrone.getCurrentBattery());
            insertDroneStatement.setInt(7, newDrone.getMaxBattery());
            insertDroneStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //[8]display_drone - see[10]

    public void flyDrone(String storeName, String droneID, String pilotAccount) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store, drone, and pilot exist
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            String checkDroneSql = "SELECT id FROM Drones WHERE id = ? AND store_name = ?";
            String checkPilotSql = "SELECT account FROM Pilots WHERE account = ?";

            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            PreparedStatement checkDroneStatement = connection.prepareStatement(checkDroneSql);
            PreparedStatement checkPilotStatement = connection.prepareStatement(checkPilotSql);

            checkStoreStatement.setString(1, storeName);
            checkDroneStatement.setString(1, droneID);
            checkDroneStatement.setString(2, storeName);
            checkPilotStatement.setString(1, pilotAccount);

            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            ResultSet checkDroneResult = checkDroneStatement.executeQuery();
            ResultSet checkPilotResult = checkPilotStatement.executeQuery();

            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }
            if (!checkDroneResult.next()) {
                System.out.println("ERROR:drone_identifier_does_not_exist");
                return;
            }
            if (!checkPilotResult.next()) {
                System.out.println("ERROR:pilot_identifier_does_not_exist");
                return;
            }

            // Unassign the previous pilot from the drone
            String unassignPrevPilotSql = "DELETE FROM Drone_Pilots WHERE drone_id = ? AND store_name = ?";
            PreparedStatement unassignPrevPilotStatement = connection.prepareStatement(unassignPrevPilotSql);
            unassignPrevPilotStatement.setString(1, droneID);
            unassignPrevPilotStatement.setString(2, storeName);
            unassignPrevPilotStatement.executeUpdate();

            // Unassign the pilot from any other drone they might be flying
            String unassignPilotFromOtherDronesSql = "DELETE FROM Drone_Pilots WHERE pilot_account = ?";
            PreparedStatement unassignPilotFromOtherDronesStatement = connection.prepareStatement(unassignPilotFromOtherDronesSql);
            unassignPilotFromOtherDronesStatement.setString(1, pilotAccount);
            unassignPilotFromOtherDronesStatement.executeUpdate();

            // Assign the pilot to the drone
            String assignPilotSql = "INSERT INTO Drone_Pilots (drone_id, store_name, pilot_account) VALUES (?, ?, ?)";
            PreparedStatement assignPilotStatement = connection.prepareStatement(assignPilotSql);
            assignPilotStatement.setString(1, droneID);
            assignPilotStatement.setString(2, storeName);
            assignPilotStatement.setString(3, pilotAccount);
            assignPilotStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //[10]display_drone:
//    public void displayDrone(String storeName) {
//        try {
//            // Create a connection to the database
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//            // Check if the store exists
//            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
//            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
//            checkStoreStatement.setString(1, storeName);
//            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
//            if (!checkStoreResult.next()) {
//                System.out.println("ERROR:store_identifier_does_not_exist");
//                return;
//            }
//
//            // Query drone and pilot data for the specific store
//            String displayDroneSql = "SELECT Drones.id, Drones.total_capacity, Drones.remaining_capacity, Drones.orders, Drones.trips_left, Pilots.first_name, Pilots.last_name " +
//                    "FROM Drones " +
//                    "LEFT JOIN Drone_Pilots ON Drones.id = Drone_Pilots.drone_id AND Drones.store_name = Drone_Pilots.store_name " +
//                    "LEFT JOIN Pilots ON Drone_Pilots.pilot_account = Pilots.account " +
//                    "WHERE Drones.store_name = ? " +
//                    "ORDER BY Drones.id";
//            PreparedStatement displayDroneStatement = connection.prepareStatement(displayDroneSql);
//            displayDroneStatement.setString(1, storeName);
//            ResultSet displayDroneResult = displayDroneStatement.executeQuery();
//
//            // Display drone information
//            while (displayDroneResult.next()) {
//                String flownBy = displayDroneResult.getString("first_name") == null ? "" : ",flown_by:" + displayDroneResult.getString("first_name") + "_" + displayDroneResult.getString("last_name");
//                System.out.println("droneID:" + displayDroneResult.getString("id") + ",total_cap:" + displayDroneResult.getInt("total_capacity")
//                        + ",num_orders:" + displayDroneResult.getInt("orders") + ",remaining_cap:"
//                        + displayDroneResult.getInt("remaining_capacity") + ",trips_left:"
//                        + displayDroneResult.getInt("trips_left") + flownBy);
//            }
//            System.out.println("OK:display_completed");
//
//            // Close the database connection
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void displayDrone(String storeName) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the store exists
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            checkStoreStatement.setString(1, storeName);
            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            // Query drone and pilot data for the specific store
            String displayDroneSql = "SELECT Drones.id, Drones.total_capacity, Drones.remaining_capacity, Drones.orders, Drones.current_battery, Pilots.first_name, Pilots.last_name " +
                    "FROM Drones " +
                    "LEFT JOIN Drone_Pilots ON Drones.id = Drone_Pilots.drone_id AND Drones.store_name = Drone_Pilots.store_name " +
                    "LEFT JOIN Pilots ON Drone_Pilots.pilot_account = Pilots.account " +
                    "WHERE Drones.store_name = ? " +
                    "ORDER BY Drones.id";
            PreparedStatement displayDroneStatement = connection.prepareStatement(displayDroneSql);
            displayDroneStatement.setString(1, storeName);
            ResultSet displayDroneResult = displayDroneStatement.executeQuery();

            // Display drone information
            while (displayDroneResult.next()) {
                String flownBy = displayDroneResult.getString("first_name") == null ? "" : ",flown_by:" + displayDroneResult.getString("first_name") + "_" + displayDroneResult.getString("last_name");
                System.out.println("droneID:" + displayDroneResult.getString("id") + ",total_cap:" + displayDroneResult.getInt("total_capacity")
                        + ",num_orders:" + displayDroneResult.getInt("orders") + ",remaining_cap:"
                        + displayDroneResult.getInt("remaining_capacity") + ",current_battery:"
                        + displayDroneResult.getInt("current_battery") + flownBy);
            }
            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[11]make_customer:
    public void makeCustomer(String account, String firstName, String lastName, String phoneNumber, int rating, int credit, String UIN, String passwd, String role) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the customer's account already exists
            String checkAccountSql = "SELECT account FROM Customers WHERE account = ?";
            PreparedStatement checkAccountStatement = connection.prepareStatement(checkAccountSql);
            checkAccountStatement.setString(1, account);
            ResultSet checkAccountResult = checkAccountStatement.executeQuery();

            if (checkAccountResult.next()) {
                System.out.println("ERROR:customer_identifier_already_exists");
                return;
            }

            // Create a new Customer instance
            Customer newCustomer = new Customer(account, firstName, lastName, phoneNumber, rating, credit);

            // Insert the customer data into the database
            String insertCustomerSql = "INSERT INTO Customers (account, first_name, last_name, phone_number, rating, credit, hold_credit,UIN,passwd,roleName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertCustomerStatement = connection.prepareStatement(insertCustomerSql);
            insertCustomerStatement.setString(1, newCustomer.getAccount());
            insertCustomerStatement.setString(2, newCustomer.getFirstName());
            insertCustomerStatement.setString(3, newCustomer.getLastName());
            insertCustomerStatement.setString(4, newCustomer.getPhoneNumber());
            insertCustomerStatement.setInt(5, newCustomer.getRating());
            insertCustomerStatement.setInt(6, newCustomer.getCredit());
            insertCustomerStatement.setInt(7, newCustomer.getHoldCredit());
            //add String UIN, String passwd, String role
            insertCustomerStatement.setString(8, UIN);
            insertCustomerStatement.setString(9, passwd);
            insertCustomerStatement.setString(10, role);
            insertCustomerStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[12] display_customers:
    public void displayCustomers() {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Retrieve all customer data from the database
            String getCustomersSql = "SELECT * FROM Customers";
            PreparedStatement getCustomersStatement = connection.prepareStatement(getCustomersSql);
            ResultSet customersResult = getCustomersStatement.executeQuery();

            // Display each customer's information
            while (customersResult.next()) {
                System.out.println("name:" + customersResult.getString("first_name") + "_" + customersResult.getString("last_name")
                        + ",phone:" + customersResult.getString("phone_number") + ","
                        + "rating:" + customersResult.getInt("rating") + "," + "credit:" + customersResult.getInt("credit"));
            }

            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[13] start_order:
    public void startOrder(String storeName, String orderID, String droneID, String customerAccount, double addressLat, double addressLng) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store, drone, and customer exist and if the order identifier is unique
            String checkStoreSql = "SELECT name, revenue FROM Stores WHERE name = ?";
            String checkDroneSql = "SELECT id, store_name, total_capacity, remaining_capacity, orders FROM Drones WHERE id = ? AND store_name = ?";
            String checkCustomerSql = "SELECT account, first_name, last_name, phone_number, rating, credit, hold_credit FROM Customers WHERE account = ?";
            String checkOrderSql = "SELECT id FROM Orders WHERE id = ? AND store_name = ?";

            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            PreparedStatement checkDroneStatement = connection.prepareStatement(checkDroneSql);
            PreparedStatement checkCustomerStatement = connection.prepareStatement(checkCustomerSql);
            PreparedStatement checkOrderStatement = connection.prepareStatement(checkOrderSql);

            checkStoreStatement.setString(1, storeName);
            checkDroneStatement.setString(1, droneID);
            checkDroneStatement.setString(2, storeName);
            checkCustomerStatement.setString(1, customerAccount);
            checkOrderStatement.setString(1, orderID);
            checkOrderStatement.setString(2, storeName);

            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            ResultSet checkDroneResult = checkDroneStatement.executeQuery();
            ResultSet checkCustomerResult = checkCustomerStatement.executeQuery();
            ResultSet checkOrderResult = checkOrderStatement.executeQuery();

            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }
            if (!checkDroneResult.next()) {
                System.out.println("ERROR:drone_identifier_does_not_exist");
                return;
            }
            if (!checkCustomerResult.next()) {
                System.out.println("ERROR:customer_identifier_does_not_exist");
                return;
            }
            if (checkOrderResult.next()) {
                System.out.println("ERROR:order_identifier_already_exists");
                return;
            }

            // Insert the new order into the Orders table
            String insertOrderSql = "INSERT INTO Orders (id, store_name, drone_id, customer_account, address_lat, address_lng) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderSql);

            insertOrderStatement.setString(1, orderID);
            insertOrderStatement.setString(2, storeName);
            insertOrderStatement.setString(3, droneID);
            insertOrderStatement.setString(4, customerAccount);
            insertOrderStatement.setDouble(5, addressLat);
            insertOrderStatement.setDouble(6, addressLng);

            insertOrderStatement.executeUpdate();

            // Create Store, Drone, and Customer objects
            Store store = new Store(storeName, checkStoreResult.getInt("revenue"),0,0);//store name does matter.
            //Drone drone = new Drone(store, droneID, checkDroneResult.getInt("total_capacity"), checkDroneResult.getInt("trips_left"));
            Drone drone = new Drone(store, droneID, checkDroneResult.getInt("total_capacity") );
            Customer customer = new Customer(
                    checkCustomerResult.getString("account"),
                    checkCustomerResult.getString("first_name"),
                    checkCustomerResult.getString("last_name"),
                    checkCustomerResult.getString("phone_number"),
                    checkCustomerResult.getInt("rating"),
                    checkCustomerResult.getInt("credit")
            );

            // Create a new Order object with the fetched data
            Order order = new Order(store, orderID, customer, drone, addressLat, addressLng);

            // Update the number of orders for the drone in the database
            String updateDroneOrdersSql = "UPDATE Drones SET orders = orders + 1 WHERE id = ? AND store_name = ?";
            PreparedStatement updateDroneOrdersStatement = connection.prepareStatement(updateDroneOrdersSql);

            updateDroneOrdersStatement.setString(1, droneID);
            updateDroneOrdersStatement.setString(2, storeName);

            updateDroneOrdersStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // [14]display_orders - see [16]

    // [15]request_item
    public void requestItem(String storeName, String orderID, String itemName, int quantity, int unitPrice) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the store, order, and item exist, and if the item has not been ordered already
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            String checkOrderSql = "SELECT id, store_name, drone_id, customer_account FROM Orders WHERE id = ? AND store_name = ?";
            String checkItemSql = "SELECT item_name, item_weight FROM Items WHERE item_name = ? AND store_name = ?";
            String checkOrderItemSql = "SELECT item_name FROM Order_Items WHERE order_id = ? AND store_name = ? AND item_name = ?";

            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            PreparedStatement checkOrderStatement = connection.prepareStatement(checkOrderSql);
            PreparedStatement checkItemStatement = connection.prepareStatement(checkItemSql);
            PreparedStatement checkOrderItemStatement = connection.prepareStatement(checkOrderItemSql);

            checkStoreStatement.setString(1, storeName);
            checkOrderStatement.setString(1, orderID);
            checkOrderStatement.setString(2, storeName);
            checkItemStatement.setString(1, itemName);
            checkItemStatement.setString(2, storeName);
            checkOrderItemStatement.setString(1, orderID);
            checkOrderItemStatement.setString(2, storeName);
            checkOrderItemStatement.setString(3, itemName);

            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            ResultSet checkOrderResult = checkOrderStatement.executeQuery();
            ResultSet checkItemResult = checkItemStatement.executeQuery();
            ResultSet checkOrderItemResult = checkOrderItemStatement.executeQuery();

            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }
            if (!checkOrderResult.next()) {
                System.out.println("ERROR:order_identifier_does_not_exist");
                return;
            }
            if (!checkItemResult.next()) {
                System.out.println("ERROR:item_identifier_does_not_exist");
                return;
            }
            if (checkOrderItemResult.next()) {
                System.out.println("ERROR:item_already_ordered");
                return;
            }

            // Check if the customer can afford the item and if the drone can carry the item
            String customerAccount = checkOrderResult.getString("customer_account");
            String droneID = checkOrderResult.getString("drone_id");
            int itemWeight = checkItemResult.getInt("item_weight");

            String checkCustomerCreditSql = "SELECT credit, hold_credit FROM Customers WHERE account = ?";
            PreparedStatement checkCustomerCreditStatement = connection.prepareStatement(checkCustomerCreditSql);
            checkCustomerCreditStatement.setString(1, customerAccount);
            ResultSet checkCustomerCreditResult = checkCustomerCreditStatement.executeQuery();
            checkCustomerCreditResult.next();

            int customerCredit = checkCustomerCreditResult.getInt("credit");
            int customerHoldCredit = checkCustomerCreditResult.getInt("hold_credit");

            if (unitPrice * quantity + customerHoldCredit > customerCredit) {
                System.out.println("ERROR:customer_cant_afford_new_item");
                return;
            }

            String checkDroneCapacitySql = "SELECT remaining_capacity FROM Drones WHERE id = ? AND store_name = ?";
            PreparedStatement checkDroneCapacityStatement = connection.prepareStatement(checkDroneCapacitySql);
            checkDroneCapacityStatement.setString(1, droneID);
            checkDroneCapacityStatement.setString(2, storeName);
            ResultSet checkDroneCapacityResult = checkDroneCapacityStatement.executeQuery();
            checkDroneCapacityResult.next();

            int droneRemainingCapacity = checkDroneCapacityResult.getInt("remaining_capacity");
            if (itemWeight * quantity > droneRemainingCapacity) {
                System.out.println("ERROR:drone_cant_carry_new_item");
                return;
            }

            // Update the customer's hold_credit and insert the item into the Order_Items table
            String updateCustomerHoldCreditSql = "UPDATE Customers SET hold_credit = hold_credit + ? WHERE account = ?";
            String insertOrderItemSql = "INSERT INTO Order_Items (order_id, store_name, item_name, unit_price, quantity) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement updateCustomerHoldCreditStatement = connection.prepareStatement(updateCustomerHoldCreditSql);
            PreparedStatement insertOrderItemStatement = connection.prepareStatement(insertOrderItemSql);

            updateCustomerHoldCreditStatement.setInt(1, unitPrice * quantity);
            updateCustomerHoldCreditStatement.setString(2, customerAccount);
            insertOrderItemStatement.setString(1, orderID);
            insertOrderItemStatement.setString(2, storeName);
            insertOrderItemStatement.setString(3, itemName);
            insertOrderItemStatement.setInt(4, unitPrice);
            insertOrderItemStatement.setInt(5, quantity);

            updateCustomerHoldCreditStatement.executeUpdate();
            insertOrderItemStatement.executeUpdate();

            // Update the total_price in the Orders table
            String updateOrderTotalPriceSql = "UPDATE Orders SET total_price = total_price + ? WHERE id = ? AND store_name = ?";
            PreparedStatement updateOrderTotalPriceStatement = connection.prepareStatement(updateOrderTotalPriceSql);

            updateOrderTotalPriceStatement.setInt(1, unitPrice * quantity);
            updateOrderTotalPriceStatement.setString(2, orderID);
            updateOrderTotalPriceStatement.setString(3, storeName);

            updateOrderTotalPriceStatement.executeUpdate();

            // Update the drone's remaining_capacity
            String updateDroneRemainingCapacitySql = "UPDATE Drones SET remaining_capacity = remaining_capacity - ? WHERE id = ? AND store_name = ?";
            PreparedStatement updateDroneRemainingCapacityStatement = connection.prepareStatement(updateDroneRemainingCapacitySql);

            updateDroneRemainingCapacityStatement.setInt(1, itemWeight * quantity);
            updateDroneRemainingCapacityStatement.setString(2, droneID);
            updateDroneRemainingCapacityStatement.setString(3, storeName);

            updateDroneRemainingCapacityStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[16] display_orders:
    public void displayOrders(String storeName) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the store exists
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            checkStoreStatement.setString(1, storeName);
            ResultSet checkStoreResult = checkStoreStatement.executeQuery();

            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            // Retrieve the orders and order items
            String ordersSql = "SELECT id FROM Orders WHERE store_name = ?";
            String orderItemsSql = "SELECT Order_Items.item_name, unit_price, quantity, item_weight FROM Order_Items INNER JOIN Items ON Order_Items.item_name = Items.item_name AND Order_Items.store_name = Items.store_name WHERE order_id = ? AND Order_Items.store_name = ?";

            PreparedStatement ordersStatement = connection.prepareStatement(ordersSql);
            PreparedStatement orderItemsStatement = connection.prepareStatement(orderItemsSql);

            ordersStatement.setString(1, storeName);

            ResultSet ordersResult = ordersStatement.executeQuery();

            while (ordersResult.next()) {
                String orderID = ordersResult.getString("id");
                System.out.println("orderID:" + orderID);

                orderItemsStatement.setString(1, orderID);
                orderItemsStatement.setString(2, storeName);

                ResultSet orderItemsResult = orderItemsStatement.executeQuery();

                while (orderItemsResult.next()) {
                    String itemName = orderItemsResult.getString("item_name");
                    int unitPrice = orderItemsResult.getInt("unit_price");
                    int quantity = orderItemsResult.getInt("quantity");
                    int weight = orderItemsResult.getInt("item_weight");

                    System.out.println("item_name:" + itemName + ",total_quantity:" + quantity
                            + ",total_cost:" + quantity * unitPrice
                            + ",total_weight:" + weight * quantity);
                }
            }

            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[17]review:

    //[18] purchase order:
//    public void purchaseOrder(String storeName, String orderID) {
//        try {
//            Connection connection = DriverManager.getConnection(url, username, password);
//
//            // Check if the store exists
//            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
//            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
//            checkStoreStatement.setString(1, storeName);
//            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
//
//            if (!checkStoreResult.next()) {
//                System.out.println("ERROR:store_identifier_does_not_exist");
//                return;
//            }
//
//            // Check if the order exists and get the drone ID and customer account
//            String checkOrderSql = "SELECT drone_id, customer_account, total_price FROM Orders WHERE store_name = ? AND id = ?";
//            PreparedStatement checkOrderStatement = connection.prepareStatement(checkOrderSql);
//            checkOrderStatement.setString(1, storeName);
//            checkOrderStatement.setString(2, orderID);
//            ResultSet checkOrderResult = checkOrderStatement.executeQuery();
//
//            if (!checkOrderResult.next()) {
//                System.out.println("ERROR:order_identifier_does_not_exist");
//                return;
//            }
//
//            String droneID = checkOrderResult.getString("drone_id");
//            String customerAccount = checkOrderResult.getString("customer_account");
//            int totalPrice = checkOrderResult.getInt("total_price");
//
//            // Check if the drone has a pilot and has enough fuel
//            String checkDroneSql = "SELECT remaining_capacity, trips_left FROM Drones WHERE store_name = ? AND id = ?";
//            PreparedStatement checkDroneStatement = connection.prepareStatement(checkDroneSql);
//            checkDroneStatement.setString(1, storeName);
//            checkDroneStatement.setString(2, droneID);
//            ResultSet checkDroneResult = checkDroneStatement.executeQuery();
//
//            if (!checkDroneResult.next()) {
//                System.out.println("ERROR:drone_identifier_does_not_exist");
//                return;
//            }
//
//            String checkPilotSql = "SELECT pilot_account FROM Drone_Pilots WHERE store_name = ? AND drone_id = ?";
//            PreparedStatement checkPilotStatement = connection.prepareStatement(checkPilotSql);
//            checkPilotStatement.setString(1, storeName);
//            checkPilotStatement.setString(2, droneID);
//            ResultSet checkPilotResult = checkPilotStatement.executeQuery();
//
//            if (!checkPilotResult.next()) {
//                System.out.println("ERROR:drone_needs_pilot");
//                return;
//            }
//
//            String pilotAccount = checkPilotResult.getString("pilot_account");
//
//            if (checkDroneResult.getInt("trips_left") < 1) {
//                System.out.println("ERROR:drone_needs_fuel");
//                return;
//            }
//
//            // Update customer credits and hold credits
//            String updateCustomerSql = "UPDATE Customers SET credit = credit - ?, hold_credit = hold_credit - ? WHERE account = ?";
//            PreparedStatement updateCustomerStatement = connection.prepareStatement(updateCustomerSql);
//            updateCustomerStatement.setInt(1, totalPrice);
//            updateCustomerStatement.setInt(2, totalPrice);
//            updateCustomerStatement.setString(3, customerAccount);
//            updateCustomerStatement.executeUpdate();
//
//
//            // Update store revenue
//            String updateStoreSql = "UPDATE Stores SET revenue = revenue + ? WHERE name = ?";
//            PreparedStatement updateStoreStatement = connection.prepareStatement(updateStoreSql);
//            updateStoreStatement.setInt(1, totalPrice);
//            updateStoreStatement.setString(2, storeName);
//            updateStoreStatement.executeUpdate();
//
//            // Update drone trips left and remove the order from the drone
//            //get totalWeight:
//            String getTotalWeightSql = "SELECT SUM(OI.quantity * I.item_weight) AS total_weight FROM Order_Items OI JOIN Items I ON OI.item_name = I.item_name AND OI.store_name = I.store_name WHERE OI.store_name = ? AND OI.order_id = ?";
//            PreparedStatement getTotalWeightStatement = connection.prepareStatement(getTotalWeightSql);
//            getTotalWeightStatement.setString(1, storeName);
//            getTotalWeightStatement.setString(2, orderID);
//            ResultSet getTotalWeightResult = getTotalWeightStatement.executeQuery();
//            getTotalWeightResult.next();
//            int totalWeight = getTotalWeightResult.getInt("total_weight");
//
////            String updateDroneSql = "UPDATE Drones SET trips_left = trips_left - 1, orders = orders - 1, remaining_capacity = remaining_capacity + ? WHERE store_name = ? AND id= ?";
////            PreparedStatement updateDroneStatement = connection.prepareStatement(updateDroneSql);
////            updateDroneStatement.setInt(1, totalWeight);
////            updateDroneStatement.setString(2, storeName);
////            updateDroneStatement.setString(3, droneID);
////            updateDroneStatement.executeUpdate();
//
//            // Get the number of orders for the drone
//            String droneOrdersSql = "SELECT orders FROM Drones WHERE store_name = ? AND id = ?";
//            PreparedStatement droneOrdersStatement = connection.prepareStatement(droneOrdersSql);
//            droneOrdersStatement.setString(1, storeName);
//            droneOrdersStatement.setString(2, droneID);
//            ResultSet droneOrdersResult = droneOrdersStatement.executeQuery();
//            droneOrdersResult.next();
//            int droneOrders = droneOrdersResult.getInt("orders");
//
//            int overloads = droneOrders > 1 ? droneOrders - 1 : 0;
//
//            // Update drone trips left, remove the order from the drone
//            String updateDroneSql = "UPDATE Drones SET trips_left = trips_left - 1, orders = orders - 1, remaining_capacity = remaining_capacity + ? WHERE store_name = ? AND id= ?";
//            PreparedStatement updateDroneStatement = connection.prepareStatement(updateDroneSql);
//            updateDroneStatement.setInt(1, totalWeight);
//            updateDroneStatement.setString(2, storeName);
//            updateDroneStatement.setString(3, droneID);
//            updateDroneStatement.executeUpdate();
//
//            // Update store overloads
//            String updateStoreOverloadsSql = "UPDATE Stores SET num_overloads = num_overloads + ? WHERE name = ?";
//            PreparedStatement updateStoreOverloadsStatement = connection.prepareStatement(updateStoreOverloadsSql);
//            updateStoreOverloadsStatement.setInt(1, overloads);
//            updateStoreOverloadsStatement.setString(2, storeName);
//            updateStoreOverloadsStatement.executeUpdate();
//
//            // Update pilot level
//            String updatePilotSql = "UPDATE Pilots SET level = level + 1 WHERE account = ?";
//            PreparedStatement updatePilotStatement = connection.prepareStatement(updatePilotSql);
//            updatePilotStatement.setString(1, pilotAccount);
//            updatePilotStatement.executeUpdate();
//
//            // Remove the order items from the Order_Items table
//            String removeOrderItemsSql = "DELETE FROM Order_Items WHERE store_name = ? AND order_id = ?";
//            PreparedStatement removeOrderItemsStatement = connection.prepareStatement(removeOrderItemsSql);
//            removeOrderItemsStatement.setString(1, storeName);
//            removeOrderItemsStatement.setString(2, orderID);
//            removeOrderItemsStatement.executeUpdate();
//
//            // Remove the order from the Orders table
//            String removeOrderSql = "DELETE FROM Orders WHERE store_name = ? AND id = ?";
//            PreparedStatement removeOrderStatement = connection.prepareStatement(removeOrderSql);
//            removeOrderStatement.setString(1, storeName);
//            removeOrderStatement.setString(2, orderID);
//            removeOrderStatement.executeUpdate();
//
//            // Update the number of purchases for the store
//            String updateStorePurchasesSql = "UPDATE Stores SET num_purchases = num_purchases + 1 WHERE name = ?";
//            PreparedStatement updateStorePurchasesStatement = connection.prepareStatement(updateStorePurchasesSql);
//            updateStorePurchasesStatement.setString(1, storeName);
//            updateStorePurchasesStatement.executeUpdate();
//
//
//            System.out.println("OK:change_completed");
//
//            // Close the database connection
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    //calculate distance (helper function):
    public static int calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        int distance = (int) Math.hypot(lat2 - lat1, lng2 - lng1);
        return distance;
    }

    public void purchaseOrder(String storeName, String orderID, int remainBattery, int batteryRecordTime, int currentTime, int dateChange){
        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the store exists and get its latitude and longitude
            String checkStoreSql = "SELECT name, latitude, longitude FROM Stores WHERE name = ?";
            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            checkStoreStatement.setString(1, storeName);
            ResultSet checkStoreResult = checkStoreStatement.executeQuery();

            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            double storeLat = checkStoreResult.getDouble("latitude");
            double storeLng = checkStoreResult.getDouble("longitude");

            // Check if the order exists and get the drone ID and customer account
            String checkOrderSql = "SELECT drone_id, customer_account, total_price FROM Orders WHERE store_name = ? AND id = ?";
            PreparedStatement checkOrderStatement = connection.prepareStatement(checkOrderSql);
            checkOrderStatement.setString(1, storeName);
            checkOrderStatement.setString(2, orderID);
            ResultSet checkOrderResult = checkOrderStatement.executeQuery();

            if (!checkOrderResult.next()) {
                System.out.println("ERROR:order_identifier_does_not_exist");
                return;
            }

            String droneID = checkOrderResult.getString("drone_id");
            String customerAccount = checkOrderResult.getString("customer_account");
            int totalPrice = checkOrderResult.getInt("total_price");

            // Check if the drone has a pilot and has enough fuel
            String checkDroneSql = "SELECT remaining_capacity, current_battery FROM Drones WHERE store_name = ? AND id = ?";
            PreparedStatement checkDroneStatement = connection.prepareStatement(checkDroneSql);
            checkDroneStatement.setString(1, storeName);
            checkDroneStatement.setString(2, droneID);
            ResultSet checkDroneResult = checkDroneStatement.executeQuery();

            if (!checkDroneResult.next()) {
                System.out.println("ERROR:drone_identifier_does_not_exist");
                return;
            }

            String checkPilotSql = "SELECT pilot_account FROM Drone_Pilots WHERE store_name = ? AND drone_id = ?";
            PreparedStatement checkPilotStatement = connection.prepareStatement(checkPilotSql);
            checkPilotStatement.setString(1, storeName);
            checkPilotStatement.setString(2, droneID);
            ResultSet checkPilotResult = checkPilotStatement.executeQuery();

            if (!checkPilotResult.next()) {
                System.out.println("ERROR:drone_needs_pilot");
                return;
            }

            String pilotAccount = checkPilotResult.getString("pilot_account");


            // Get all orders, related drone information, and total weight for the store
//            String getOrdersSql = "SELECT O.id, O.drone_id, O.customer_account, O.total_price, O.address_lat, O.address_lng, SUM(Items.item_weight * Order_Items.quantity) as weight, D.total_capacity, D.remaining_capacity, D.orders, D.current_battery, D.max_battery FROM Orders O INNER JOIN Drones D ON O.drone_id = D.id AND O.store_name = D.store_name INNER JOIN Order_Items ON O.id = Order_Items.order_id AND O.store_name = Order_Items.store_name INNER JOIN Items ON Order_Items.item_name = Items.item_name AND Order_Items.store_name = Items.store_name WHERE O.store_name = ? GROUP BY O.id, O.drone_id, O.customer_account, O.total_price, O.address_lat, O.address_lng, D.total_capacity, D.remaining_capacity, D.orders, D.current_battery, D.max_battery";
//            PreparedStatement getOrdersStatement = connection.prepareStatement(getOrdersSql);
//            getOrdersStatement.setString(1, storeName);
//            ResultSet getOrdersResult = getOrdersStatement.executeQuery();

            String getOrdersSql = "SELECT O.id, O.drone_id, O.customer_account, O.total_price, O.address_lat, O.address_lng, SUM(Items.item_weight * Order_Items.quantity) as weight, D.total_capacity, D.remaining_capacity, D.orders, D.current_battery, D.max_battery FROM Orders O INNER JOIN Drones D ON O.drone_id = D.id AND O.store_name = D.store_name INNER JOIN Order_Items ON O.id = Order_Items.order_id AND O.store_name = Order_Items.store_name INNER JOIN Items ON Order_Items.item_name = Items.item_name AND Order_Items.store_name = Items.store_name WHERE O.store_name = ? AND O.drone_id = ? GROUP BY O.id, O.drone_id, O.customer_account, O.total_price, O.address_lat, O.address_lng, D.total_capacity, D.remaining_capacity, D.orders, D.current_battery, D.max_battery";
            PreparedStatement getOrdersStatement = connection.prepareStatement(getOrdersSql);
            getOrdersStatement.setString(1, storeName);
            getOrdersStatement.setString(2, droneID); // Pass the droneID as a parameter
            ResultSet getOrdersResult = getOrdersStatement.executeQuery();

            double previousLat = storeLat;
            double previousLng = storeLng;
            int totalDistance = 0;
            int totalBatteryUsed = 0;

            Drone drone = null;

            while (getOrdersResult.next()) {

                String droneID1 = getOrdersResult.getString("drone_id");
                double addressLat1 = getOrdersResult.getDouble("address_lat");
                double addressLng1 = getOrdersResult.getDouble("address_lng");
                int weight = getOrdersResult.getInt("weight");
                int totalCapacity1 = getOrdersResult.getInt("total_capacity");

                Store store1 = new Store(storeName, 0, 0, 0);
                Drone drone1 = new Drone(store1, droneID1, totalCapacity1);
                int standByBatt = drone1.chargeDrone(batteryRecordTime, currentTime, dateChange);

                int distance = calculateDistance(previousLat, previousLng, addressLat1, addressLng1);
                totalDistance += distance;
                totalBatteryUsed += drone1.batteryUse(distance, weight);

//                System.out.println( "total battery used: " + totalBatteryUsed);
//                System.out.println( "total battery we have: " + standByBatt);
//                System.out.println("distance: " + distance);
//                System.out.println("weight: " + weight);

                if (standByBatt < totalBatteryUsed) {
                    System.out.println("ERROR:drone_needs_charge");
                    return;
                }

                // Update the previousLat and previousLng for the next iteration
                previousLat = addressLat1;
                previousLng = addressLng1;
            }

            // Update customer credits and hold credits
            String updateCustomerSql = "UPDATE Customers SET credit = credit - ?, hold_credit = hold_credit - ? WHERE account = ?";
            PreparedStatement updateCustomerStatement = connection.prepareStatement(updateCustomerSql);
            updateCustomerStatement.setInt(1, totalPrice);
            updateCustomerStatement.setInt(2, totalPrice);
            updateCustomerStatement.setString(3, customerAccount);
            updateCustomerStatement.executeUpdate();

            //update if delivery On Time: have to create some objects:
            Store store2 = new Store(storeName, 0, 0, 0);
            Drone drone2 = new Drone(store2, droneID, 0);
            Customer newCustomer = new Customer("a", "A", "B", "0", 0, 0);
            Order order2 = new Order(store2, orderID, newCustomer, drone2, 0, 0);
            order2.setOnTime(true);

            // store add revenue, if not on time less 15%
            if (order2.getOnTime()== false) {
                 totalPrice =  (int) Math.round(totalPrice * 0.85);
            }

            // Update store revenue
            String updateStoreSql = "UPDATE Stores SET revenue = revenue + ? WHERE name = ?";
            PreparedStatement updateStoreStatement = connection.prepareStatement(updateStoreSql);
            updateStoreStatement.setInt(1, totalPrice);
            updateStoreStatement.setString(2, storeName);
            updateStoreStatement.executeUpdate();

            // Update drone trips left and remove the order from the drone

            //get totalWeight:
            String getTotalWeightSql = "SELECT SUM(OI.quantity * I.item_weight) AS total_weight FROM Order_Items OI JOIN Items I ON OI.item_name = I.item_name AND OI.store_name = I.store_name WHERE OI.store_name = ? AND OI.order_id = ?";
            PreparedStatement getTotalWeightStatement = connection.prepareStatement(getTotalWeightSql);
            getTotalWeightStatement.setString(1, storeName);
            getTotalWeightStatement.setString(2, orderID);
            ResultSet getTotalWeightResult = getTotalWeightStatement.executeQuery();
            getTotalWeightResult.next();
            int totalWeight = getTotalWeightResult.getInt("total_weight");

            // Get the number of orders for the drone
            String droneOrdersSql = "SELECT orders FROM Drones WHERE store_name = ? AND id = ?";
            PreparedStatement droneOrdersStatement = connection.prepareStatement(droneOrdersSql);
            droneOrdersStatement.setString(1, storeName);
            droneOrdersStatement.setString(2, droneID);
            ResultSet droneOrdersResult = droneOrdersStatement.executeQuery();
            droneOrdersResult.next();
            int droneOrders = droneOrdersResult.getInt("orders");

            int overloads = droneOrders > 1 ? droneOrders - 1 : 0;

            // Update drone trips left, remove the order from the drone
            String updateDroneSql = "UPDATE Drones SET current_battery = ?, orders = orders - 1, remaining_capacity = remaining_capacity + ? WHERE store_name = ? AND id= ?";
            PreparedStatement updateDroneStatement = connection.prepareStatement(updateDroneSql);
            updateDroneStatement.setInt(1, remainBattery);
            updateDroneStatement.setInt(2, totalWeight);
            updateDroneStatement.setString(3, storeName);
            updateDroneStatement.setString(4, droneID);
            updateDroneStatement.executeUpdate();

            // Update store overloads
            String updateStoreOverloadsSql = "UPDATE Stores SET num_overloads = num_overloads + ? WHERE name = ?";
            PreparedStatement updateStoreOverloadsStatement = connection.prepareStatement(updateStoreOverloadsSql);
            updateStoreOverloadsStatement.setInt(1, overloads);
            updateStoreOverloadsStatement.setString(2, storeName);
            updateStoreOverloadsStatement.executeUpdate();

            // Update pilot level
            String updatePilotSql = "UPDATE Pilots SET level = level + 1 WHERE account = ?";
            PreparedStatement updatePilotStatement = connection.prepareStatement(updatePilotSql);
            updatePilotStatement.setString(1, pilotAccount);
            updatePilotStatement.executeUpdate();

            // Remove the order items from the Order_Items table
            String removeOrderItemsSql = "DELETE FROM Order_Items WHERE store_name = ? AND order_id = ?";
            PreparedStatement removeOrderItemsStatement = connection.prepareStatement(removeOrderItemsSql);
            removeOrderItemsStatement.setString(1, storeName);
            removeOrderItemsStatement.setString(2, orderID);
            removeOrderItemsStatement.executeUpdate();

            // Remove the order from the Orders table
            String removeOrderSql = "DELETE FROM Orders WHERE store_name = ? AND id = ?";
            PreparedStatement removeOrderStatement = connection.prepareStatement(removeOrderSql);
            removeOrderStatement.setString(1, storeName);
            removeOrderStatement.setString(2, orderID);
            removeOrderStatement.executeUpdate();

            // Update the number of purchases for the store
            String updateStorePurchasesSql = "UPDATE Stores SET num_purchases = num_purchases + 1 WHERE name = ?";
            PreparedStatement updateStorePurchasesStatement = connection.prepareStatement(updateStorePurchasesSql);
            updateStorePurchasesStatement.setString(1, storeName);
            updateStorePurchasesStatement.executeUpdate();


            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[19][20][21]: review

    //[22]: cancel_order:
    public void cancelOrder(String storeName, String orderID) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if store and order exist
            String checkStoreSql = "SELECT name FROM Stores WHERE name = ?";
            String checkOrderSql = "SELECT id FROM Orders WHERE id = ? AND store_name = ?";

            PreparedStatement checkStoreStatement = connection.prepareStatement(checkStoreSql);
            PreparedStatement checkOrderStatement = connection.prepareStatement(checkOrderSql);

            checkStoreStatement.setString(1, storeName);
            checkOrderStatement.setString(1, orderID);
            checkOrderStatement.setString(2, storeName);

            ResultSet checkStoreResult = checkStoreStatement.executeQuery();
            ResultSet checkOrderResult = checkOrderStatement.executeQuery();

            if (!checkStoreResult.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }
            if (!checkOrderResult.next()) {
                System.out.println("ERROR:order_identifier_does_not_exist");
                return;
            }

            // Get the drone ID
            String getDroneSql = "SELECT drone_id FROM Orders WHERE id = ? AND store_name = ?";
            PreparedStatement getDroneStatement = connection.prepareStatement(getDroneSql);
            getDroneStatement.setString(1, orderID);
            getDroneStatement.setString(2, storeName);

            ResultSet getDroneResult = getDroneStatement.executeQuery();
            getDroneResult.next();
            String droneID = getDroneResult.getString("drone_id");

            // Calculate the total weight of the order
            String totalWeightSql = "SELECT SUM(item_weight * quantity) AS total_weight " +
                    "FROM Order_Items, Items " +
                    "WHERE order_id = ? AND Order_Items.store_name = ? " +
                    "AND Order_Items.item_name = Items.item_name " +
                    "AND Order_Items.store_name = Items.store_name";
            PreparedStatement totalWeightStatement = connection.prepareStatement(totalWeightSql);
            totalWeightStatement.setString(1, orderID);
            totalWeightStatement.setString(2, storeName);

            ResultSet totalWeightResult = totalWeightStatement.executeQuery();
            totalWeightResult.next();
            int totalWeight = totalWeightResult.getInt("total_weight");

            // Remove the order items from the Order_Items table
            String deleteOrderItemsSql = "DELETE FROM Order_Items WHERE order_id = ? AND store_name = ?";
            PreparedStatement deleteOrderItemsStatement = connection.prepareStatement(deleteOrderItemsSql);
            deleteOrderItemsStatement.setString(1, orderID);
            deleteOrderItemsStatement.setString(2, storeName);
            deleteOrderItemsStatement.executeUpdate();

            // Remove the order from the Orders table
            String deleteOrderSql = "DELETE FROM Orders WHERE id = ? AND store_name = ?";
            PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderSql);
            deleteOrderStatement.setString(1, orderID);
            deleteOrderStatement.setString(2, storeName);
            deleteOrderStatement.executeUpdate();

            // Update drone's number of orders and remaining capacity
            String updateDroneSql = "UPDATE Drones SET orders = orders - 1, remaining_capacity = remaining_capacity + ? WHERE id = ? AND store_name = ?";
            PreparedStatement updateDroneStatement = connection.prepareStatement(updateDroneSql);
            updateDroneStatement.setInt(1, totalWeight);
            updateDroneStatement.setString(2, droneID);
            updateDroneStatement.setString(3, storeName);
            updateDroneStatement.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //[23]review:

    //[24] Transfer order:
    public void transferOrder(String storeName, String orderID, String droneID) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            String checkStoreExistsQuery = "SELECT name FROM Stores WHERE name = ?";
            PreparedStatement checkStoreExistsStmt = connection.prepareStatement(checkStoreExistsQuery);

            String checkOrderExistsQuery = "SELECT id, store_name FROM Orders WHERE id = ? AND store_name = ?";
            PreparedStatement checkOrderExistsStmt = connection.prepareStatement(checkOrderExistsQuery);

            String checkDroneExistsQuery = "SELECT id, store_name FROM Drones WHERE id = ? AND store_name = ?";
            PreparedStatement checkDroneExistsStmt = connection.prepareStatement(checkDroneExistsQuery);

            String getOrderWeightQuery = "SELECT SUM(i.item_weight * oi.quantity) AS total_weight FROM Order_Items AS oi INNER JOIN Items AS i ON oi.item_name = i.item_name AND oi.store_name = i.store_name WHERE oi.order_id = ? AND oi.store_name = ?";
            PreparedStatement weightStmt = connection.prepareStatement(getOrderWeightQuery);

            String getDroneRemainCapQuery = "SELECT remaining_capacity FROM Drones WHERE id = ? AND store_name = ?";
            PreparedStatement getDroneRemainCapStmt = connection.prepareStatement(getDroneRemainCapQuery);

            String transferOrderQuery = "UPDATE Orders SET drone_id = ? WHERE id = ? AND store_name = ?";
            PreparedStatement transferOrderStmt = connection.prepareStatement(transferOrderQuery);

//
//            String updateOldDroneQuery = "UPDATE Drones SET remaining_capacity = remaining_capacity + ?, orders = orders - 1 WHERE id = (SELECT drone_id FROM Orders WHERE id = ? AND store_name = ?) AND store_name = ?";
//            PreparedStatement updateOldDroneStmt = connection.prepareStatement(updateOldDroneQuery);

            String updateOldDroneQuery = "UPDATE Drones SET remaining_capacity = remaining_capacity + ?, orders = orders - 1 WHERE id = ? AND store_name = ?";
            PreparedStatement updateOldDroneStmt = connection.prepareStatement(updateOldDroneQuery);

            String updateNewDroneQuery = "UPDATE Drones SET remaining_capacity = remaining_capacity - ?, orders = orders + 1 WHERE id = ? AND store_name = ?";
            PreparedStatement updateNewDroneStmt = connection.prepareStatement(updateNewDroneQuery);

            // Check if the store exists
            checkStoreExistsStmt.setString(1, storeName);
            ResultSet storeExistsRS = checkStoreExistsStmt.executeQuery();
            if (!storeExistsRS.next()) {
                System.out.println("ERROR:store_identifier_does_not_exist");
                return;
            }

            // Check if the order exists
            checkOrderExistsStmt.setString(1, orderID);
            checkOrderExistsStmt.setString(2, storeName);
            ResultSet orderExistsRS = checkOrderExistsStmt.executeQuery();
            if (!orderExistsRS.next()) {
                System.out.println("ERROR:order_identifier_does_not_exist");
                return;
            }

            // Check if the drone exists
            checkDroneExistsStmt.setString(1, droneID);
            checkDroneExistsStmt.setString(2, storeName);
            ResultSet droneExistsRS = checkDroneExistsStmt.executeQuery();
            if (!droneExistsRS.next()) {
                System.out.println("ERROR:drone_identifier_does_not_exist");
                return;
            }

            // Get order total weight
            weightStmt.setString(1, orderID);
            weightStmt.setString(2, storeName);
            ResultSet weightRS = weightStmt.executeQuery();
            int orderTotalWeight = 0;
            if (weightRS.next()) {
                orderTotalWeight = weightRS.getInt("total_weight");
            }

            // Check if the new drone has enough capacity
            getDroneRemainCapStmt.setString(1, droneID);
            getDroneRemainCapStmt.setString(2, storeName);
            ResultSet droneRemainCapRS = getDroneRemainCapStmt.executeQuery();
            if (droneRemainCapRS.next()) {
                int droneRemainCap = droneRemainCapRS.getInt("remaining_capacity");
                //System.out.println(orderTotalWeight);
                if (droneRemainCap < orderTotalWeight) {
                    System.out.println("ERROR:new_drone_does_not_have_enough_capacity");
                    return;
                }
            }

            // Get the current drone ID for the order
            String getCurrentDroneIDQuery = "SELECT drone_id FROM Orders WHERE id = ? AND store_name = ?";
            PreparedStatement getCurrentDroneIDStmt = connection.prepareStatement(getCurrentDroneIDQuery);
            getCurrentDroneIDStmt.setString(1, orderID);
            getCurrentDroneIDStmt.setString(2, storeName);
            ResultSet currentDroneIDRS = getCurrentDroneIDStmt.executeQuery();
            String currentDroneID = "";
            if (currentDroneIDRS.next()) {
                currentDroneID = currentDroneIDRS.getString("drone_id");
            }

            // Check if the new drone is the same as the current drone
            if (currentDroneID.equals(droneID)) {
                System.out.println("OK:new_drone_is_current_drone_no_change");
                return;
            }

            // Transfer order to the new drone
            transferOrderStmt.setString(1, droneID);
            transferOrderStmt.setString(2, orderID);
            transferOrderStmt.setString(3, storeName);
            transferOrderStmt.executeUpdate();

            // Update old drone's remaining_capacity and orders
            updateOldDroneStmt.setInt(1, orderTotalWeight);
            updateOldDroneStmt.setString(2, currentDroneID);
            updateOldDroneStmt.setString(3, storeName);
            //updateOldDroneStmt.setString(4, storeName);
            updateOldDroneStmt.executeUpdate();

            // Update new drone's remaining_capacity and orders
            updateNewDroneStmt.setInt(1, orderTotalWeight);
            updateNewDroneStmt.setString(2, droneID);
            updateNewDroneStmt.setString(3, storeName);
            updateNewDroneStmt.executeUpdate();

            //number of transferred order:
            String updateStoreTransfersQuery = "UPDATE Stores SET num_transfers = num_transfers + 1 WHERE name = ?";
            PreparedStatement updateStoreTransfersStmt = connection.prepareStatement(updateStoreTransfersQuery);
            // Update the store's number of transfers
            updateStoreTransfersStmt.setString(1, storeName);
            updateStoreTransfersStmt.executeUpdate();

            System.out.println("OK:change_completed");

            // Close the database connection
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[25] display efficiency:
    public void displayEfficiency() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT name, num_purchases, num_overloads, num_transfers FROM Stores";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int purchases = resultSet.getInt("num_purchases");
                int overloads = resultSet.getInt("num_overloads");
                int transfers = resultSet.getInt("num_transfers");

                System.out.println("name:" + name + ",purchases:" + purchases + ",overloads:" + overloads + ",transfers:" + transfers);
            }

            System.out.println("OK:display_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //[26] stop
    //[27] comments

    public void checkPasswd(String UIN, String passwd, String role) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the UIN exists
            String checkUINSql = "SELECT COUNT(*) FROM Roles WHERE UIN = ?";
            PreparedStatement checkUINStatement = connection.prepareStatement(checkUINSql);
            checkUINStatement.setString(1, UIN);
            ResultSet checkUINResult = checkUINStatement.executeQuery();
            checkUINResult.next();
            int count = checkUINResult.getInt(1);

            if (count == 0) {
                System.out.println("ERROR:UIN_does_not_exist");
            } else {
                // Check if the role is correct
                String checkRoleSql = "SELECT roleName, passwd, errorAttempts FROM Roles WHERE UIN = ?";
                PreparedStatement checkRoleStatement = connection.prepareStatement(checkRoleSql);
                checkRoleStatement.setString(1, UIN);
                ResultSet checkRoleResult = checkRoleStatement.executeQuery();
                if (checkRoleResult.next()) {
                    String storedRoleName = checkRoleResult.getString("roleName");
                    String storedPasswd = checkRoleResult.getString("passwd");
                    int errorAttempts = checkRoleResult.getInt("errorAttempts");

                    if (!role.equals(storedRoleName)) {
                        System.out.printf("ERROR: this UIN does not have access to the role %s%n", role);
                    } else if (!passwd.equals(storedPasswd)) {
                        errorAttempts++;

                        // Update errorAttempts for the specific UIN in the Roles table
                        String updateErrorAttemptsSql = "UPDATE Roles SET errorAttempts = ? WHERE UIN = ?";
                        PreparedStatement updateErrorAttemptsStatement = connection.prepareStatement(updateErrorAttemptsSql);
                        updateErrorAttemptsStatement.setInt(1, errorAttempts);
                        updateErrorAttemptsStatement.setString(2, UIN);
                        updateErrorAttemptsStatement.executeUpdate();

                        if (errorAttempts >= 3) {
                            System.out.println("ERROR:maximum attempts reached, please reset your password.");
                        } else {
                            int remainingAttempts = 3 - errorAttempts;
                            System.out.printf("ERROR:password is wrong, you have %d more attempts%n", remainingAttempts);
                        }
                } else {
                    // Print the success message only when UIN exists, role is correct, and password is correct
                    System.out.println("OK:login_completed");
                }
                }
            }

            //System.out.println("OK:login_completed");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetPasswd(String UIN, String newPasswd, String phone_number) {
        try {
            // Create a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Check if the UIN exists and get the roleName
            String checkUINSql = "SELECT roleName FROM Roles WHERE UIN = ?";
            PreparedStatement checkUINStatement = connection.prepareStatement(checkUINSql);
            checkUINStatement.setString(1, UIN);
            ResultSet checkUINResult = checkUINStatement.executeQuery();

            if (!checkUINResult.next()) {
                System.out.println("ERROR:UIN_does_not_exist");
            } else {
                String roleName = checkUINResult.getString("roleName");
                boolean correctPhone = false;

                switch (roleName) {
                    case "admin":
                        String checkAdminPhoneSql = "SELECT phone_number FROM Stores WHERE UIN = ?";
                        PreparedStatement checkAdminPhoneStatement = connection.prepareStatement(checkAdminPhoneSql);
                        checkAdminPhoneStatement.setString(1, UIN);
                        ResultSet checkAdminPhoneResult = checkAdminPhoneStatement.executeQuery();
                        if (checkAdminPhoneResult.next() && phone_number.equals(checkAdminPhoneResult.getString("phone_number"))) {
                            correctPhone = true;
                        }
                        break;

                    case "customer":
                        String checkCustomerPhoneSql = "SELECT phone_number FROM Customers WHERE UIN = ?";
                        PreparedStatement checkCustomerPhoneStatement = connection.prepareStatement(checkCustomerPhoneSql);
                        checkCustomerPhoneStatement.setString(1, UIN);
                        ResultSet checkCustomerPhoneResult = checkCustomerPhoneStatement.executeQuery();
                        if (checkCustomerPhoneResult.next() && phone_number.equals(checkCustomerPhoneResult.getString("phone_number"))) {
                            correctPhone = true;
                        }
                        break;

                    case "pilot":
                        String checkPilotPhoneSql = "SELECT phone_number FROM Pilots WHERE UIN = ?";
                        PreparedStatement checkPilotPhoneStatement = connection.prepareStatement(checkPilotPhoneSql);
                        checkPilotPhoneStatement.setString(1, UIN);
                        ResultSet checkPilotPhoneResult = checkPilotPhoneStatement.executeQuery();
                        if (checkPilotPhoneResult.next() && phone_number.equals(checkPilotPhoneResult.getString("phone_number"))) {
                            correctPhone = true;
                        }
                        break;
                }

                if (correctPhone) {
                    // Update the password and reset errorAttempts in the Roles table
                    String updateRolesPasswordSql = "UPDATE Roles SET passwd = ?, errorAttempts = 0 WHERE UIN = ?";
                    PreparedStatement updateRolesPasswordStatement = connection.prepareStatement(updateRolesPasswordSql);
                    updateRolesPasswordStatement.setString(1, newPasswd);
                    updateRolesPasswordStatement.setString(2, UIN);
                    updateRolesPasswordStatement.executeUpdate();


                    // Update the password in the specific table (Stores, Customers, or Pilots) based on roleName
                    String tableName;
                    if ("admin".equals(roleName)) {
                        tableName = "Stores";
                    } else if ("pilot".equals(roleName)) {
                        tableName = "Pilots";
                    } else if ("customer".equals(roleName)) {
                        tableName = "Customers";
                    } else {
                        System.out.println("ERROR: Invalid role.");
                        return;
                    }

                    String updatePasswordSql = "UPDATE " + tableName + " SET passwd = ? WHERE UIN = ?";
                    PreparedStatement updatePasswordStatement = connection.prepareStatement(updatePasswordSql);
                    updatePasswordStatement.setString(1, newPasswd);
                    updatePasswordStatement.setString(2, UIN);
                    updatePasswordStatement.executeUpdate();


                    System.out.println("OK:password_reset_completed");
                } else {
                    System.out.println("ERROR:incorrect_use_information");
                }
            }

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void commandLoop() {
            Scanner commandLineInput = new Scanner(System.in);
            String wholeInputLine;
            String[] tokens;
            final String DELIMITER = ",";

            //password role UIN
            String UIN = "000";
            String passwd = "000";
            String role = "admin";
            String usr = "kroger";

            while (true) {
            try {

                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                if (tokens[0].equals("switch_role")) {
                    //eg, switch_role,UIN,passwd,role
                    UIN = tokens[1];
                    passwd = tokens[2];
                    role = tokens[3];
                    usr = tokens[4];
                    checkPasswd(UIN, passwd,role);
                    //System.out.println("UIN: " + UIN + ", role: " + role + ", user: " + usr);


                } else if (tokens[0].equals("reset_passwd")) {
                    //eg, reset_passwd,UIN,newPasswd,phone_number
                    resetPasswd(tokens[1],tokens[2],tokens[3] );

                } else if (tokens[0].equals("make_store")) {
                    //eg, make_store,store,revenue,lat,lng,UIN,passwd,role,phone_number
                    if (!role.equals("admin")) {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.println("store: " + tokens[1] + ", revenue: " + tokens[2]);
                        //Store newStore = new Store(tokens[1], Integer.parseInt(tokens[2]));
                        makeStore(tokens[1], Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]) ,tokens[5],tokens[6],tokens[7],tokens[8]);
                    }

                } else if (tokens[0].equals("display_stores")) {
                    if (!role.equals("admin") && !role.equals("customer")) {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.println("no parameters needed");
                        displayStores();
                    }

                } else if (tokens[0].equals("sell_item")) {
                    if (!role.equals("admin")  ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1] + ", item: " + tokens[2] + ", weight: " + tokens[3]);
                        sellItem(tokens[2], tokens[1], Integer.parseInt(tokens[3]));
                    }

                } else if (tokens[0].equals("display_items")) {
                    if (!role.equals("admin")  && !role.equals("customer") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1]);
                        displayItems(tokens[1]);
                    }

                } else if (tokens[0].equals("make_pilot")) {
                    if (!role.equals("admin") && !role.equals("pilot")) {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        // System.out.print("account: " + tokens[1] + ", first_name: " + tokens[2] + ", last_name: " + tokens[3]);
                        // System.out.println(", phone: " + tokens[4] + ", tax: " + tokens[5] + ", license: " + tokens[6] + ", experience: " + tokens[7]);8~UIN;9~passwd;10~role
                        makePilot(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], Integer.parseInt(tokens[7]), tokens[8], tokens[9], tokens[10]);
                    }

                } else if (tokens[0].equals("display_pilots")) {
                    if (!role.equals("admin") && !role.equals("pilot")) {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.println("no parameters needed");
                        displayPilots();
                    }

                } else if (tokens[0].equals("make_drone")) {
                    if (!role.equals("admin")  && !role.equals("pilot") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1] + ", drone: " + tokens[2] + ", capacity: " + tokens[3] + ", fuel: " + tokens[4]);
                        makeDrone(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                    }

                } else if (tokens[0].equals("display_drones")) {
                    if (!role.equals("admin")  && !role.equals("pilot") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1]);
                        displayDrone(tokens[1]);
                    }

                } else if (tokens[0].equals("fly_drone")) {
                    if (!role.equals("admin")   && !role.equals("pilot")){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1] + ", drone: " + tokens[2] + ", pilot: " + tokens[3]);
                        flyDrone(tokens[1], tokens[2], tokens[3]);
                    }

                } else if (tokens[0].equals("make_customer")) {
                    if (!role.equals("admin")&& !role.equals("customer"))  {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.print("account: " + tokens[1] + ", first_name: " + tokens[2] + ", last_name: " + tokens[3]);
                        //System.out.println(", phone: " + tokens[4] + ", rating: " + tokens[5] + ", credit: " + tokens[6]);7~UNI;8~passwd;9~role
                        makeCustomer(tokens[1], tokens[2], tokens[3], tokens[4], Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), tokens[7], tokens[8], tokens[9]);
                    }

                } else if (tokens[0].equals("display_customers")) {
                    if (!role.equals("admin") && !role.equals("customer"))  {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.println("no parameters needed");
                        displayCustomers();
                    }

                } else if (tokens[0].equals("start_order")) {
                    if (!role.equals("admin")  && !role.equals("customer") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])&&!usr.equals(tokens[4])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1] + ", order: " + tokens[2] + ", drone: " + tokens[3] + ", customer: " + tokens[4]);
                        startOrder(tokens[1], tokens[2], tokens[3], tokens[4], Double.parseDouble(tokens[5]), Double.parseDouble(tokens[6]));
                    }

                } else if (tokens[0].equals("display_orders")) {
                    if (!role.equals("admin")  && !role.equals("customer") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        //System.out.println("store: " + tokens[1]);
                        displayOrders(tokens[1]);
                    }

                } else if (tokens[0].equals("request_item")) {
                    if (!role.equals("admin")  && !role.equals("customer") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        /* System.out.println("store: " + tokens[1] + ", order: " + tokens[2] + ", item: " + tokens[3] + ", quantity: " + tokens[4] + ", unit_price: " + tokens[5]); */
                        requestItem(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
                    }

                } else if (tokens[0].equals("purchase_order")) {
                    if (!role.equals("admin")  && !role.equals("customer") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        purchaseOrder(tokens[1], tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]));
                    }

                } else if (tokens[0].equals("cancel_order")) {
                    if (!role.equals("admin")  && !role.equals("customer") ){
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_role");
                    } else if(!usr.equals(tokens[1])) {
                        System.out.println("Error:you_do_not_have_permission_to_execute_this_command_please_switch_user");
                    } else {
                        cancelOrder(tokens[1], tokens[2]);
                    }

                } else if (tokens[0].equals("transfer_order")) {
                    if (!role.equals("admin") && !role.equals("pilot"))  {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.println("store: " + tokens[1] + ", order: " + tokens[2] + ", new_drone: " + tokens[3]);
                        transferOrder(tokens[1], tokens[2], tokens[3]);
                    }

                } else if (tokens[0].equals("display_efficiency")) {
                    if (!role.equals("admin") && !role.equals("pilot"))  {
                        System.out.println("Error: You do not have permission to execute this command, please switch role.");
                    } else {
                        //System.out.println("no parameters needed");
                        displayEfficiency();
                    }

                } else if (tokens[0].equals("stop")) {
                    System.out.println("stop acknowledged");
                    break;

                } else if (wholeInputLine.startsWith("//")){

                }

                else {
                    System.out.println("command " + tokens[0] + " NOT acknowledged");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }
}
