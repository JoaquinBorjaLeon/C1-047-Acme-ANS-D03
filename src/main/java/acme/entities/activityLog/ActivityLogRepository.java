
package acme.entities.activityLog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("SELECT FA.leg FROM ActivityLog AL JOIN AL.reporter FA WHERE AL.id = :activityLogId")
	Leg findLegByActivityLogId(int activityLogId);
}
