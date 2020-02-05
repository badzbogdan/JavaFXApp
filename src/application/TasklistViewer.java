package application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TasklistViewer extends Application {
	
	public static final String APP_DIR_PATH = System.getProperty("user.home") + "/.TasklistViewer";
	
	private PropertiesHelper helper = new PropertiesHelper();
	
	private TasklistTableView tableView;
	
	public static void main(String[] args) {
		launch(args);
//		test();
	}
	
	public static void test() {
		
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
        	tableView.update();
            event.consume();
        });
        
        toolBar.getItems().add(currentTasks);

        Button clear = new Button("Clear");
        clear.setOnAction(event -> {
        	tableView.clear();
            event.consume();
        });
        
        toolBar.getItems().add(clear);
        
        parent.setTop(toolBar);
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
