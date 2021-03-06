package laba.warehouse;

import net.minidev.json.JSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/warehouse")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final WarehouseService warehouseService;

    public ItemController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/items")
    public List<Item> getAllItems() {
        logger.info("---- got getAllItems request");
        return warehouseService.findAll();
    }

    @GetMapping("/item/{id}")
    public Item getItemById(@PathVariable("id") ObjectId id) {
        logger.info("---- got getItemById request");
        return warehouseService.findBy_id(id);
    }

    @PostMapping("/item")
    public Item createItem(@RequestBody Item item) {
        logger.info("---- got createItem request");
        return warehouseService.createItem(item);
    }

    @PostMapping("/item/{id}")
    public Item reserveItems(@PathVariable("id") ObjectId id, @RequestBody JSONObject json) {
        logger.info("---- got reserveItems request");
        return warehouseService.reserveItems(id,json);
    }

    @PutMapping("/item/{id}")
    public Item addItems(@PathVariable("id") ObjectId id, @RequestBody String amount) {
        logger.info("---- got addItems request");
        return warehouseService.changeAmount(id,"-"+amount);
    }
}