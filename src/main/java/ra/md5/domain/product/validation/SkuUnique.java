package ra.md5.domain.product.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(
        validatedBy = {SkuValidator.class}
)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkuUnique {
    String message() default "{SKU đã tồn tại}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
