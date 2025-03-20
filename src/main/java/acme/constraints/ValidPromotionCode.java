
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PromotionCodeValidator.class)

public @interface ValidPromotionCode {

	String message() default "{acme.validation.service.promotion.code}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
