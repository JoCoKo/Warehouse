package laba.warehouse;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {
    Item findBy_id(ObjectId _id);
    boolean existsBy_id(ObjectId _id);
}

