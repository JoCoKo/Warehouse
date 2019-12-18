package laba.warehouse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    NotFoundException(String message){
        super(message);
        logger.error("----- Got Id not found exception");
    }
}
