package application.ui;

import java.util.StringJoiner;

import data.model.Task;

public class ComparisonInfo {
	
	private String name;
	private String memUsage;
	
	private ComparisonStatus status;
	
	ComparisonInfo() {
		this("", "", ComparisonStatus.EMPTY);
	}
	
	ComparisonInfo(Task task, ComparisonStatus status) {
		this(task.getName(), Long.toString(task.getMemUsage()), status);
	}
	
	private ComparisonInfo(String name, String memUsage, ComparisonStatus status) {
		this.name = name;
		this.memUsage = memUsage;
		this.status = status;
	}

	public String getName() {
		return name;
	}
	
	public String getMemUsage() {
		return memUsage;
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
			.add("memusage=".concat(memUsage))
			.add("status=".concat(status.name()))
			.toString();
	}
	
}
