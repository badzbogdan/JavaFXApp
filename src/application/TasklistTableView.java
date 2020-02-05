package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import java.util.function.Function;
import java.util.stream.Collectors;

import data.model.Task;
import data.util.SizeUnit;
import data.util.TaskComparator;
import data.util.TaskReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class TasklistTableView {
	
	public static final String IMAGE_NAME_COLUMN_TITLE = "Image Name";
	public static final String PID_COLUMN_TITLE = "PID";
	public static final String MEM_USAGE_COLUMN_TITLE = "Mem Usage";
	
	private BorderPane parent;
	
	private TaskReader reader = new TaskReader();
	private List<Task> tasks = new ArrayList<>();
	private final ObservableList<Task> data = FXCollections.observableArrayList(new ArrayList<>());
	
	private boolean grouping;
	
	public TasklistTableView(BorderPane parent, boolean grouping) {
		this.parent = parent;
		this.grouping = grouping;
	}
	
	public void init() {
		TableView<Task> table = new TableView<>();
		
		TableColumn<Task, String> imageNameCol = new TableColumn<>(IMAGE_NAME_COLUMN_TITLE);
		imageNameCol.prefWidthProperty().bind(table.widthProperty().divide(2));
		imageNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Task, String> pidCol = new TableColumn<>(PID_COLUMN_TITLE);
		pidCol.prefWidthProperty().bind(table.widthProperty().divide(5));
		pidCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		pidCol.setCellValueFactory(new PropertyValueFactory<>("pid"));
		
		TableColumn<Task, Long> usageMemoryCol = new TableColumn<>(MEM_USAGE_COLUMN_TITLE);
		usageMemoryCol.prefWidthProperty().bind(table.widthProperty().divide(5));
		usageMemoryCol.setStyle("-fx-alignment: CENTER-RIGHT;");
		usageMemoryCol.setCellValueFactory(new PropertyValueFactory<>("memusage"));
		usageMemoryCol.setCellFactory(param -> new TableCell<Task, Long>() {
			@Override
			public void updateItem(Long item, boolean empty) {
				if (item != null) {
					setText(SizeUnit.KB.convertToString(item, SizeUnit.MB).concat(" MB"));
				}
			}
		});
		usageMemoryCol.setComparator(new TaskComparator());
		
		table.setItems(data);
		table.getColumns().setAll(Arrays.asList(imageNameCol, pidCol, usageMemoryCol));
		
		parent.setCenter(table);
	}
	
	public void clear() {
		tasks.clear();
		data.clear();
	}
	
	public void update() {
		tasks.clear();
		try {
			tasks.addAll(reader.readTasks());
		} catch (IOException e) {
			Log.LOGGER.error(e.getMessage(), e);
		}
		applyGrouping();
	}
	
	private void update(Collection<Task> tasks) {
		data.clear();
		data.addAll(tasks);
		Collections.sort(data);
	}
	
	public void enableGrouping(boolean val) {
		grouping = val;
	}
	
	public void applyGrouping() {
		Collection<Task> preparedTasks = tasks;
		if (grouping) {
			preparedTasks = getGroupedTasks();
		}
		update(preparedTasks);
	}
	
	private Collection<Task> getGroupedTasks() {
		Map<Task, Long> duplicateElementCount = tasks.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()));
		
		Map<Task, Long> memusageSumDuplicatedElements = tasks.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.mapping(Task::getMemusage, Collectors.reducing(0L, Long::sum))));
		
		List<Task> preparedTasks = new ArrayList<>();
		String namePattern = "%s (%s)";
		duplicateElementCount.forEach((key, value) -> {
			String name = (value > 1) ?
					String.format(namePattern, key.getName(), value) : key.getName();
			long aggregatedMemusage = memusageSumDuplicatedElements.get(key);
			preparedTasks.add(new Task(name, key.getPid(), aggregatedMemusage));
		});
		
		return preparedTasks;
	}
	
}
