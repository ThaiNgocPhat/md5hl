package ra.md5.domain.order.exception;

public class OrderIdNotFoundException extends RuntimeException{
    public OrderIdNotFoundException(String message){
        super(message);
    }
}
