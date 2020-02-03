package data.model;

import java.util.StringJoiner;

public class Task implements Comparable<Task> {
	
	private String name;
	private transient String pid;
	
	/**
	 * Memory usage in KB
	 * @see <a href="https://docs.microsoft.com/en-us/windows-server/administration/windows-commands/tasklist">
	 *    Microsoft Documentation/Commands by ServerRole/Tasklist
	 * </a>
	 */
	private long memusage;
	
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
	public int compareTo(Task t) {
		return Long.compare(memusage, t.memusage);
	}
	
}
