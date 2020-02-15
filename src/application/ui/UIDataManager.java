package application.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import data.model.Task;

public class UIDataManager {
	
	private boolean grouping;
	
	private List<Task> tasks = new ArrayList<>();
	private final ObservableList<Task> data = FXCollections.observableArrayList(new ArrayList<>());
	
	public UIDataManager(boolean grouping) {
		this.grouping = grouping;
	}
	
	public List<Task> getTasks() {
		return Collections.unmodifiableList(tasks);
	}
	
	public ObservableList<Task> getData() {
		return data;
	}
	
	public boolean isEmpty() {
		return tasks.isEmpty();
	}
	
	public void clear() {
		tasks.clear();
		data.clear();
	}
	
	public void update(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		applyGrouping();
	}
	
	private void updateData(List<Task> tasks) {
		data.clear();
		data.addAll(tasks);
		Collections.sort(data);
	}
	
	public void enableGrouping(boolean enable) {
		grouping = enable;
	}
	
	public void applyGrouping() {
		List<Task> preparedTasks = tasks;
		if (grouping) {
			preparedTasks = getGroupedTasks();
		}
		updateData(preparedTasks);
	}
	
	private List<Task> getGroupedTasks() {
		Map<Task, Long> duplicatedElementCount = tasks.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()));
		
		Map<Task, Long> memusageSumOfDuplicatedElements = tasks.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.mapping(Task::getMemusage, Collectors.reducing(0L, Long::sum))));
		
		List<Task> preparedTasks = new ArrayList<>();
		String namePattern = "%s (%s)";
		duplicatedElementCount.forEach((key, value) -> {
			String name = (value > 1) ?
					String.format(namePattern, key.getName(), value) : key.getName();
			long aggregatedMemusage = memusageSumOfDuplicatedElements.get(key);
			preparedTasks.add(new Task(name, key.getPid(), aggregatedMemusage));
		});
		
		return preparedTasks;
	}

}
