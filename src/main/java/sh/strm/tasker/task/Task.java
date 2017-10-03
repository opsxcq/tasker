package sh.strm.tasker.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sh.strm.tasker.notify.Notifier;

public abstract class Task {

	private String name;

	// Internal configuration variables

	private List<Notifier> notifiers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addNotifier(Notifier notifier) {
		if (this.notifiers == null) {
			this.cleanNotifiers();
		}
		this.notifiers.add(notifier);
	}

	public List<Notifier> getNotifiers() {
		if (this.notifiers == null) {
			this.cleanNotifiers();
		}
		return Collections.unmodifiableList(this.notifiers);
	}

	public void cleanNotifiers() {
		this.notifiers = new ArrayList<Notifier>();
	}

}
