package ra.md5.domain.shoppingcart.exception;

public class ShoppingCartNotFoundException extends RuntimeException{
    public ShoppingCartNotFoundException(String message){
        super(message);
    }
}
