
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
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;
import acme.realms.airlinemanager.AirlineManager;
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

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

	@Optional
	@Size(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager		manager;


	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstScheduledDeparture(this.getId()).orElse(null);
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastScheduledArrival(this.getId()).orElse(null);
	}

	@Transient
	public String getOriginCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstOriginCity(this.getId()).orElse("");
	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastDestinationCity(this.getId()).orElse("");
	}

	@Transient
	public Integer getNumberOfLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.numberOfLayovers(this.getId());
	}

	@Transient
	public boolean isDraftMode() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsByFlightId(this.getId());

		if (legs.isEmpty())
			return false;

		return legs.stream().anyMatch(l -> Boolean.TRUE.equals(l.getDraftMode()));
	}

}
