
package acme.entities.flight;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> legsByFlightId(int flightId);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	Collection<Flight> findFlightsByManagerId(int managerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findById(int id);
}
