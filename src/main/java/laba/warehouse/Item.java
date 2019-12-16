package laba.warehouse;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Item {
    @Id
    public ObjectId _id;
    private String name;
    private int amount;
    private double price;


    public Item(ObjectId _id, String name, int amount, double price) {
        this._id = _id;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public String get_id() {
        return _id.toString();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
