package ra.md5.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ra.md5.domain.address.exception.AddressNotFoundException;
import ra.md5.domain.category.exception.CategoryNotFoundException;
import ra.md5.domain.order.exception.*;
import ra.md5.domain.product.exception.ProductNotFoundException;
import ra.md5.domain.product.exception.ProductSearchException;
import ra.md5.domain.role.exception.DuplicateRoleException;
import ra.md5.domain.role.exception.RoleNotFoundException;
import ra.md5.domain.shoppingcart.exception.InsufficientStockException;
import ra.md5.domain.shoppingcart.exception.NotItemsException;
import ra.md5.domain.shoppingcart.exception.QuantityEnoughException;
import ra.md5.domain.shoppingcart.exception.ShoppingCartNotFoundException;
import ra.md5.domain.user.dto.res.user.ResponseError;
import ra.md5.domain.user.exception.DuplicateException;
import ra.md5.domain.user.exception.InvalidCredentialsException;
import ra.md5.domain.user.exception.NotFoundException;
import ra.md5.domain.wishlist.exception.WishListException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ResponseError> handleDuplicateData(DuplicateException e){
        return new ResponseEntity<>(new ResponseError(409,HttpStatus.CONFLICT,e.getMessage()), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotFoundException .class)
    public ResponseEntity<ResponseError> NotFoundException(NotFoundException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ResponseError> NotFoundException(AddressNotFoundException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ResponseError> NotFoundException(InvalidRequestException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotItemsException.class)
    public ResponseEntity<ResponseError> NotFoundException(NotItemsException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ResponseError> NotFoundException(InsufficientStockException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(SerialNumberNotFound.class)
    public ResponseEntity<ResponseError> NotFoundException(SerialNumberNotFound e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrderStatusException.class)
    public ResponseEntity<ResponseError> NotFoundException(OrderStatusException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<ResponseError> NotFoundException(ShoppingCartNotFoundException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ResponseError> handleNotValid(CategoryNotFoundException e){
        return new ResponseEntity<>(new ResponseError(404, HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ResponseError> handleNotValid(RoleNotFoundException e){
        return new ResponseEntity<>(new ResponseError(404, HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrderIdNotFoundException.class)
    public ResponseEntity<ResponseError> handleNotValid(OrderIdNotFoundException e){
        return new ResponseEntity<>(new ResponseError(404, HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<ResponseError> handleNotValid(DuplicateRoleException e){
        return new ResponseEntity<>(new ResponseError(409, HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseError> handleMissingParams(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(new ResponseError(400,HttpStatus.BAD_REQUEST,e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProductSearchException.class)
    public ResponseEntity<ResponseError> handleNotValid(ProductSearchException e){
        return new ResponseEntity<>(new ResponseError(404, HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ResponseError> handleNotValid(ProductNotFoundException e){
        return new ResponseEntity<>(new ResponseError(404, HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleNotValid(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(new ResponseError(400,HttpStatus.BAD_REQUEST,errors), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseError> handleNotValid(InvalidCredentialsException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("username", e.getMessage());
        return new ResponseEntity<>(new ResponseError(401,HttpStatus.UNAUTHORIZED,errors), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(QuantityEnoughException.class)
    public ResponseEntity<ResponseError> handleDuplicateData(QuantityEnoughException e){
        return new ResponseEntity<>(new ResponseError(422,HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ResponseError> NotFoundException(OrderException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(WishListException.class)
    public ResponseEntity<ResponseError> NotFoundException(WishListException e){
        return new ResponseEntity<>(new ResponseError(404,HttpStatus.NOT_FOUND,e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
