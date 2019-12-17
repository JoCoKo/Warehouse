package laba.warehouse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse")
public class ItemController {
    @Autowired
    private ItemRepository repository;

    @RequestMapping(value = "/getitems", method = RequestMethod.GET)
    public List<Item> getAllItems() {
        return repository.findAll();
    }

    @RequestMapping(value = "/getitem/{id}", method = RequestMethod.GET)
    public Item getItemById(@PathVariable("id") ObjectId id) {
        return repository.findBy_id(id);
    }

    @RequestMapping(value = "/createitem", method = RequestMethod.POST)
    public Item createItem(@Valid @RequestBody Item item) {
        item.set_id(ObjectId.get());
        repository.save(item);
        return item;
    }

    @RequestMapping(value = "/additem", method = RequestMethod.PUT)
    public Item addAmountItem(@RequestBody Map<String, String> map) {
        Item item = repository.findBy_id(new ObjectId(map.get("id")));
        item.addAmount(Integer.parseInt(map.get("amount")));
        return repository.save(item);
    }

    @RequestMapping(value = "/detractitem", method = RequestMethod.PUT)
    public Item detractAmountItem(@RequestBody Map<String, String> map) {
        Item item = repository.findBy_id(new ObjectId(map.get("id")));
        item.subtractAmount(Integer.parseInt(map.get("amount")));
        return repository.save(item);
    }


}