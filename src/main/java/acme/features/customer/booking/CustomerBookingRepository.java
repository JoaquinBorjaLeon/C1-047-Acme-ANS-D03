
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.Passenger;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select distinct br.passenger from BookingRecord br where br.booking.id in (select b.id from Booking b where b.customer.id = :customerId)")
	public Collection<Passenger> findPassengerByCustomerBookings(int customerId);

	@Query("select br.passenger from BookingRecord br where br.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBooking(int bookingId);

	@Query("select br.passenger from BookingRecord br where br.booking.id = :bookingId and br.passenger.draftMode = true")
	Collection<Passenger> findPassengersInDraftMode(int bookingId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

}
