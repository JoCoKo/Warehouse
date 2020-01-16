package laba.warehouse.broker;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public Declarables exchanges() {
        FanoutExchange warehouseExchange = new FanoutExchange(RabbitConstants.WAREHOUSE_EXCHANGE_RESERVE_ITEMS,true,false);
        Queue queueReservedItems= new Queue(RabbitConstants.WAREHOUSE_QUEUE_RESERVE_ITEM, false);
        Queue queueReserveItemsCanceled=new Queue(RabbitConstants.WAREHOUSE_QUEUE_RESERVE_ITEM_CANCELED,false);
        //FanoutExchange testExchange = new FanoutExchange(RabbitConstants.TEST_EXCHANGE,false,false);
        return new Declarables(
                warehouseExchange,
                queueReservedItems,
                queueReserveItemsCanceled,
                BindingBuilder.bind(queueReservedItems).to(warehouseExchange)
                //BindingBuilder.bind(queueReserveItemsCanceled).to(testExchange)
        );
    }
}
