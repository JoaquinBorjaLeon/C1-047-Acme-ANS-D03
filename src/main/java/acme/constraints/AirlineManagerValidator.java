
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.airlinemanager.AirlineManager;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager AirlineManager, final ConstraintValidatorContext context) {
		// HINT: AirlineManager can be null
		assert context != null;

		boolean result;

		if (AirlineManager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			DefaultUserIdentity identity = AirlineManager.getIdentity();
			String nombreInicial = identity.getName().substring(0, 1);
			String surname = identity.getSurname();
			String[] palabras = surname.split("\\s");
			String surnameIniciales = "";
			int limite = Math.min(palabras.length, 2);
			for (int i = 0; i < limite; i++)
				if (!palabras[i].isEmpty())
					surnameIniciales += palabras[i].substring(0, 1);
			surnameIniciales = surnameIniciales.toUpperCase();
			String iniciales = nombreInicial + surnameIniciales;
			String identifier = AirlineManager.getIdentifierNumber();

			Boolean esCorrectoIdentificador = identifier.startsWith(iniciales);
			super.state(context, esCorrectoIdentificador, "identifierNumber", "Initials Error");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
