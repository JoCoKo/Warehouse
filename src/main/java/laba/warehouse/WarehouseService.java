package laba.warehouse;

import laba.warehouse.broker.RabbitConstants;
import laba.warehouse.exceptions.ForbiddenOperationException;
import laba.warehouse.exceptions.NotFoundException;
import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@EnableRabbit
@Service
public class WarehouseService {

    private final RabbitTemplate rabbitMQTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
    private final ItemRepository itemRepository;

    WarehouseService(ItemRepository itemRepository, RabbitTemplate rabbitMQTemplate) {
        this.itemRepository = itemRepository;
        this.rabbitMQTemplate = rabbitMQTemplate;
    }

    public List<Item> findAll() {
        logger.info("--- Get all items");
        return itemRepository.findAll();
    }

    public Item findBy_id(ObjectId id) {
        logger.info("--- Get item by id {}", id.toString());
        if (!itemRepository.existsBy_id(id))
            throw new NotFoundException("No item with id" + id.toString());
        return itemRepository.findBy_id(id);
    }

    public Item createItem(Item item) {
        logger.info("--- Create {}", item.toString());
        itemRepository.save(item);
        return item;
    }

    public Item changeAmount(ObjectId id, int amount) {
        if (!itemRepository.existsBy_id(id))
            throw new NotFoundException("No item with id" + id.toString());

        Item item = itemRepository.findBy_id(id);
        item.setAmount(item.getAmount() - amount);

        logger.info("--- Change amount of item id={} on {}", id, amount);
        return itemRepository.save(item);
    }

    public Item reserveItems(ObjectId id, JSONObject jsonObject) {
        String username;
        String strAmount;
        String strOrderID;
        int amount;
        int OrderID;

        if(jsonObject.containsKey("amount") && jsonObject.containsKey("orderID") && jsonObject.containsKey("username")){
            strAmount=jsonObject.getAsString("amount");
            strOrderID=jsonObject.getAsString("orderID");
            username=jsonObject.getAsString("username");
        }
        else {
            logger.error("----- Not all expected parameters passed");
            throw new ForbiddenOperationException("Invalid params");
        }
        if(strAmount.isBlank() || strOrderID.isBlank() || username.isBlank()){
            logger.error("----- Some required parameters passed are blank");
            throw new ForbiddenOperationException("Blank params");
        }
        try {
            amount = Integer.parseInt(strAmount);
            OrderID= Integer.parseInt(strOrderID);
        }
        catch (NumberFormatException e){
            logger.error("----- Got Incorrect number exception");
            throw new ForbiddenOperationException("Incorrect number passed");
        }

        if (amount > 10000 || amount <= 0) {
            logger.error("----- Got amount>10_000 or <=0 exception");
            throw new ForbiddenOperationException("Amount for 1 operation should be in (0;10000)");
        }

        Item item = changeAmount(id,amount);

        jsonObject.remove("amount");
        JSONObject j = item.toJson();
        j.put("amount", amount);
        jsonObject.put("Item",j);


        logger.trace("****************************** Current JSON {} ", jsonObject);
        rabbitMQTemplate.convertAndSend(RabbitConstants.WAREHOUSE_EXCHANGE_RESERVE_ITEMS,"", jsonObject.toString());
        logger.info("----> Send message to rabbit {} ", jsonObject.toString());
        return item;
    }

    @RabbitListener(queues = RabbitConstants.WAREHOUSE_QUEUE_RESERVE_ITEM_CANCELED)
    private void cancelReservation(JSONObject jsonObject) {
        logger.info("----> Got message from rabbit {} ", jsonObject);
        String id = jsonObject.getAsString("id");
        int amount = jsonObject.getAsNumber("amount").intValue();
        changeAmount(new ObjectId(id),amount*-1);
    }

}
