
package acme.entities.tasks;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecordTask extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

//	TO-DO: uncomment this when/if MaintenanceRecord class is accepted
//	@ManyToOne
//	@Mandatory
//	@Automapped
//	private MaintenanceRecord MaintenanceRecord;
	
	@ManyToOne
	@Mandatory
	@Automapped
	private Task task;
	
}
