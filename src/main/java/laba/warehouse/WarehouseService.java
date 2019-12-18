package laba.warehouse;


import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
    private final ItemRepository itemRepository;

    WarehouseService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {
        logger.info("--- Get all items");
        return itemRepository.findAll();
    }

    public Item findBy_id(ObjectId id) {
        logger.info("--- Get item by id {}", id.toString());
        if (!itemRepository.existsById(id.toString()))
            throw new NotFoundException("No item with id" + id.toString());
        return itemRepository.findBy_id(id);
    }

    public Item createItem(Item item) {
        logger.info("--- Create {}", item.toString());
        itemRepository.save(item);
        return item;
    }

    public Item changeAmount(ObjectId id, String am) {
        if (!itemRepository.existsById(id.toString()))
            throw new NotFoundException("No item with id" + id.toString());

        int amount;

        try {
            amount = Integer.parseInt(am);
        }
        catch (NumberFormatException e){
            logger.error("----- Got Incorrect amount exception");
            throw new ForbiddenOperationException("Incorrect amount");
        }

        if (Math.abs(amount) > 10000 || amount == 0) {
            logger.error("----- Got |amount|>10_000 exception");
            throw new ForbiddenOperationException("Amount for 1 operation should be in (0;10000)");
        }

        Item item = itemRepository.findBy_id(id);
        item.setAmount(item.getAmount() + amount);
        logger.info("--- Change amount of item id={} on {}", id, amount);
        return itemRepository.save(item);
    }
}
