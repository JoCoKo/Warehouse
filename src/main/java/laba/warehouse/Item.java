package laba.warehouse;

import laba.warehouse.exceptions.ForbiddenOperationException;
import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

public class Item {
    private static final Logger logger = LoggerFactory.getLogger(Item.class);
    @Id
    private ObjectId _id;
    private String name;
    private int amount;
    private double price;


    public Item(ObjectId _id, String name, int amount, double price) {
        logger.trace("----- Invocating constructor");
        this.set_id(_id);
        this.setName(name);
        this.setAmount(amount);
        this.setPrice(price);
    }

    public String get_id() {
        return _id.toString();
    }

    public void set_id(ObjectId _id) {
        logger.trace("----- Invocating set_id {}", _id);
        if (_id == null || _id.toString().isBlank()) {
            logger.trace("----- Generate new id");
            this._id = ObjectId.get();
        } else this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        logger.trace("----- Invocating setName {}", name);
        if (name==null || name.isBlank()) {
            logger.error("----- Got blank name field exception");
            throw new ForbiddenOperationException("Name can't be blank");
        }
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        logger.trace("----- Invocating setAmount {}", amount);
        if (amount < 0) {
            logger.error("----- Got amount < 0 exception");
            throw new ForbiddenOperationException("Amount can't become < 0");
        }
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        logger.trace("----- Invocating setPrice {}", price);
        if (price < 0) {
            logger.error("----- Got price < 0 exception");
            throw new ForbiddenOperationException("Price can't be negative");
        }
        this.price = price;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id",_id.toString());
        obj.put("name",name);
        obj.put("price",price);
        return obj;
    }

    @Override
    public String toString() {
        return "Item{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
