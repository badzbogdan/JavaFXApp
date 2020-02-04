package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertiesHelper {
	
	private static final String PROPERTIES_PATH = TasklistViewer.APP_DIR_PATH + "/.properties";
	
	public static final String GROUPING_KEY = "grouping";
	private static final String GROUPING_BY_DEFAULT = "true";
	
	private Properties properties = new Properties();
	
	public void readProperties() {
		try (InputStream input = new FileInputStream(PROPERTIES_PATH)) {
			properties.load(input);
		} catch (FileNotFoundException e) {
			Log.LOGGER.info(e.getMessage());
		} catch (IOException e) {
			Log.LOGGER.error(e.getMessage(), e);
		}
		
		if (!properties.containsKey(GROUPING_KEY)) {
			properties.setProperty(GROUPING_KEY, GROUPING_BY_DEFAULT);
		}
	}
	
	public void writeProperties() {
		try (OutputStream output = new FileOutputStream(PROPERTIES_PATH)) {
			properties.store(output, StringUtils.EMPTY);
		} catch (IOException e) {
			Log.LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
}
