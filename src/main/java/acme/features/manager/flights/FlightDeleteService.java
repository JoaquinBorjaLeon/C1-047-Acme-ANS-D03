
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.realms.airlinemanager.AirlineManager;

@GuiService
public class FlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findById(flightId);

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = flight != null && flight.isDraftMode() && flight.getManager().getId() == managerId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id = super.getRequest().getData("id", int.class);
		flight = this.repository.findById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		dataset.put("draftMode", flight.isDraftMode());
		super.getResponse().addData(dataset);
	}
}
