
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.entities.maintenancerecord.MaintenanceRecord;

public class NextInspectionDateValidator extends AbstractValidator<ValidNextInspectionDate, MaintenanceRecord> {

	@Override
	public void initialise(final ValidNextInspectionDate annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {
		assert context != null;

		if (maintenanceRecord == null || maintenanceRecord.getMaintenaceMoment() == null || maintenanceRecord.getNextInspectionDate() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean nextInspectionDateAfterMaintenanceMoment = maintenanceRecord.getNextInspectionDate().after(maintenanceRecord.getMaintenaceMoment());
			super.state(context, nextInspectionDateAfterMaintenanceMoment, "nextInspectionDate", "acme.validation.maintenancerecord.nextinspectiondate.message");
		}

		return !super.hasErrors(context);
	}
}
