package com.littlecode.business.annotation.constraint;

import com.littlecode.business.annotation.FieldCheckForBusiness;
import com.littlecode.business.exceptions.BusinessValidatorException;
import com.littlecode.business.validator.privates.ValidatorBase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class FieldCheckConstraint implements ConstraintValidator<FieldCheckForBusiness, String> {
    private FieldCheckForBusiness targetAnnotation;

    private ValidatorBase createValidator(Class<?> aClass) {
        try {
            var c = aClass.getConstructor();
            return (ValidatorBase) c.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(FieldCheckForBusiness targetAnnotation) {
        this.targetAnnotation = targetAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (targetAnnotation == null)
            return false;
        if (targetAnnotation.validator() == null)
            throw new RuntimeException("Validator is null");
        if (targetAnnotation.nullable() && (value == null || value.trim().isEmpty()))
            return true;

        List.of(this.targetAnnotation.validator())
                .forEach(aClass ->
                        {
                            var validator = createValidator(aClass);
                            validator.setNullable(this.targetAnnotation.nullable());
                            validator.setValue(value);
                            validator.setMessage(this.targetAnnotation.message());
                            if (!validator.isValid())
                                throw new BusinessValidatorException(validator.message());
                        }
                );
        return true;
    }
}
