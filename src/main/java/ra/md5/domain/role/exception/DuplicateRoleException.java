package ra.md5.domain.role.exception;

public class DuplicateRoleException extends RuntimeException{
    public DuplicateRoleException(String message){
        super(message);
    }
}
