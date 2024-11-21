package ra.md5.domain.category.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ra.md5.domain.category.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class CategoryNameValidator implements ConstraintValidator<CategoryNameUnique, String> {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !categoryRepository.existsByCategoryName(value);
    }
}
