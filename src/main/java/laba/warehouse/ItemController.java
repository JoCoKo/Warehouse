package laba.warehouse;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/warehouse")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final WarehouseService warehouseService;

    public ItemController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public List<Item> getAllItems() {
        return warehouseService.findAll();
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public Item getItemById(@PathVariable("id") ObjectId id) {
        return warehouseService.findBy_id(id);
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public Item createItem(@RequestBody Item item) {
        logger.info("---- got createItem request");
        return warehouseService.createItem(item);
    }

    @RequestMapping(value = "/item", method = RequestMethod.PUT)
    public Item changeAmount(@RequestBody Map<String, String> map) {
        return warehouseService.changeAmount(map);
    }
}