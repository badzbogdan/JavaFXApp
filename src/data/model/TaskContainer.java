package data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskContainer {
	
	@XmlElement(name = "task")
	private List<Task> tasks = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private TaskContainer() {
		/* The empty constructor necessary for JAXB only */
	}
	
	public TaskContainer(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public List<Task> getTasks() {
		return Collections.unmodifiableList(tasks);
	}
	
	public void addAll(List<Task> tasks) {
		this.tasks.addAll(tasks);
	}
	
	public void clear() {
		tasks.clear();
	}
	
}
