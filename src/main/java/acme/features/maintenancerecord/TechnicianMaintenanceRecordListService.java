package acme.features.maintenancerecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenancerecord.MaintenanceRecord;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	//Internal state ---------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecord;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		maintenanceRecord = this.repository.findAllByTechnicianId(technicianId);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "aircraft", "status", "estimatedCost", "maintenanceMoment", "nextInspectionDate");

		super.addPayload(dataset, maintenanceRecord, "notes");

		super.getResponse().addData(dataset);
	}

}