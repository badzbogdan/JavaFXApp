package application.dialog;

import java.util.StringJoiner;

import application.ui.ComparisonInfo;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

class CellImpl extends TableCell<ComparisonInfo, Object> {
	
	private TableView<ComparisonInfo> table;
	
	CellImpl(TableView<ComparisonInfo> table) {
		this.table = table;
	}
	
	@Override
    public void updateIndex(int index) {
        super.updateIndex(index);
        
        ObservableList<ComparisonInfo> items = table.getItems();
        if (items.isEmpty() || index < 0 || index >= items.size()) {
        	return;
        }
        
        ComparisonInfo info = items.get(index);
        int r, g, b;
        switch (info.getStatus()) {
            case EQUAL:
            	// light green
            	r = 0xEB;
            	g = 0xF4;
            	b = 0xEB;
            	break;
            case NOT_EQUAL:
            	// light red
            	r = 0xF6;
            	g = 0xE5;
            	b = 0xE6;
            	break;
            case EMPTY:
            	// empty color
            	r = 0xB8;
            	g = 0xCF;
            	b = 0xE5;
            	break;
        	default:
        		// white
        		r = 0xFF;
        		g = 0xFF;
        		b = 0xFF;
        }
        
        StringJoiner css = new StringJoiner(",", "-fx-background-color: rgb(", "); ");
        css.add(Integer.toString(r));
        css.add(Integer.toString(g));
        css.add(Integer.toString(b));
		setStyle(css.toString());
    }
	
	@Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty || item == null) {
            return;
        }
        
        setText(item.toString());
    }
	
}
