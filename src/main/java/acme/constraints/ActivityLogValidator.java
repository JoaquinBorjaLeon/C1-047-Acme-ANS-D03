
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.ActivityLog;
import acme.entities.legs.LegType;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}
	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean isLegLanded;
			isLegLanded = activityLog.getReporter().getLeg().getStatus() == LegType.LANDED;
			super.state(context, !isLegLanded, "legType", "acme.validation.ActivityLog.statusLeg.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
