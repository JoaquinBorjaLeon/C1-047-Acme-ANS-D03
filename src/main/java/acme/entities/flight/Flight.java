
package acme.entities.flight;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.entities.airport.Airport;
import acme.entities.legs.Leg;
import acme.realms.manager.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Size(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				cost;

	@Optional
	@Size(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;


	@Transient
	public Date getFlightDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Leg firstLeg = listOfLegs.stream().findFirst().orElse(null);
		return firstLeg != null ? firstLeg.getScheduledDeparture() : null;
	}

	@Transient
	public Date getFlightArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Date scheduledArrival = null;
		if (!listOfLegs.isEmpty())
			scheduledArrival = listOfLegs.get(listOfLegs.size() - 1).getScheduledArrival();
		return scheduledArrival;
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		return listOfLegs.size() - 1;
	}

	@Transient
	public Airport getDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Leg firstLeg = listOfLegs.stream().findFirst().orElse(null);
		return firstLeg != null ? firstLeg.getDepartureAirport() : null;
	}

	@Transient
	public Airport getArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Airport destination = null;
		if (!listOfLegs.isEmpty())
			destination = listOfLegs.get(listOfLegs.size() - 1).getArrivalAirport();
		return destination;
	}
}
