package ra.md5.domain.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.md5.domain.user.repository.UserRepository;

@Component
public class PhoneValidator implements ConstraintValidator<PhoneUnique,String> {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByPhone(value);
    }
}
