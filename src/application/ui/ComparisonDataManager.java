package application.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lcs.LCSImpl;
import data.model.Task;

public class ComparisonDataManager {
	
	private List<Task> currentTasks;
	private List<Task> targetTasks;
	
	private List<ComparisonInfo> left = new ArrayList<>();
	private List<ComparisonInfo> right = new ArrayList<>();
	
	public ComparisonDataManager(List<Task> currentTasks, List<Task> targetTasks) {
		this.currentTasks = new ArrayList<>(currentTasks);
		this.targetTasks = new ArrayList<>(targetTasks);
	}
	
	public void prepareData() {
		Comparator<Task> comparator = (task1, task2) -> task1.getName().compareTo(task2.getName());
		Collections.sort(currentTasks, comparator);
		Collections.sort(targetTasks, comparator);
		
		List<Task> lcs = LCSImpl.getLCS(currentTasks, targetTasks);
		int currentIndex = 0;
		int targetIndex = 0;
		for (Task lcsTask : lcs) {
			Task currentTask = (currentIndex < currentTasks.size()) ? currentTasks.get(currentIndex) : null;
			Task targetTask = (targetIndex < targetTasks.size()) ? targetTasks.get(targetIndex) : null;
			while (!Task.equals(currentTask, lcsTask) || !Task.equals(targetTask, lcsTask)) {
				if (Task.equals(currentTask, lcsTask)) {
					left.add(new ComparisonInfo());
					ComparisonInfo rightInfo = (targetTask == null) ? new ComparisonInfo()
							: new ComparisonInfo(targetTask, ComparisonStatus.NOT_EQUAL);
					right.add(rightInfo);
					targetIndex++;
					targetTask = (targetIndex < targetTasks.size()) ? targetTasks.get(targetIndex) : null;
				} else if (Task.equals(targetTask, lcsTask)) {
					ComparisonInfo leftInfo = (currentTask == null) ? new ComparisonInfo()
							: new ComparisonInfo(currentTask, ComparisonStatus.NOT_EQUAL);
					left.add(leftInfo);
					right.add(new ComparisonInfo());
					currentIndex++;
					currentTask = (currentIndex < currentTasks.size()) ? currentTasks.get(currentIndex) : null;
				} else {
					ComparisonInfo leftInfo = (currentTask == null) ? new ComparisonInfo()
							: new ComparisonInfo(currentTask, ComparisonStatus.NOT_EQUAL);
					ComparisonInfo rightInfo = (targetTask == null) ? new ComparisonInfo()
							: new ComparisonInfo(targetTask, ComparisonStatus.NOT_EQUAL);
					left.add(leftInfo);
					right.add(rightInfo);
					currentIndex++;
					targetIndex++;
					currentTask = (currentIndex < currentTasks.size()) ? currentTasks.get(currentIndex) : null;
					targetTask = (targetIndex < targetTasks.size()) ? targetTasks.get(targetIndex) : null;
				}
			}
			
			ComparisonInfo lcsInfo = new ComparisonInfo(lcsTask, ComparisonStatus.EQUAL);
			left.add(lcsInfo);
			right.add(lcsInfo);
			currentIndex++;
			targetIndex++;
		}
		
		for (; currentIndex < currentTasks.size(); currentIndex++, targetIndex++) {
			Task currentTask = currentTasks.get(currentIndex);
			Task targetTask = (targetIndex < targetTasks.size()) ? targetTasks.get(targetIndex) : null;
			
			ComparisonStatus status = Task.equals(currentTask, targetTask) ? ComparisonStatus.EQUAL
					: ComparisonStatus.NOT_EQUAL;
			
			ComparisonInfo leftInfo = new ComparisonInfo(currentTask, status);
			ComparisonInfo rightInfo = (targetIndex >= targetTasks.size()) ? new ComparisonInfo()
					: new ComparisonInfo(targetTask, status);
			
			left.add(leftInfo);
			right.add(rightInfo);
		}
		
		for (; targetIndex < targetTasks.size(); currentIndex++, targetIndex++) {
			Task currentTask = (currentIndex < currentTasks.size()) ? currentTasks.get(currentIndex) : null;
			Task targetTask = targetTasks.get(targetIndex);
			
			ComparisonStatus status = Task.equals(currentTask, targetTask) ? ComparisonStatus.EQUAL
					: ComparisonStatus.NOT_EQUAL;
			
			ComparisonInfo leftInfo = (currentIndex >= currentTasks.size()) ? new ComparisonInfo()
					: new ComparisonInfo(currentTask, status);
			ComparisonInfo rightInfo = new ComparisonInfo(targetTask, status);
			
			left.add(leftInfo);
			right.add(rightInfo);
		}
	}
	
	public List<ComparisonInfo> getLeftData() {
		return Collections.unmodifiableList(left);
	}
	
	public List<ComparisonInfo> getRightData() {
		return Collections.unmodifiableList(right);
	}
	
	/**
	 * It doesn’t matter from which list to take the size
	 * Both lists will be the same size
	 */
	public int getSize() {
		return left.size();
	}
	
}
