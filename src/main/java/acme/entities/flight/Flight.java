
package acme.entities.flight;

import java.beans.Transient;
import java.time.LocalDateTime;
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
	public LocalDateTime getScheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<LocalDateTime> results = repository.findScheduledDepartureByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public LocalDateTime getScheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<LocalDateTime> results = repository.findScheduledArrivalByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public String getOriginCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<String> results = repository.findOriginCityByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<String> results = repository.findDestinationCityByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public Integer getLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLayoversByFlightId(this.getId());
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
