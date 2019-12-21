package laba.warehouse.broker;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public Declarables exchanges() {
        FanoutExchange warehouseExchange = new FanoutExchange(RabbitConstants.WAREHOUSE_EXCHANGE_RESERVE_ITEMS,true,false);
        Queue queueReservedItems= new Queue(RabbitConstants.WAREHOUSE_QUEUE_RESERVE_ITEM, true);
        Queue queueReserveItemsCanceled=new Queue(RabbitConstants.WAREHOUSE_QUEUE_RESERVE_ITEM_CANCELED,true);

        return new Declarables(
                warehouseExchange,
                queueReservedItems,
                queueReserveItemsCanceled,
                BindingBuilder.bind(queueReservedItems).to(warehouseExchange)
        );
    }
}
