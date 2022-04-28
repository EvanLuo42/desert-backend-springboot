package com.cactus.desert.desertbackend.form;

import lombok.AllArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author EvanLuo42
 * @date 4/24/22 1:40 PM
 */
@AllArgsConstructor
public class FieldError {
    private Path propertyPath;

    private String messageTemplate;

    private String message;

    private static FieldError of(ConstraintViolation<? extends Form> constraintViolation) {
        return new FieldError(
                constraintViolation.getPropertyPath(),
                constraintViolation.getMessageTemplate(),
                constraintViolation.getMessage());
    }

    public static List<FieldError> getErrors(Set<ConstraintViolation<Form>> constraintViolations) {

        return constraintViolations.stream()
                .map(FieldError::of)
                .collect(Collectors.toList());
    }
}
