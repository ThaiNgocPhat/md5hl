package ra.md5.domain.category.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(
        validatedBy = {CategoryNameValidator.class}
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryNameUnique {
    String message() default "{Tên thể loại đã tồn tại}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
