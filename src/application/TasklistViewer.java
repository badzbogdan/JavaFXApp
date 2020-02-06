package application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import data.model.Task;
import data.util.FSReader;
import data.util.TasklistReader;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TasklistViewer extends Application {
	
	public static final String APP_DIR_PATH = System.getProperty("user.home") + "/.TasklistViewer";
	
	private PropertiesHelper helper = new PropertiesHelper();
	
	private TasklistTableView tableView;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		helper.readProperties();
		
		BorderPane root = new BorderPane();
		initToolBar(root);
		
		boolean grouping = Boolean.parseBoolean(helper.getProperty(PropertiesHelper.GROUPING_KEY));
		tableView = new TasklistTableView(root, grouping);
		tableView.init();
		
		initAdditionalControls(root);
		
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		double width = bounds.getWidth() - bounds.getWidth() * 0.5;
		double height = bounds.getHeight() - bounds.getHeight() * 0.5;
		Scene scene = new Scene(root, width, height);
		primaryStage.setTitle("Tasklist Viewer");
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		double x = (bounds.getWidth() - primaryStage.getWidth()) / 2;
		double y = (bounds.getHeight() - primaryStage.getHeight()) / 2;
		primaryStage.setX(x);
        primaryStage.setY(y);
	}
	
	@Override
	public void stop() {
		helper.writeProperties();
	}
	
	private void initToolBar(BorderPane parent) {
		ToolBar toolBar = new ToolBar();

        Button currentTasks = new Button("Current tasks");
        currentTasks.setOnAction(event -> {
        	try {
				tableView.update(new TasklistReader().readTasks());
			} catch (IOException e) {
				Log.LOGGER.error(e);
			}
            event.consume();
        });
        
        toolBar.getItems().add(currentTasks);
        toolBar.getItems().add(new Separator());
        
        initOpenButton(toolBar);
        initSaveButton(toolBar);
        toolBar.getItems().add(new Separator());

        Button clear = new Button("Clear");
        clear.setOnAction(event -> {
        	tableView.clear();
            event.consume();
        });
        
        toolBar.getItems().add(clear);
        
        parent.setTop(toolBar);
	}
	
	private void initOpenButton(ToolBar toolBar) {
		Button open = new Button("Open");
        open.setOnAction(event -> {
        	try {
        		FileChooser fileChooser = prepareFileChooser();
        		File file = fileChooser.showOpenDialog(toolBar.getScene().getWindow());
        		if (file != null) {
        			tableView.update(new FSReader(file).readTasks());
        		}
			} catch (IOException e) {
				Log.LOGGER.error(e);
			}
            event.consume();
        });
        
        toolBar.getItems().add(open);
	}
	
	private void initSaveButton(ToolBar toolBar) {
		Button save = new Button("Save");
        save.setOnAction(event -> {
        	List<Task> tasks = tableView.getTasks();
        	if (tasks.isEmpty()) {
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("Tasklist is empty");
        		alert.showAndWait();
        		return;
        	}
        	
        	FileChooser fileChooser = prepareFileChooser();
    		File file = fileChooser.showSaveDialog(toolBar.getScene().getWindow());
        	try {
        		if (file != null) {
        			new FSReader(file).writeTasks(tableView.getTasks());
        		}
			} catch (IOException e) {
				Log.LOGGER.error(e);
			}
            event.consume();
        });
        
        toolBar.getItems().add(save);
	}
	
	private FileChooser prepareFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}
	
	private void initAdditionalControls(BorderPane parent) {
		CheckBox groupTasks = new CheckBox("Group tasks");
		
		boolean grouping = Boolean.parseBoolean(helper.getProperty(PropertiesHelper.GROUPING_KEY));
		groupTasks.setSelected(grouping);
		
		groupTasks.selectedProperty().addListener(
				(observableValue, oldVal, newVal) -> {
					helper.setProperty(PropertiesHelper.GROUPING_KEY, newVal.toString());
					tableView.enableGrouping(newVal);
					tableView.applyGrouping();
				});
		
		parent.setBottom(groupTasks);
	}

}
