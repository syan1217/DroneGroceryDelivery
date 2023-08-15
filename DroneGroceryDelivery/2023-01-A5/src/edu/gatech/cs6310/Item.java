package edu.gatech.cs6310;
import java.util.TreeMap;
public class Item implements Comparable<Item> {
    private String name;
    private Store store;
    private int weight;
    private int unitPrice;

    public Item (Store store, String name,  int weight) {
        this.name = name;
        this.store = store;
        this.weight = weight;
        //store.addToStoreItemsList(this.name, this);
    }
    public Store getStore() {
        return this.store;
    }
    public void setStore(Store store) {
        this.store = store;
    }
    public int getWeight() {
        return this.weight;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String newName) {
        this.name = newName;
    }

    public int getPrice() {
        return this.unitPrice;
    }
    public void setPrice(int newPrice) {
        this.unitPrice = newPrice;
    }

    public int compareTo(Item i){
        return this.name.compareTo(i.getName());
    }
}
