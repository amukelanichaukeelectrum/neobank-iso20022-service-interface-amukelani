package io.electrum.neobank.api.model.utils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

public class ApiModelValidator {

   private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

   private ApiModelValidator() {
   }

   public static <T> void validate(T objectToValidate) throws ValidationException {
      Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

      if (!violations.isEmpty()) {
         List<String> errors =
               violations.stream()
                     .map(
                           violation -> String.format(
                                 "%s for property %s with value %s",
                                 violation.getMessage(),
                                 violation.getPropertyPath(),
                                 violation.getInvalidValue()))
                     .collect(Collectors.toList());
         throw new ValidationException(
               MessageFormat.format(
                     "Validation failed due to the following errors in {0}. Errors = {1}",
                     objectToValidate.getClass(),
                     errors));
      }
   }
}
