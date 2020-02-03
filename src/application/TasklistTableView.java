package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
import javafx.scene.layout.VBox;

public class TasklistTableView {
	
	public static final String IMAGE_NAME_COLUMN_TITLE = "Image Name";
	public static final String PID_COLUMN_TITLE = "PID";
	public static final String MEM_USAGE_COLUMN_TITLE = "Mem Usage";
	
	private VBox parent;
	
	private TaskReader reader = new TaskReader();
	private final ObservableList<Task> data = FXCollections.observableArrayList(new ArrayList<>());
	
	public TasklistTableView(VBox parent) {
		this.parent = parent;
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
		
		parent.getChildren().add(table);
	}
	
	public void clear() {
		data.clear();
	}
	
	public void update() {
		data.clear();
		try {
			data.addAll(reader.readTasks());
			Collections.sort(data);
		} catch (IOException e) {
			Log.LOGGER.error(e.getMessage(), e);
		}
	}
	
}
