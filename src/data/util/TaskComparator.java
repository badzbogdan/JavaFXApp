package data.util;

import java.util.Comparator;

public class TaskComparator implements Comparator<Long> {
	
	@Override
	public int compare(Long memusage1, Long memusage2) {
		return memusage1.compareTo(memusage2);
	}

}
