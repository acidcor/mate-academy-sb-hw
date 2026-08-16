package mate.academy.hw.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchImpl implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            Field firstObj = value.getClass().getDeclaredField(firstFieldName);
            firstObj.setAccessible(true);
            Object firstValue = firstObj.get(value);

            Field secondObj = value.getClass().getDeclaredField(secondFieldName);
            secondObj.setAccessible(true);
            Object secondValue = secondObj.get(value);
            return Objects.equals(firstValue, secondValue);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            return false;
        }
    }
}
