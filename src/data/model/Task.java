package data.model;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Comparable<Task> {
	
	private String name;
	
	@XmlTransient
	private String pid = StringUtils.EMPTY;
	
	/**
	 * Memory usage in KB
	 * @see <a href="https://docs.microsoft.com/en-us/windows-server/administration/windows-commands/tasklist">
	 *    Microsoft Documentation/Commands by ServerRole/Tasklist
	 * </a>
	 */
	private long memusage;
	
	@SuppressWarnings("unused")
	private Task() {
		/* The empty constructor necessary for JAXB only */
	}

	public Task(String name, String pid, long memusage) {
		this.name = name;
		this.pid = pid;
		this.memusage = memusage;
	}

	public String getName() {
		return name;
	}
	
	public String getPid() {
		return pid;
	}

	public long getMemusage() {
		return memusage;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", "Task [", "]")
			.add("name=".concat(name))
			.add("pid=".concat(pid))
			.add("memusage=".concat(String.valueOf(memusage)))
			.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Task t) {
		return Long.compare(memusage, t.memusage);
	}
	
}
