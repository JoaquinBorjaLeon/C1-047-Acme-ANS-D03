
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.realms.airlinemanager.AirlineManager;

@GuiService
public class FlightCreateService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AirlineManager manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		Flight flight = new Flight();
		flight.setTag("");
		flight.setRequiresSelfTransfer(false);
		flight.setCost(new Money());
		flight.setDescription("");

		flight.setManager(manager);

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
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		super.getResponse().addData(dataset);
	}
}
