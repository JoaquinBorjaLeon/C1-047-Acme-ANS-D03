
package acme.features.flightcrewmember.flightassignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightassignment.FlightAssignment;
import acme.entities.legs.LegType;

@Repository
public interface CrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.leg.status = ?1 and f.allocatedFlightCrewMember.id = ?2")
	Collection<FlightAssignment> assignmentsLandedLegs(LegType legStatus, Integer member);

	@Query("select f from FlightAssignment f where f.leg.status in ?1 and f.allocatedFlightCrewMember.id = ?2")
	Collection<FlightAssignment> assignmentsPlannedLegs(Collection<LegType> legStatuses, Integer member);

	@Query("select f from FlightAssignment f where f.id = ?1")
	FlightAssignment findFlightAssignmentById(int id);
}
