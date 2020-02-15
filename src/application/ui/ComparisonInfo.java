package application.ui;

import java.util.StringJoiner;

import data.model.Task;

public class ComparisonInfo {
	
	private String name;
	private String memusage;
	
	private ComparisonStatus status;
	
	ComparisonInfo() {
		this("", "", ComparisonStatus.EMPTY);
	}
	
	ComparisonInfo(Task task, ComparisonStatus status) {
		this(task.getName(), Long.toString(task.getMemusage()), status);
	}
	
	private ComparisonInfo(String name, String memusage, ComparisonStatus status) {
		this.name = name;
		this.memusage = memusage;
		this.status = status;
	}

	public String getName() {
		return name;
	}
	
	public String getMemusage() {
		return memusage;
	}
	
	public ComparisonStatus getStatus() {
		return status;
	}
	
	void setStatus(ComparisonStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return new StringJoiner(", ", "Task [", "]")
			.add("name=".concat(name))
			.add("memusage=".concat(memusage))
			.add("status=".concat(status.name()))
			.toString();
	}
	
}
