package application.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import application.Log;
import application.PropertieHelper;
import application.dialog.ComparisonDialog;
import data.model.Task;
import data.util.FSReader;
import data.util.TasklistReader;

public class Toolbar {
	
	private BorderPane parent;
	
	private PropertieHelper propertieHelper;
	private UIDataManager uiDataManager;
	
	public Toolbar(BorderPane parent, PropertieHelper propertieHelper, UIDataManager uiDataManager) {
		this.parent = parent;
		this.propertieHelper = propertieHelper;
		this.uiDataManager = uiDataManager;
	}
	
	public void init() {
		ToolBar toolBar = new ToolBar();

		Button currentTasks = new Button("Current tasks");
		currentTasks.setOnAction(event -> {
			try {
				uiDataManager.update(new TasklistReader().readTasks());
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
			uiDataManager.clear();
			event.consume();
		});

		toolBar.getItems().add(clear);
		toolBar.getItems().add(new Separator());

		Button compareWith = new Button("Compare With...");
		compareWith.setOnAction(event -> {
			if (uiDataManager.isEmpty()) {
				showEmptyTasksDialog();
				return;
			}
			
			try {
				FileChooser fileChooser = prepareFileChooser("Open Resource File");
				File file = fileChooser.showOpenDialog(toolBar.getScene().getWindow());
				if (file != null) {
					propertieHelper.setProperty(
							PropertieHelper.RESOURCES_PATH_KEY,
							file.getParentFile().getPath());
					
					List<Task> current = uiDataManager.getTasks();
					List<Task> target = new FSReader(file).readTasks();
					
					ComparisonDataManager dataManager = new ComparisonDataManager(current, target);
					dataManager.prepareData();
					
					ComparisonDialog dialog = new ComparisonDialog(dataManager);
					dialog.open();
				}
			} catch (IOException e) {
				Log.LOGGER.error(e);
			}
		});

		toolBar.getItems().add(compareWith);

		parent.setTop(toolBar);
	}
	
	private void initOpenButton(ToolBar toolBar) {
		Button open = new Button("Open");
		open.setOnAction(event -> {
			try {
				FileChooser fileChooser = prepareFileChooser("Open Resource File");
				File file = fileChooser.showOpenDialog(toolBar.getScene().getWindow());
				if (file != null) {
					propertieHelper.setProperty(
							PropertieHelper.RESOURCES_PATH_KEY,
							file.getParentFile().getPath());
					uiDataManager.update(new FSReader(file).readTasks());
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
			List<Task> tasks = uiDataManager.getTasks();
			if (tasks.isEmpty()) {
				showEmptyTasksDialog();
				return;
			}

			FileChooser fileChooser = prepareFileChooser("Save Resource File");
			File file = fileChooser.showSaveDialog(toolBar.getScene().getWindow());
			try {
				if (file != null) {
					propertieHelper.setProperty(
							PropertieHelper.RESOURCES_PATH_KEY,
							file.getParentFile().getPath());
					new FSReader(file).writeTasks(uiDataManager.getTasks());
				}
			} catch (IOException e) {
				Log.LOGGER.error(e);
			}
			event.consume();
		});

		toolBar.getItems().add(save);
	}

	private FileChooser prepareFileChooser(String title) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.setInitialDirectory(new File(System
				.getProperty("user.home")));

		String resourcesPath = propertieHelper
				.getProperty(PropertieHelper.RESOURCES_PATH_KEY);
		if (StringUtils.isNotEmpty(resourcesPath)) {
			File resourcesDir = new File(resourcesPath);
			if (resourcesDir.exists()) {
				fileChooser.setInitialDirectory(resourcesDir);
			}
		}

		propertieHelper.setProperty(PropertieHelper.RESOURCES_PATH_KEY,
				fileChooser.getInitialDirectory().getPath());

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}
	
	private void showEmptyTasksDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Tasklist is empty");
		alert.showAndWait();
	}
	
}
