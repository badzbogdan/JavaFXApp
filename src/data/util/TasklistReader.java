package data.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.IOUtils;

import application.Log;
import application.TasklistTableView;
import data.model.Task;

public class TasklistReader implements ITaskReader {
	
	private static final String SYS32_PATH = System.getenv("windir") + "/system32";
	private static final String CHCP_APP = SYS32_PATH + "/chcp.com";
	private static final String TASKLIST_APP = SYS32_PATH + "/tasklist.exe";
	
	private static final String FO_PARAMETER = "/fo";
	private static final String FO_CSV_ARGUMENT = "csv";
	
	private static final String KB_SUFFIX = " K";
	
	@Override
	public List<Task> readTasks() throws IOException {
		String[] commands = {
			TASKLIST_APP,
			FO_PARAMETER,
			FO_CSV_ARGUMENT
		};
		
		// Try to get system charset or use default charset if not found
		Charset charset = Optional.ofNullable(getSystemCharset()).orElse(Charset.defaultCharset());
		
		Process p = Runtime.getRuntime().exec(commands);
		String content = IOUtils.toString(
				new BufferedInputStream(p.getInputStream()), charset);
		
		return parse(content);
	}
	
	@Override
	public void writeTasks(List<Task> tasks) {
		String message = "Using the system utility \"TaskList\" we can only read";
		RuntimeException e = new UnsupportedOperationException(message);
		Log.LOGGER.error(e);
		throw e;
	}
	
	private Charset getSystemCharset() throws IOException {
		Process p = Runtime.getRuntime().exec(CHCP_APP);
		String windowsCodePage;
		String pattern = ".*:"; // skip 'Active code page:' for chcp output
		try (Reader reader = new InputStreamReader(p.getInputStream())) {
			windowsCodePage = new Scanner(reader).skip(pattern).next();
		}

		Charset charset = null;
		String[] charsetPrefixes = new String[] { "", "windows-", "x-windows-", "IBM", "x-IBM" };
		for (String charsetPrefix : charsetPrefixes) {
			try {
				charset = Charset.forName(charsetPrefix + windowsCodePage);
			} catch (UnsupportedCharsetException e) {
				break;
			}
		}

		return charset;
	}

	private List<Task> parse(String content) throws IOException {
		CSVParser parser = CSVParser.parse(content, CSVFormat.RFC4180.withQuote('"').withHeader());
		return parser.getRecords().stream().collect(Collectors.mapping(record -> {
			String imageName = record.get(TasklistTableView.IMAGE_NAME_COLUMN_TITLE);
			String pid = record.get(TasklistTableView.PID_COLUMN_TITLE);
			String memUsage = record.get(TasklistTableView.MEM_USAGE_COLUMN_TITLE);
			return new Task(imageName, pid, parseMemUsage(memUsage));
		}, Collectors.toList()));
	}
	
	private long parseMemUsage(String memUsageStr) {
		String memUsage;
		if (memUsageStr.endsWith(KB_SUFFIX)) {
			int endIndex = memUsageStr.length() - KB_SUFFIX.length();
			memUsage = memUsageStr.substring(0, endIndex);
		} else {
			memUsage = memUsageStr;
		}
		
		String memUsageWithoutSpaces = memUsage.replace(" ", "");
		
		Number number = 0;
		try {
			number = NumberFormat.getNumberInstance().parse(memUsageWithoutSpaces);
		} catch (ParseException e) {
			Log.LOGGER.error(e.getMessage(), e);
		}
		
		return number.longValue();
	}
	
}
