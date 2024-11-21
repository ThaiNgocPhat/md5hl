package ra.md5.domain.order.exception;

public class OrderStatusException extends RuntimeException{
    public OrderStatusException(String message){
        super(message);
    }
}
