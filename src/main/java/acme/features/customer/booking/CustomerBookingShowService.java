
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.Passenger;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices classChoices;
		SelectChoices flightChoices;

		Collection<Flight> flights;
		Collection<Passenger> passengersInDraftMode;

		boolean status = true;
		boolean anyPassengerInDraftMode = true;

		flights = this.repository.findAllFlights();
		flightChoices = SelectChoices.from(flights, "tag", booking.getFlight());
		classChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		passengersInDraftMode = this.repository.findPassengersInDraftMode(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble", "draftMode");
		dataset.put("bookingId", booking.getId());
		dataset.put("classes", classChoices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);

		if (!booking.getLastCardNibble().isEmpty())
			status = false;
		dataset.put("lastCardNibbleIsEmpty", status);
		if (passengersInDraftMode.isEmpty())
			anyPassengerInDraftMode = false;
		dataset.put("anyPassengerInDraftMode", anyPassengerInDraftMode);

		super.getResponse().addData(dataset);
	}
}
