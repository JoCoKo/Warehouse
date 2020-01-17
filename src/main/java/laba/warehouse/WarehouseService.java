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
        //test();
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

    public Item changeAmount(ObjectId id, String strAmount) {
        if (!itemRepository.existsBy_id(id))
            throw new NotFoundException("No item with id" + id.toString());
        int amount;

        try {
            amount = Integer.parseInt(strAmount);
        } catch (NumberFormatException e) {
            logger.error("----- Got Incorrect amount exception");
            throw new ForbiddenOperationException("Incorrect amount passed");
        }

        if (Math.abs(amount) > 10000 || amount == 0) {
            logger.error("----- Got amount>10_000 or <=0 exception");
            throw new ForbiddenOperationException("Amount for 1 operation should be in (0;10000)");
        }

        Item item = itemRepository.findBy_id(id);
        item.setAmount(item.getAmount() - amount);

        logger.info("--- Change amount of item id={} on {}", id, amount);
        return itemRepository.save(item);
    }

    public Item reserveItems(ObjectId id, JSONObject jsonObject) {
        String username;
        String strAmount;

        if (jsonObject.containsKey("amount") && jsonObject.containsKey("username")) {
            strAmount = jsonObject.getAsString("amount");
            username = jsonObject.getAsString("username");
        } else {
            logger.error("----- Not all expected parameters passed");
            throw new ForbiddenOperationException("Invalid params");
        }
        if (strAmount.isBlank() || username.isBlank()) {
            logger.error("----- Some required parameters passed are blank");
            throw new ForbiddenOperationException("Blank params");
        }

        Item item = changeAmount(id, strAmount);

        jsonObject.remove("amount");
        JSONObject j = item.toJson();
        j.put("amount", strAmount);
        jsonObject.put("Item", j);


        logger.trace("****************************** Current JSON {} ", jsonObject);
        rabbitMQTemplate.convertAndSend(RabbitConstants.WAREHOUSE_EXCHANGE_RESERVE_ITEMS, "", jsonObject.toString());
        logger.info("----> Send message to rabbit {} ", jsonObject.toString());
        return item;
    }

    public void test() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "5df7bff906f34a21d47e4ba9");
        jsonObject.put("amount", "1");
        logger.info("----> Send message to RABBIT {} ", jsonObject);
        rabbitMQTemplate.convertAndSend(RabbitConstants.TEST_EXCHANGE, "", jsonObject);
    }

    @RabbitListener(queues = RabbitConstants.WAREHOUSE_QUEUE_RESERVE_ITEM_CANCELED)
    private void cancelReservation(JSONObject jsonObject) {
        logger.info("----> Got message from rabbit {} ", jsonObject);
        String id = jsonObject.getAsString("id");
        String amount = jsonObject.getAsString("amount");
        changeAmount(new ObjectId(id), "-"+amount);
    }

}
