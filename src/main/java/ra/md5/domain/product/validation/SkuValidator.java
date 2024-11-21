package ra.md5.domain.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.md5.domain.product.repository.ProductRepository;

@Component
public class SkuValidator implements ConstraintValidator<SkuUnique, String> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !productRepository.existsProductBySku(value);
    }
}
