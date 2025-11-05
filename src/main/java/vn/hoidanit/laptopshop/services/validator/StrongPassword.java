package vn.hoidanit.laptopshop.services.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
//right here im just defining the annotation for the StrongPasswordValidator
//the 4 @Constrants, @Target, @Retention, @Documented are just for the annotation to work
//the @interface makes it a annotation to be used elsewhere
//the @Constraint(validatedBy = StrongPasswordValidator.class) is the validator for the annotation
//the StrongPasswordValidator.class is the class that implements the validator
//the @Target({ ElementType.METHOD, ElementType.FIELD }) is the working scope of the annotation (just one field or a whole class)
//the @Retention(RetentionPolicy.RUNTIME) is telling the annotation to be available at runtime
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "Must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
