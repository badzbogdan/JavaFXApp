package application;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class Log {
	
	private Log() {}
	
	public static final Logger LOGGER = Logger.getLogger(Log.class);
	static {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		
		PatternLayout layout = new PatternLayout(
				"%d{ISO8601} [%t] %-5p %c %x - %m%n");
		rootLogger.addAppender(new ConsoleAppender(layout));

		try {
			RollingFileAppender fileAppender = new RollingFileAppender(layout,
					TasklistViewer.APP_DIR_PATH + "/.log");
			rootLogger.addAppender(fileAppender);
		} catch (IOException e) {
			System.err.println("Failed to add appender");
		}
	}
	
}
