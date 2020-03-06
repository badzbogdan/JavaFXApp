package application.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.ui.ComparisonDataManager;
import application.ui.ComparisonInfo;
import application.ui.TasklistTableView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ComparisonDialog {
	
	private ComparisonDataManager dataManager;

	private final ObservableList<ComparisonInfo> currentTasksData = FXCollections.observableArrayList(new ArrayList<>());
	private final ObservableList<ComparisonInfo> targetTasksData = FXCollections.observableArrayList(new ArrayList<>());

	private Dialog<?> dialog;

	public ComparisonDialog(ComparisonDataManager dataManager) {
		this.dataManager = dataManager;
		init();
	}

	private void init() {
		dialog = new Dialog<>();
		dialog.setTitle("Comparing Dialog");
		dialog.setResizable(true);

		TableView<ComparisonInfo> left = new TableView<>();
		TableView<ComparisonInfo> right = new TableView<>();

		initTable(left);
		initTable(right);
		
		fill(dataManager.getLeftData(), dataManager.getRightData());
		
		left.setItems(currentTasksData);
		right.setItems(targetTasksData);
		
		ScrollBar verScrollBar = new ScrollBar();
		verScrollBar.setOrientation(Orientation.VERTICAL);
		verScrollBar.setMax(dataManager.getSize());
		verScrollBar.setMin(0);
		verScrollBar.valueProperty().addListener((ChangeListener<Number>) (arg0, arg1, arg2) -> {
			left.scrollTo(arg1.intValue());
			right.scrollTo(arg1.intValue());
		});

		HBox hBox = new HBox(left, verScrollBar, right);
		HBox.setHgrow(left, Priority.ALWAYS);
		HBox.setHgrow(right, Priority.ALWAYS);

		dialog.getDialogPane().setContent(hBox);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
	}

	private void initTable(TableView<ComparisonInfo> table) {
		TableColumn<ComparisonInfo, Number> indexColumn = new TableColumn<>("#");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(
				table.getItems().indexOf(column.getValue())));
		
		TableColumn<ComparisonInfo, Object> imageNameCol = new TableColumn<>(
				TasklistTableView.IMAGE_NAME_COLUMN_TITLE);
		imageNameCol.prefWidthProperty().bind(table.widthProperty().divide(2));
		imageNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		imageNameCol.setCellFactory(column -> new CellImpl(table));

		TableColumn<ComparisonInfo, Object> usageMemoryCol = new TableColumn<>(
				TasklistTableView.MEM_USAGE_COLUMN_TITLE + " (KB)");
		usageMemoryCol.prefWidthProperty().bind(table.widthProperty().divide(2));
		usageMemoryCol.setCellValueFactory(new PropertyValueFactory<>("memUsage"));
		usageMemoryCol.setCellFactory(column -> new CellImpl(table));

		table.getColumns().setAll(Arrays.asList(indexColumn, imageNameCol, usageMemoryCol));
		table.getStylesheets().add("css/hide-vscroll.css");
		table.getStylesheets().add("css/disable-row-highlighting.css");
		table.addEventFilter(ScrollEvent.ANY, Event::consume);
	}
	
	private void fill(List<ComparisonInfo> current, List<ComparisonInfo> target) {
		currentTasksData.addAll(current);
		targetTasksData.addAll(target);
	}

	public void open() {
		dialog.showAndWait();
	}

}
