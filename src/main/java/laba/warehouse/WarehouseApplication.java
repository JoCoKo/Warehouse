package laba.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class WarehouseApplication {

    public static void main(String[] args) {
    	SpringApplication.run(WarehouseApplication.class, args);
    }

}
