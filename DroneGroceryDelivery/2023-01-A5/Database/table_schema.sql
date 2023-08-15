CREATE DATABASE IF NOT EXISTS delivery_service_db;
USE delivery_service_db;

DROP TABLE IF EXISTS `Order_Items`;
DROP TABLE IF EXISTS `Items`;
DROP TABLE IF EXISTS `Drone_Pilots`;
DROP TABLE IF EXISTS `Pilots`;
DROP TABLE IF EXISTS `Orders`;
DROP TABLE IF EXISTS `Drones`;
DROP TABLE IF EXISTS `Customers`;
DROP TABLE IF EXISTS `Stores`;
DROP TABLE IF EXISTS `Roles`;

CREATE TABLE Roles (
                       UIN VARCHAR(255) NOT NULL,
                       roleName VARCHAR(255) NOT NULL,
                       passwd VARCHAR(255) NOT NULL,
                       errorAttempts INT NOT NULL DEFAULT 0,
                       usr VARCHAR(255) NOT NULL DEFAULT 'kroger',
                       PRIMARY KEY(UIN)
);


-- store table:
CREATE TABLE Stores (
        name VARCHAR(255) NOT NULL,
        UIN VARCHAR(255) NOT NULL,
        passwd VARCHAR(255) NOT NULL,
        roleName VARCHAR(255) NOT NULL,
        phone_number VARCHAR(255) NOT NULL,
        revenue INT NOT NULL,
        num_purchases INT NOT NULL DEFAULT 0,
        num_overloads INT NOT NULL DEFAULT 0,
        num_transfers INT NOT NULL DEFAULT 0,
        latitude DECIMAL NOT NULL,
        longitude DECIMAL NOT NULL,
        PRIMARY KEY(name)
);



CREATE TRIGGER after_stores_insert
    AFTER INSERT
    ON Stores FOR EACH ROW
BEGIN
    INSERT INTO Roles (UIN, passwd, roleName,usr) VALUES (NEW.UIN, NEW.passwd, NEW.roleName, NEW.name);
END;

-- item table:
CREATE TABLE Items (
        item_name VARCHAR(255) NOT NULL,
        item_weight INT NOT NULL,
        store_name VARCHAR(255) NOT NULL,
        PRIMARY KEY(item_name, store_name),
        FOREIGN KEY(store_name) REFERENCES Stores(name)
);

CREATE TABLE Drones (
        id VARCHAR(255) NOT NULL,
        store_name VARCHAR(255) NOT NULL,
        total_capacity INT NOT NULL,
        remaining_capacity INT NOT NULL,
        orders INT NOT NULL,
        current_battery INT NOT NULL,
        max_battery INT NOT NULL,
        PRIMARY KEY (id, store_name),
        FOREIGN KEY (store_name) REFERENCES Stores(name)
);


CREATE TABLE Pilots (
        account VARCHAR(255) NOT NULL,
        UIN VARCHAR(255) NOT NULL,
        passwd VARCHAR(255) NOT NULL,
        roleName VARCHAR(255) NOT NULL,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        phone_number VARCHAR(255) NOT NULL,
        tax_id VARCHAR(255) NOT NULL,
        license_id VARCHAR(255) NOT NULL,
        level INT NOT NULL,
        drone_id VARCHAR(255),
        store_name VARCHAR(255),
        PRIMARY KEY (account),
        FOREIGN KEY (drone_id, store_name) REFERENCES Drones(id, store_name)
);


CREATE TRIGGER after_pilots_insert
    AFTER INSERT
    ON Pilots FOR EACH ROW
BEGIN
    INSERT INTO Roles (UIN, passwd, roleName, usr) VALUES (NEW.UIN, NEW.passwd, NEW.roleName, NEW.account);
END;


-- drone pilot table:
CREATE TABLE Drone_Pilots (
        drone_id VARCHAR(255) NOT NULL,
        store_name VARCHAR(255) NOT NULL,
        pilot_account VARCHAR(255) NOT NULL,
        PRIMARY KEY (drone_id, store_name),
        FOREIGN KEY (drone_id, store_name) REFERENCES Drones(id, store_name),
        FOREIGN KEY (pilot_account) REFERENCES Pilots(account)
);

-- customer table:
CREATE TABLE Customers (
        account VARCHAR(255) NOT NULL,
        UIN VARCHAR(255) NOT NULL,
        passwd VARCHAR(255) NOT NULL,
        roleName VARCHAR(255) NOT NULL,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        phone_number VARCHAR(255) NOT NULL,
        rating INT NOT NULL,
        credit INT NOT NULL,
        hold_credit INT NOT NULL,
        PRIMARY KEY (account)
);

CREATE TRIGGER after_customers_insert
    AFTER INSERT
    ON Customers FOR EACH ROW
BEGIN
    INSERT INTO Roles (UIN, passwd, roleName, usr) VALUES (NEW.UIN, NEW.passwd, NEW.roleName, NEW.account);
END;

-- Order table:
CREATE TABLE Orders (
        id VARCHAR(255) NOT NULL,
        store_name VARCHAR(255) NOT NULL,
        drone_id VARCHAR(255) NOT NULL,
        customer_account VARCHAR(255) NOT NULL,
        total_price INT NOT NULL DEFAULT 0,
        address_lat DOUBLE NOT NULL,
        address_lng DOUBLE NOT NULL,
        PRIMARY KEY (id, store_name),
        FOREIGN KEY (store_name) REFERENCES Stores(name),
        FOREIGN KEY (drone_id, store_name) REFERENCES Drones(id, store_name),
        FOREIGN KEY (customer_account) REFERENCES Customers(account)
);

-- Order Items Method:
CREATE TABLE Order_Items (
        order_id VARCHAR(255) NOT NULL,
        store_name VARCHAR(255) NOT NULL,
        item_name VARCHAR(255) NOT NULL,
        unit_price INT NOT NULL,
        quantity INT NOT NULL,
        PRIMARY KEY (order_id, store_name, item_name),
        FOREIGN KEY (order_id, store_name) REFERENCES Orders(id, store_name),
        FOREIGN KEY (item_name, store_name) REFERENCES Items(item_name, store_name)
);

