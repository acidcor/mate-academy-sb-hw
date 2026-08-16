package mate.academy.hw.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = FieldMatchImpl.class)
public @interface FieldMatch {
    String first();

    String second();

    String message() default "Values mismatch";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
