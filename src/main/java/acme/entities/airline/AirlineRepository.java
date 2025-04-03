
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirlineRepository extends AbstractRepository {

	@Query("select a from Airline a where a.id = :airlineId")
	Airline findAirlineById(int airlineId);

}
