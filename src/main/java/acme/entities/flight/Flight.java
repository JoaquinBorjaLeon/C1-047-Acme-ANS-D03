
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
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.legs.Leg;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
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

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Optional
	@Size(max = 255)
	@Automapped
	private String				description;


	@Transient
	public Date getFlightDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Date scheduledDeparture = null;
		if (!listOfLegs.isEmpty()) {
			Leg firstLeg = null;
			for (Leg leg : listOfLegs)
				if (firstLeg == null || leg.getScheduledDeparture().before(firstLeg.getScheduledDeparture()))
					firstLeg = leg;
			if (firstLeg != null)
				scheduledDeparture = firstLeg.getScheduledDeparture();
		}
		return scheduledDeparture;
	}

	@Transient
	public Date getFlightArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Date scheduledArrival = null;
		if (!listOfLegs.isEmpty()) {
			Leg lastLeg = null;
			for (Leg leg : listOfLegs)
				if (lastLeg == null || leg.getScheduledArrival().after(lastLeg.getScheduledArrival()))
					lastLeg = leg;
			if (lastLeg != null)
				scheduledArrival = lastLeg.getScheduledArrival();
		}
		return scheduledArrival;
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		return listOfLegs.size() - 2;
	}

	@Transient
	public Airport getDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> listOfLegs = repository.legsDuringFlight(this.getId());
		Leg firstLegs = listOfLegs.stream().findFirst().orElse(null);
		return firstLegs != null ? firstLegs.getDepartureAirport() : null;
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
