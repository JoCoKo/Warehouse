package laba.warehouse;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/warehouse")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemRepository repository;

    @RequestMapping(value = "/getitems", method = RequestMethod.GET)
    public List<Item> getAllItems() {
        logger.info("--- Get all items");
        return repository.findAll();
    }

    @RequestMapping(value = "/getitem/{id}", method = RequestMethod.GET)
    public Item getItemById(@PathVariable("id") ObjectId id) {
        logger.info("--- Get item by id {}", id.toString());
        return repository.findBy_id(id);
    }

    @RequestMapping(value = "/createitem", method = RequestMethod.POST)
    public Item createItem(@Valid @RequestBody Item item) {
        item.set_id(ObjectId.get());
        repository.save(item);
        logger.info("--- Create item {}", item.toString());
        return item;
    }

    @RequestMapping(value = "/additem", method = RequestMethod.PUT)
    public Item addAmountItem(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        int amount = Integer.parseInt(map.get("amount"));
        Item item = repository.findBy_id(new ObjectId(id));
        item.addAmount(amount);
        logger.info("--- Increase amount of item id={} by {}", id, amount);
        return repository.save(item);
    }

    @RequestMapping(value = "/detractitem", method = RequestMethod.PUT)
    public Item detractAmountItem(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        int amount = Integer.parseInt(map.get("amount"));
        Item item = repository.findBy_id(new ObjectId(id));
        item.subtractAmount(amount);
        logger.info("--- Decrease amount of item id={} by {}", id, amount);
        return repository.save(item);
    }


}