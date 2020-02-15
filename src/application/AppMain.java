package application;

import application.ui.BottomPane;
import application.ui.TasklistTableView;
import application.ui.Toolbar;
import application.ui.UIDataManager;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AppMain extends Application {

	public static final String APP_DIR_PATH = System.getProperty("user.home") + "/.TasklistViewer";
	
	private PropertieHelper propertieHelper;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		propertieHelper = new PropertieHelper();
		propertieHelper.readProperties();
		
		boolean grouping = Boolean.parseBoolean(
				propertieHelper.getProperty(PropertieHelper.GROUPING_KEY));
		UIDataManager uiDataManager = new UIDataManager(grouping);
		
		BorderPane root = new BorderPane();
		
		Toolbar toolbar = new Toolbar(root, propertieHelper, uiDataManager);
		toolbar.init();
		
		TasklistTableView tableView = new TasklistTableView(root);
		tableView.init(uiDataManager.getData());
		
		BottomPane bottomPane = new BottomPane(root, propertieHelper, uiDataManager);
		bottomPane.init();

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
		propertieHelper.writeProperties();
	}

}
