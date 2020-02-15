package application.ui;

import java.util.Arrays;

import data.model.Task;
import data.util.SizeUnit;
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
	
	public TasklistTableView(BorderPane parent) {
		this.parent = parent;
	}
	
	public void init(ObservableList<Task> data) {
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
		usageMemoryCol.setComparator((memusage1, memusage2) -> memusage1.compareTo(memusage2));
		
		table.setItems(data);
		table.getColumns().setAll(Arrays.asList(imageNameCol, pidCol, usageMemoryCol));
		
		parent.setCenter(table);
	}
	
}
