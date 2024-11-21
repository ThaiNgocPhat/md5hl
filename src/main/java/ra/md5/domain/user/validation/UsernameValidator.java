package ra.md5.domain.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.md5.domain.user.repository.UserRepository;

@Component
public class UsernameValidator implements ConstraintValidator<UsernameUnique, String> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userRepository.existsUserByUsername(value);
    }
}
