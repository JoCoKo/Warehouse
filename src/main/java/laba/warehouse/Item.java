package laba.warehouse;

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
        logger.info("----- Now in constructor");
        this.set_id(_id);
        this.setName(name);
        this.setAmount(amount);
        this.setPrice(price);
    }

    public String get_id() {
        return _id.toString();
    }

    public void set_id(ObjectId _id) {
        logger.info("----- Now in set_id {}",_id);
        if (_id.toString().isBlank())
            throw new ForbiddenOperationException("ID can't be blank");
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        logger.info("----- Now in setName {}",name);
        if (name.isBlank())
            throw new ForbiddenOperationException("Name can't be blank");
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        logger.info("----- Now in setAmount {}", amount);
        if (amount < 0)
            throw new ForbiddenOperationException("Amount can't become < 0");
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        logger.info("----- Now in setPrice {}",price);
        if (price < 0)
            throw new ForbiddenOperationException("Price can't be negative");
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "_id=" + _id.toString() +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
