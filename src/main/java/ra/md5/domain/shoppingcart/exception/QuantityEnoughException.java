package ra.md5.domain.shoppingcart.exception;

public class QuantityEnoughException extends RuntimeException{
    public QuantityEnoughException(String message){
        super(message);
    }
}
