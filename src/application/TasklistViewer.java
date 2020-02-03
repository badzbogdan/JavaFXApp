package application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TasklistViewer extends Application {
	
	public static final String APP_DIR_PATH = System.getProperty("user.home") + "/.TasklistViewer";
	
	private TasklistTableView tableView;
	
	public static void main(String[] args) {
		launch(args);
//		test();
	}
	
	public static void test() {
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		
		initToolBar(root);
		
		tableView = new TasklistTableView(root);
		tableView.init();
		
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
	
	public void initToolBar(VBox parent) {
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
        
        parent.getChildren().add(toolBar);
	}

}
