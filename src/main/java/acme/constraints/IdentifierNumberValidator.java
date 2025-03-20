
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.airlinemanager.AirlineManager;

public class IdentifierNumberValidator extends AbstractValidator<ValidIdentifierNumber, AirlineManager> {

	@Override
	protected void initialise(final ValidIdentifierNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airlineManager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean codeContainsInitials = true;

			try {
				String code = airlineManager.getIdentifierNumber();
				String name = airlineManager.getUserAccount().getIdentity().getName();
				String surname = airlineManager.getUserAccount().getIdentity().getSurname();

				char codeFirstChar = Character.toUpperCase(code.charAt(0));
				char codeSecondChar = Character.toUpperCase(code.charAt(1));
				char nameFirstChar = Character.toUpperCase(name.charAt(0));
				char surnameFirstChar = Character.toUpperCase(surname.charAt(0));

				if (!(codeFirstChar == nameFirstChar && codeSecondChar == surnameFirstChar))
					codeContainsInitials = false;

			} catch (Exception e) {
				codeContainsInitials = false;
			}

			super.state(context, codeContainsInitials, "*", "acme.validation.role.identifier.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
