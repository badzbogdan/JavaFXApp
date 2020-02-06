package data.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import data.model.Task;
import data.model.TaskContainer;

public class FSReader implements ITaskReader {
	
	private File file;
	
	public FSReader(File file) {
		this.file = file;
	}
	
	@Override
	public List<Task> readTasks() throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TaskContainer.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			TaskContainer taskContainer = (TaskContainer) jaxbUnmarshaller
					.unmarshal(file);
			
			return taskContainer.getTasks();
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void writeTasks(List<Task> tasks) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TaskContainer.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(new TaskContainer(tasks), file);
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}

}
