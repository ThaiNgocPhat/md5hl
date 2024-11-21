package ra.md5.domain.wishlist.exception;

public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(String message){
        super(message);
    }
}
