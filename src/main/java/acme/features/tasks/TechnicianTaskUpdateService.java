package acme.features.tasks;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskUpdateService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean exist;
		Task task;
		Technician technician;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findById(id);

		exist = task != null;
		if (exist) {
			technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
			if (technician.equals(task.getTechnician()))
				super.getResponse().setAuthorised(true);
		}
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {

		if (!this.getBuffer().getErrors().hasErrors("type"))
			super.state(task.getType() != null, "type", "acme.validation.tasks.type.message", task);

		if (!this.getBuffer().getErrors().hasErrors("description") && task.getDescription() != null)
			super.state(task.getDescription().length() <= 255, "description", "acme.validation.tasks.description.message", task);

		if (!this.getBuffer().getErrors().hasErrors("priority") && task.getPriority() != null)
			super.state(0 <= task.getPriority() && task.getPriority() <= 10, "priority", "acme.validation.tasks.priority.message", task);

		if (!this.getBuffer().getErrors().hasErrors("estimatedDuration") && task.getDuration() != null)
			super.state(0 <= task.getDuration() && task.getDuration() <= 1000, "estimatedDuration", "acme.validation.tasks.duration.message", task);
	}

	@Override
	public void perform(final Task task) {
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;

		Dataset dataset;
		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		dataset.put("type", choices.getSelected().getKey());
		dataset.put("type", choices);

		super.getResponse().addData(dataset);
	}

}