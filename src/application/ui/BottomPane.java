package application.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import application.PropertieHelper;

public class BottomPane {
	
	private BorderPane parent;
	
	private PropertieHelper propertieHelper;
	private UIDataManager uiDataManager;
	
	public BottomPane(BorderPane parent, PropertieHelper propertieHelper, UIDataManager uiDataManager) {
		this.parent = parent;
		this.propertieHelper = propertieHelper;
		this.uiDataManager = uiDataManager;
	}
	
	public void init() {
		CheckBox groupTasks = new CheckBox("Group tasks");

		boolean grouping = Boolean.parseBoolean(propertieHelper.getProperty(PropertieHelper.GROUPING_KEY));
		groupTasks.setSelected(grouping);

		groupTasks.selectedProperty().addListener(
			(observableValue, oldVal, newVal) -> {
				propertieHelper.setProperty(PropertieHelper.GROUPING_KEY, newVal.toString());
				uiDataManager.enableGrouping(newVal);
				uiDataManager.applyGrouping();
			});

		parent.setBottom(groupTasks);
	}
	
}
