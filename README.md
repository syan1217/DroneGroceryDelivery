Group 50: A5 Readme for CS6310 Spring 2023

# Add jdk and mysql connector

![add jdk and mysql connector](./img/add jdk and mysqlConnector.png)
![add jdk](./img/add jdk.png)
![add mysql connector 1](./img/add mysqlConnector-1.png)
![add mysql connector 2](./img/add mysqlConnector-2.png)
![jdk and mysqlConnector done](./img/jdk and mysqlConnector done.png)

# Pull mysql in docker: (Do it in terminal)
```
docker pull mysql 
```
![docker pull mysql](./img/docker pull mysql.png)

# Create and run mysql container in Docker: (Do it in terminal)
```
docker run --name delivery_service_db -e MYSQL_ROOT_PASSWORD="123456" -p 3307:3306 -d mysql
```
![create mysql container](./img/create mysql container.png)
![create mysql container done](./img/create mysql container done.png)


# Connect to a localhost, run mysql to create table and view data

### Step 1: View > Tool Windows > Database:

![openDataBase](./img/openDataBase.png)

### Step 2: Set up connection and add a new MySQL data source

![AddmySQLResource](./img/AddmySQLResource.png)

### Step 3: Fill in the connection details (Host: localhost, Port:3307, User:root, Password:123456)

![FillConnectiondetails](./img/FillConnectiondetails.png)

### Step 4: run table schema.sql under Database Directory:

![selectAllandRightClickandExcute](./img/selectAllandRightClickandExcute.png)
![localhostconsole](./img/localhostconsole.png)
![tableRunDone](./img/tableRunDone.png)
![AllTables](./img/AllTables.png)

# Run test case from the terminal:

### Step 1: Built up the main and execute:

![MainBuiltUpandRun](./img/MainBuiltUpandRun.png)
![BuiltUpSucccessfully](./img/BuiltUpSucccessfully.png)

### Step 2: Run the test case (copy all and paste into the terminal)

![TerminalResults](./img/TerminalResults.png)

### Step 3: Compare the terminal result with the desired result.

```
Go the website (difference checker): https://www.diffchecker.com/text-compare/ 
```

![differenceChecker](./img/differenceChecker.png)

```
Paste the termianl result to left text window and the desired result to the right text window and test. 
```
![TerminalResults](./img/TestResultDiff.png)

### Goals for completion
```
1) Completed the functional modifications – Solar-Powered Drones
2) Completed the non-functional modifications - Performance
3) Completed the group’s choice of modifications - Authorization/Authentication
```




# Test Case Demonstrations (commands_70.txt)

## Part I Solar Power Drone

### // create multiple stores
```
make_store,kroger,33000,3,6,90100,3j8fr7gu89j,admin,800-888-8888
make_store,kroger,37000,10,20,90101,89rw9h59ng,admin,800-888-8889
make_store,publix,33000,10,20,90102,84g48u8g09,admin,800-888-8890
```
Expected outcome: 

Store "kroger" and "publix" are added. If store_identifier_already_exists, store cannot be added.
```
> make_store,kroger,33000,3,6,90100,3j8fr7gu89j,admin,800-888-8888
OK:change_completed
> make_store,kroger,37000,10,20,90101,89rw9h59ng,admin,800-888-8889
ERROR:store_identifier_already_exists
> make_store,publix,33000,10,20,90102,84g48u8g09,admin,800-888-8890
OK:change_completed
> display_stores
name:kroger,revenue:33000,addressLat:3.0,addressLng:6.0
name:publix,revenue:33000,addressLat:10.0,addressLng:20.0
OK:display_completed
```
### // create multiple items to be sold by stores
```
switch_role,90100,3j8fr7gu89j,admin,kroger
sell_item,kroger,pot_roast,5
sell_item,kroger,cheesecake,4
sell_item,kroger,cheesecake,3
display_items,kroger
switch_role,90102,84g48u8g09,admin,publix
sell_item,publix,cheesecake,8
display_items,publix
sell_item,whole_foods,antipasto,10
display_items,whole_foods
```
Expected outcome:

* Item "cheesecake" and "pot_roast" are added to store "kroger". If item_identifier_already_exists, cannot add that item.

* Item "cheesecake" is added to store "publix".

* Since store "whole_foods" does not exist in the system, we cannot run the corresponding "sell_item" or "display_items" command for "whole_foods".
```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> sell_item,kroger,pot_roast,5
OK:change_completed
> sell_item,kroger,cheesecake,4
OK:change_completed
> sell_item,kroger,cheesecake,3
ERROR:item_identifier_already_exists
> display_items,kroger
cheesecake,4
pot_roast,5
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> sell_item,publix,cheesecake,8
OK:change_completed
> display_items,publix
cheesecake,8
OK:display_completed
> sell_item,whole_foods,antipasto,10
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
> display_items,whole_foods
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
```
### // create multiple pilots to control the drones
```
make_pilot,ffig8,Finneas,Fig,888-888-8888,890-12-3456,panam_10,33,90301,0fu4wj8g8q0,pilot
make_pilot,ggrape17,Gillian,Grape,999-999-9999,234-56-7890,twa_21,31,90302,fq84j9tu0u0,pilot
make_pilot,ffig8,Frances,Faro,777-777-7777,678-90-1234,eastern_6,36,90303,40utj08qjg,pilot
make_pilot,kkiwi23,Kiara,Kiwi,555-555-5555,890-12-3456,panam_10,28,90304,f4q308j080,pilot
display_pilots
```
Expected outcome:

Pilot "Finneas_Fig" and "Gillian_Grape" are added. If pilot_identifier_already_exists/ pilot_license_already_exists, cannot add that pilot.

```
> make_pilot,ffig8,Finneas,Fig,888-888-8888,890-12-3456,panam_10,33,90301,0fu4wj8g8q0,pilot
OK:change_completed
> make_pilot,ggrape17,Gillian,Grape,999-999-9999,234-56-7890,twa_21,31,90302,fq84j9tu0u0,pilot
OK:change_completed
> make_pilot,ffig8,Frances,Faro,777-777-7777,678-90-1234,eastern_6,36,90303,40utj08qjg,pilot
ERROR:pilot_identifier_already_exists
> make_pilot,kkiwi23,Kiara,Kiwi,555-555-5555,890-12-3456,panam_10,28,90304,f4q308j080,pilot
ERROR:pilot_license_already_exists
> display_pilots
name:Finneas_Fig,phone:888-888-8888,taxID:890-12-3456,licenseID:panam_10,experience:33
name:Gillian_Grape,phone:999-999-9999,taxID:234-56-7890,licenseID:twa_21,experience:31
OK:display_completed
```
### // create multiple drones to deliver the orders
```
switch_role,90100,3j8fr7gu89j,admin,kroger
make_drone,kroger,1,40
make_drone,kroger,1,100
make_drone,kroger,2,20
display_drones,kroger
switch_role,90102,84g48u8g09,admin,publix
make_drone,publix,1,40
display_drones,publix
make_drone,whole_foods,1,40
display_drones,whole_foods
switch_role,90100,3j8fr7gu89j,admin,kroger
fly_drone,kroger,1,ffig8
display_drones,kroger
switch_role,90102,84g48u8g09,admin,publix
fly_drone,publix,2,ggrape17
fly_drone,publix,1,hhoneydew20
fly_drone,publix,1,ggrape17
display_drones,publix
fly_drone,whole_foods,1,ggrape17
display_drones,whole_foods
```
Expected outcome:

* 2 drones are added to store "kroger". If drone_identifier_already_exists, drone cannot be added to that store.
* 1 drone is added to store "publix".
* Since store "whole_foods" does not exist in the system, we cannot add drone to store "whole_foods".
```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> make_drone,kroger,1,40
OK:change_completed
> make_drone,kroger,1,100
ERROR:drone_identifier_already_exists
> make_drone,kroger,2,20
OK:change_completed
> display_drones,kroger
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:50
droneID:2,total_cap:20,num_orders:0,remaining_cap:20,current_battery:50
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> make_drone,publix,1,40
OK:change_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:50
OK:display_completed
> make_drone,whole_foods,1,40
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
> display_drones,whole_foods
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
```
### // assign pilots to drones
```
switch_role,90100,3j8fr7gu89j,admin,kroger
fly_drone,kroger,1,ffig8
display_drones,kroger
switch_role,90102,84g48u8g09,admin,publix
fly_drone,publix,2,ggrape17
fly_drone,publix,1,hhoneydew20
fly_drone,publix,1,ggrape17
display_drones,publix
fly_drone,whole_foods,1,ggrape17
display_drones,whole_foods
```
Expected outcome:

* kroger: pilot "Finneas_Fig" is assigned to drone 1.
* publix: pilot "Gillian_Grape" is assigned to drone 1. If drone_identifier_does_not_exist/ pilot_identifier_does_not_exist, pilot cannot be assigned to that drone.
* Since store "whole_foods" does not exist in the system, we cannot assign pilot to drone for store "whole_foods".
```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> fly_drone,kroger,1,ffig8
OK:change_completed
> display_drones,kroger
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:50,flown_by:Finneas_Fig
droneID:2,total_cap:20,num_orders:0,remaining_cap:20,current_battery:50
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> fly_drone,publix,2,ggrape17
ERROR:drone_identifier_does_not_exist
> fly_drone,publix,1,hhoneydew20
ERROR:pilot_identifier_does_not_exist
> fly_drone,publix,1,ggrape17
OK:change_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:50,flown_by:Gillian_Grape
OK:display_completed
> fly_drone,whole_foods,1,ggrape17
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
> display_drones,whole_foods
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
```
### // create multiple customers to purchase items
```
make_customer,aapple2,Alana,Apple,222-222-2222,4,100,90500,03j408q3tj,customer
make_customer,aapple2,Ariana,Asparagus,333-333-3333,5,150,90501,f029j0g0gj8,customer
make_customer,ccherry4,Carlos,Cherry,444-444-4444,5,300,90502,3g08gq4qg5,customer
display_customers
```
Expected outcome:

Customer "Alana_Apple" and "Carlos_Cherry" are added. If customer_identifier_already_exists, customer cannot be added. 

```
> make_customer,aapple2,Alana,Apple,222-222-2222,4,100,90500,03j408q3tj,customer
OK:change_completed
> make_customer,aapple2,Ariana,Asparagus,333-333-3333,5,150,90501,f029j0g0gj8,customer
ERROR:customer_identifier_already_exists
> make_customer,ccherry4,Carlos,Cherry,444-444-4444,5,300,90502,3g08gq4qg5,customer
OK:change_completed
> display_customers
name:Alana_Apple,phone:222-222-2222,rating:4,credit:100
name:Carlos_Cherry,phone:444-444-4444,rating:5,credit:300
OK:display_completed
```
### // create multiple orders as requested by customers
```
switch_role,90100,3j8fr7gu89j,admin,kroger
start_order,kroger,purchaseA,1,aapple2,5,6
start_order,kroger,purchaseB,1,aapple2,10,12
start_order,kroger,purchaseA,1,aapple2,12,20
start_order,kroger,purchaseC,3,aapple2,20,30
start_order,kroger,purchaseC,1,bbanana3,50,50
start_order,kroger,purchaseD,2,ccherry4,50,50
display_orders,kroger
switch_role,90102,84g48u8g09,admin,publix
start_order,publix,purchaseA,1,ccherry4,20,25
display_orders,publix
start_order,whole_foods,purchaseC,1,aapple2,35,12
display_orders,whole_foods
```
Expected outcome:

* kroger: order "purchaseA", "purchaseB" and "purchaseD" are added; if order_identifier_already_exists/ drone_identifier_does_not_exist/ customer_identifier_does_not_exist, order cannot be added.
* publix: order "purchaseA" is added.
* Since store "whole_foods" does not exist in the system, we cannot add order for store "whole_foods".

```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> start_order,kroger,purchaseA,1,aapple2,5,6
OK:change_completed
> start_order,kroger,purchaseB,1,aapple2,10,12
OK:change_completed
> start_order,kroger,purchaseA,1,aapple2,12,20
ERROR:order_identifier_already_exists
> start_order,kroger,purchaseC,3,aapple2,20,30
ERROR:drone_identifier_does_not_exist
> start_order,kroger,purchaseC,1,bbanana3,50,50
ERROR:customer_identifier_does_not_exist
> start_order,kroger,purchaseD,2,ccherry4,50,50
OK:change_completed
> display_orders,kroger
orderID:purchaseA
orderID:purchaseB
orderID:purchaseD
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> start_order,publix,purchaseA,1,ccherry4,20,25
OK:change_completed
> display_orders,publix
orderID:purchaseA
OK:display_completed
> start_order,whole_foods,purchaseC,1,aapple2,35,12
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
> display_orders,whole_foods
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
```
### // add multiple items to the orders
```
switch_role,90100,3j8fr7gu89j,admin,kroger
request_item,kroger,purchaseA,pot_roast,3,10
request_item,kroger,purchaseB,pot_roast,4,5
request_item,kroger,purchaseE,cheesecake,1,10
request_item,kroger,purchaseA,truffle_risotto,1,10
request_item,kroger,purchaseA,pot_roast,1,10
request_item,kroger,purchaseA,cheesecake,1,90
request_item,kroger,purchaseA,cheesecake,10,5
request_item,kroger,purchaseD,cheesecake,1,10
display_orders,kroger
switch_role,90102,84g48u8g09,admin,publix
request_item,publix,purchaseA,cheesecake,3,10
display_orders,publix
request_item,whole_foods,purchaseA,cheesecake,1,10
```
Expected outcome:

* kroger: items are added to order "purchaseA", "purchaseB" and "purchaseD"; if order_identifier_does_not_exist/ item_identifier_does_not_exist/ item_already_ordered/ customer_cant_afford_new_item/ drone_cant_carry_new_item, item cannot be added to that order.
* publix: item "cheesecake" is added to "purchaseA" in publix.
* Since store "whole_foods" does not exist in the system, we cannot request item from store "whole_foods".

```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> request_item,kroger,purchaseA,pot_roast,3,10
OK:change_completed
> request_item,kroger,purchaseB,pot_roast,4,5
OK:change_completed
> request_item,kroger,purchaseE,cheesecake,1,10
ERROR:order_identifier_does_not_exist
> request_item,kroger,purchaseA,truffle_risotto,1,10
ERROR:item_identifier_does_not_exist
> request_item,kroger,purchaseA,pot_roast,1,10
ERROR:item_already_ordered
> request_item,kroger,purchaseA,cheesecake,1,90
ERROR:customer_cant_afford_new_item
> request_item,kroger,purchaseA,cheesecake,10,5
ERROR:drone_cant_carry_new_item
> request_item,kroger,purchaseD,cheesecake,1,10
OK:change_completed
> display_orders,kroger
orderID:purchaseA
item_name:pot_roast,total_quantity:3,total_cost:30,total_weight:15
orderID:purchaseB
item_name:pot_roast,total_quantity:4,total_cost:20,total_weight:20
orderID:purchaseD
item_name:cheesecake,total_quantity:1,total_cost:10,total_weight:4
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> request_item,publix,purchaseA,cheesecake,3,10
OK:change_completed
> display_orders,publix
orderID:purchaseA
item_name:cheesecake,total_quantity:3,total_cost:30,total_weight:24
OK:display_completed
> request_item,whole_foods,purchaseA,cheesecake,1,10
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
```
### // display the state of the simulation
```
display_customers
display_stores
display_pilots
switch_role,90100,3j8fr7gu89j,admin,kroger
display_orders,kroger
display_drones,kroger
switch_role,90102,84g48u8g09,admin,publix
display_orders,publix
display_drones,publix
```
Expected outcome:

A repetition of previous displayed outcome.
```
display_customers
name:Alana_Apple,phone:222-222-2222,rating:4,credit:100
name:Carlos_Cherry,phone:444-444-4444,rating:5,credit:300
OK:display_completed
> display_stores
name:kroger,revenue:33000,addressLat:3.0,addressLng:6.0
name:publix,revenue:33000,addressLat:10.0,addressLng:20.0
OK:display_completed
> display_pilots
name:Finneas_Fig,phone:888-888-8888,taxID:890-12-3456,licenseID:panam_10,experience:33
name:Gillian_Grape,phone:999-999-9999,taxID:234-56-7890,licenseID:twa_21,experience:31
OK:display_completed
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> display_orders,kroger
orderID:purchaseA
item_name:pot_roast,total_quantity:3,total_cost:30,total_weight:15
orderID:purchaseB
item_name:pot_roast,total_quantity:4,total_cost:20,total_weight:20
orderID:purchaseD
item_name:cheesecake,total_quantity:1,total_cost:10,total_weight:4
OK:display_completed
> display_drones,kroger
droneID:1,total_cap:40,num_orders:2,remaining_cap:5,current_battery:50,flown_by:Finneas_Fig
droneID:2,total_cap:20,num_orders:1,remaining_cap:16,current_battery:50
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> display_orders,publix
orderID:purchaseA
item_name:cheesecake,total_quantity:3,total_cost:30,total_weight:24
OK:display_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:1,remaining_cap:16,current_battery:50,flown_by:Gillian_Grape
OK:display_completed
```
### // deliver an order and display the updated state
```
switch_role,90100,3j8fr7gu89j,admin,kroger
purchase_order,kroger,purchaseA,100,4,5,0
purchase_order,kroger,purchaseA,100,6,17,0
display_orders,kroger
display_drones,kroger
switch_role,90102,84g48u8g09,admin,publix
display_orders,publix
display_drones,publix
display_customers
display_stores
display_pilots
```
Expected outcome:

* The purchase_order command takes in the storeName, orderID, remainBattery, batteryRecordTime, currentTime, and dateChange. 

* In the command "purchase_order,kroger,purchaseA,100,4,5,0", the drone was charged between 4AM to 5AM, thus the battery is not enough to support the weight and distance of "PurchaseA". Then we have error message drone_needs_charge.

* After order "PurchaseA" is purchased from kroger, it is removed from the "display_order". The "num_orders" of that drone is reduced by 1. When "display_stores", the kroger revenue increased 30. When "display_pilots", pilot "Finneas_Fig"'s experience increased from 33 to 34.

```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> purchase_order,kroger,purchaseA,100,4,5,0
ERROR:drone_needs_charge
> purchase_order,kroger,purchaseA,100,6,17,0
OK:change_completed
> display_orders,kroger
orderID:purchaseB
item_name:pot_roast,total_quantity:4,total_cost:20,total_weight:20
orderID:purchaseD
item_name:cheesecake,total_quantity:1,total_cost:10,total_weight:4
OK:display_completed
> display_drones,kroger
droneID:1,total_cap:40,num_orders:1,remaining_cap:20,current_battery:100,flown_by:Finneas_Fig
droneID:2,total_cap:20,num_orders:1,remaining_cap:16,current_battery:50
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> display_orders,publix
orderID:purchaseA
item_name:cheesecake,total_quantity:3,total_cost:30,total_weight:24
OK:display_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:1,remaining_cap:16,current_battery:50,flown_by:Gillian_Grape
OK:display_completed
> display_customers
name:Alana_Apple,phone:222-222-2222,rating:4,credit:70
name:Carlos_Cherry,phone:444-444-4444,rating:5,credit:300
OK:display_completed
> display_stores
name:kroger,revenue:33030,addressLat:3.0,addressLng:6.0
name:publix,revenue:33000,addressLat:10.0,addressLng:20.0
OK:display_completed
> display_pilots
name:Finneas_Fig,phone:888-888-8888,taxID:890-12-3456,licenseID:panam_10,experience:34
name:Gillian_Grape,phone:999-999-9999,taxID:234-56-7890,licenseID:twa_21,experience:31
OK:display_completed
```
### // deliver orders from various stores
```
purchase_order,whole_foods,purchaseA,20,5,6,0
switch_role,90100,3j8fr7gu89j,admin,kroger
purchase_order,kroger,purchaseF,100,5,8,1
purchase_order,kroger,purchaseB,70,3,4,0
purchase_order,kroger,purchaseD,60,50,30,0
fly_drone,kroger,2,ffig8
purchase_order,kroger,purchaseD,60,5,10,0
purchase_order,kroger,purchaseD,60,5,20,0
purchase_order,kroger,purchaseD,60,20,6,1
purchase_order,kroger,purchaseD,60,20,6,2
display_orders,kroger
display_drones,kroger
display_customers
display_stores
display_pilots
switch_role,90102,84g48u8g09,admin,publix
display_orders,publix
display_drones,publix
purchase_order,publix,purchaseA,230,8,15,0
purchase_order,publix,purchaseA,230,8,15,1
display_orders,publix
display_drones,publix
display_customers
display_stores
display_pilots
```
Expected outcome:

* If order_identifier_does_not_exist (eg, purchaseF for kroger), then we cannot purchase order.
* Order "purchaseD" of kroger is assigned to drone 2 of kroger, but that drone does not have a pilot, therefore throw error message drone_needs_pilot.
* After running fly_drone,kroger,2,ffig8, and "wait" until the drone is charged (after the charging time increased), we are able to run "purchase_order,kroger,purchaseD,60,20,6,2".
* After order "purchaseD" is delivered, it is removed from the "display_orders,kroger" command.
* After order "purchaseD" is delivered, the remaining number of order of that drone, the customer's credit, store's revenue, and pilot's experience also changed.
* Similar delivery action is done for order "purchaseA" of publix.
```
> purchase_order,whole_foods,purchaseA,20,5,6,0
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> purchase_order,kroger,purchaseF,100,5,8,1
ERROR:order_identifier_does_not_exist
> purchase_order,kroger,purchaseB,70,3,4,0
ERROR:drone_needs_charge
> purchase_order,kroger,purchaseD,60,50,30,0
ERROR:drone_needs_pilot
> fly_drone,kroger,2,ffig8
OK:change_completed
> purchase_order,kroger,purchaseD,60,5,10,0
ERROR:drone_needs_charge
> purchase_order,kroger,purchaseD,60,5,20,0
ERROR:drone_needs_charge
> purchase_order,kroger,purchaseD,60,20,6,1
ERROR:drone_needs_charge
> purchase_order,kroger,purchaseD,60,20,6,2
OK:change_completed
> display_orders,kroger
orderID:purchaseB
item_name:pot_roast,total_quantity:4,total_cost:20,total_weight:20
OK:display_completed
> display_drones,kroger
droneID:1,total_cap:40,num_orders:1,remaining_cap:20,current_battery:100
droneID:2,total_cap:20,num_orders:0,remaining_cap:20,current_battery:60,flown_by:Finneas_Fig
OK:display_completed
> display_customers
name:Alana_Apple,phone:222-222-2222,rating:4,credit:70
name:Carlos_Cherry,phone:444-444-4444,rating:5,credit:290
OK:display_completed
> display_stores
name:kroger,revenue:33040,addressLat:3.0,addressLng:6.0
name:publix,revenue:33000,addressLat:10.0,addressLng:20.0
OK:display_completed
> display_pilots
name:Finneas_Fig,phone:888-888-8888,taxID:890-12-3456,licenseID:panam_10,experience:35
name:Gillian_Grape,phone:999-999-9999,taxID:234-56-7890,licenseID:twa_21,experience:31
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> display_orders,publix
orderID:purchaseA
item_name:cheesecake,total_quantity:3,total_cost:30,total_weight:24
OK:display_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:1,remaining_cap:16,current_battery:50,flown_by:Gillian_Grape
OK:display_completed
> purchase_order,publix,purchaseA,230,8,15,0
ERROR:drone_needs_charge
> purchase_order,publix,purchaseA,230,8,15,1
OK:change_completed
> display_orders,publix
OK:display_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:230,flown_by:Gillian_Grape
OK:display_completed
> display_customers
name:Alana_Apple,phone:222-222-2222,rating:4,credit:70
name:Carlos_Cherry,phone:444-444-4444,rating:5,credit:260
OK:display_completed
> display_stores
name:kroger,revenue:33040,addressLat:3.0,addressLng:6.0
name:publix,revenue:33030,addressLat:10.0,addressLng:20.0
OK:display_completed
> display_pilots
name:Finneas_Fig,phone:888-888-8888,taxID:890-12-3456,licenseID:panam_10,experience:35
name:Gillian_Grape,phone:999-999-9999,taxID:234-56-7890,licenseID:twa_21,experience:32
OK:display_completed
```
### // transfer order and display efficiency
```
transfer_order,kroger,purchaseB,2
display_efficiency
```
Expected outcome:

* Order purchaseB is transferred to drone 2
* Efficiency is displayed
```
transfer_order,kroger,purchaseB,2
> transfer_order,kroger,purchaseB,2
OK:change_completed
display_efficiency
> display_efficiency
name:kroger,purchases:2,overloads:1,transfers:1
name:publix,purchases:1,overloads:0,transfers:0
OK:display_completed
```
### // cancel orders per customer requests
```
cancel_order,whole_foods,purchaseB
switch_role,90100,3j8fr7gu89j,admin,kroger
cancel_order,kroger,purchaseG
cancel_order,kroger,purchaseB
display_orders,kroger
display_drones,kroger
switch_role,90102,84g48u8g09,admin,publix
display_orders,publix
display_drones,publix
display_customers
display_stores
display_pilots
```
Expected outcome:

* If order_identifier_does_not_exist (eg, purchaseG for kroger), then we cannot cancel order.
* Order "purchaseB" of kroger is the only remaining order after we run previous codes, and we can cancel it by running "cancel_order,kroger,purchaseB"
* After we cancel "purchaseB" of kroger, then it's removed from "display_orders,kroger"
* After we cancel "purchaseB" of kroger, the number of order of the assigned drone (drone 2 of kroger) is reduced from 1 to 0 as well.
* After we cancel "purchaseB" of kroger, the customer credit and store revenue was not affected.
```
> cancel_order,whole_foods,purchaseB
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> cancel_order,kroger,purchaseG
ERROR:order_identifier_does_not_exist
> cancel_order,kroger,purchaseB
OK:change_completed
> display_orders,kroger
OK:display_completed
> display_drones,kroger
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:100
droneID:2,total_cap:20,num_orders:0,remaining_cap:20,current_battery:60,flown_by:Finneas_Fig
OK:display_completed
> switch_role,90102,84g48u8g09,admin,publix
OK:login_completed
> display_orders,publix
OK:display_completed
> display_drones,publix
droneID:1,total_cap:40,num_orders:0,remaining_cap:40,current_battery:230,flown_by:Gillian_Grape
OK:display_completed
> display_customers
name:Alana_Apple,phone:222-222-2222,rating:4,credit:70
name:Carlos_Cherry,phone:444-444-4444,rating:5,credit:260
OK:display_completed
> display_stores
name:kroger,revenue:33040,addressLat:3.0,addressLng:6.0
name:publix,revenue:33030,addressLat:10.0,addressLng:20.0
OK:display_completed
> display_pilots
name:Finneas_Fig,phone:888-888-8888,taxID:890-12-3456,licenseID:panam_10,experience:35
name:Gillian_Grape,phone:999-999-9999,taxID:234-56-7890,licenseID:twa_21,experience:32
OK:display_completed
```

## Part II Authentication & Authorization
### // check if user exist
```
switch_role,90555,98w985ht,admin,heb
switch_role,90100,3j8fr7gu89j,admin,kroger
```
Expected outcome:

If UIN (unique identity number) does not exist, throw error UIN_does_not_exist and prevent login
```
> switch_role,90555,98w985ht,admin,heb
ERROR:UIN_does_not_exist
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
```

### // check if role is correct
```
switch_role,90500,03j408q3tj,admin,aapple2
switch_role,90500,03j408q3tj,pilot,aapple2
switch_role,90500,03j408q3tj,customer,aapple2
```
Expected outcome:

User "aapple2" is a customer, so he/she cannot access the system with role of admin or pilot, thus preventing the user entering the system with some role that is not theirs.
```
> switch_role,90555,98w985ht,admin,heb
ERROR:UIN_does_not_exist
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> //check if role is correct
switch_role,90500,03j408q3tj,admin,aapple2
> switch_role,90500,03j408q3tj,admin,aapple2
ERROR: this UIN does not have access to the role admin
switch_role,90500,03j408q3tj,pilot,aapple2
> switch_role,90500,03j408q3tj,pilot,aapple2
ERROR: this UIN does not have access to the role pilot
switch_role,90500,03j408q3tj,customer,aapple2
> switch_role,90500,03j408q3tj,customer,aapple2
OK:login_completed
```

### // use authentication to protect user privacy
```
switch_role,90100,3j8fr7gu89j,admin,kroger
display_items,kroger
display_items,publix
```
Expected outcome:

The store "kroger" can only access "display_items,kroger", but cannot access "display_items,publix"
```
> switch_role,90100,3j8fr7gu89j,admin,kroger
OK:login_completed
> display_items,kroger
cheesecake,4
pot_roast,5
OK:display_completed
> display_items,publix
Error:you_do_not_have_permission_to_execute_this_command_please_switch_user
```
### // reset password if failed three times
```
switch_role,90100,123456789,admin,kroger
switch_role,90100,12345678910,admin,kroger
switch_role,90100,123456789101bc,admin,kroger
reset_passwd,90100,NewPWforkroger,800-888-8888
```
Expected outcome:

* Each time a wrong password was entered, tell the user how many attempts are left, when error attempts exceed 3, user are asked to reset the password.
* The reset_passwd command takes in UIN,new password, and phone-number. The phone_number act as a way to verify the user. After the password reset, the number of error attempts is changed back to zero.
```
> switch_role,90100,123456789,admin,kroger
ERROR:password is wrong, you have 2 more attempts
> switch_role,90100,12345678910,admin,kroger
ERROR:password is wrong, you have 1 more attempts
> switch_role,90100,123456789101bc,admin,kroger
ERROR:maximum attempts reached, please reset your password.
reset_passwd,90100,NewPWforkroger,800-888-8888
> reset_passwd,90100,NewPWforkroger,800-888-8888
OK:password_reset_completed
```
### // check if new password is correct
```
switch_role,90100,NewPWforkroger,admin,krogerv
```
Expected outcome:

We try to login with the new password for kroger, and it worked.
```
> switch_role,90100,NewPWforkroger,admin,kroger
OK:login_completed
```

# Thanks to all TAs and instructor's help. You are all the best!!