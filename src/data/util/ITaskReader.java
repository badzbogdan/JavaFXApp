package data.util;

import java.io.IOException;
import java.util.List;

import data.model.Task;

public interface ITaskReader {
	
	List<Task> readTasks() throws IOException ;
	void writeTasks(List<Task> tasks) throws IOException;
	
}
