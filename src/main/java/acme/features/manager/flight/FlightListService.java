
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.realms.airlinemanager.AirlineManager;

@GuiService
public class FlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> flights = this.repository.findFlightsByManagerId(managerId);
		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
