package lcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.model.Task;

/*
 * Longest Common Subsequence, Needleman-Wunsch algorithm implementation
 */
public class LCSImpl {
	
	private LCSImpl() {}
	
	private static int[][] fillTracebackMatr(List<Task> current, List<Task> target) {
		int[][] L = new int[current.size() + 1][target.size() + 1];
		for (int i = 1; i < L.length; i++) {
			for (int j = 1; j < L[i].length; j++) {
				if (Task.equals(current.get(i - 1), target.get(j - 1))) {
					L[i][j] = L[i - 1][j - 1] + 1;
				} else {
					L[i][j] = Math.max(L[i][j - 1], L[i - 1][j]);
				}
			}
		}
		return L;
	}
	
	/*
	 * May be used to implementation more detailed showing of comparison results
	 */
	private static int[][] fillTracebackMatr(String str1, String str2) {
		int[][] L = new int[str1.length() + 1][str2.length() + 1];
		for (int i = 1; i < L.length; i++) {
			for (int j = 1; j < L[i].length; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					L[i][j] = L[i - 1][j - 1] + 1;
				} else {
					L[i][j] = Math.max(L[i][j - 1], L[i - 1][j]);
				}
			}
		}
		return L;
	}
	
	public static List<Task> getLCS(List<Task> current, List<Task> target) {
		int[][] L = fillTracebackMatr(current, target);
		return getLCS(current, target, L);
	}
	
	private static List<Task> getLCS(List<Task> current, List<Task> target, int[][] L) {
		List<Task> lcs = new ArrayList<>();
		int indexOfCurrent = current.size() - 1;
		int indexOfTarget = target.size() - 1;
		while (indexOfCurrent >= 0 && indexOfTarget >= 0) {
			Task currentTask = current.get(indexOfCurrent);
			Task targetTask = target.get(indexOfTarget);
			if (Task.equals(currentTask, targetTask)) {
				lcs.add(currentTask);
				indexOfCurrent--;
				indexOfTarget--;
			} else if (L[indexOfCurrent][indexOfTarget + 1] > L[indexOfCurrent + 1][indexOfTarget]) {
				indexOfCurrent--;
			} else {
				indexOfTarget--;
			}
		}
		Collections.reverse(lcs);
		return lcs;
	}
	
	/*
	 * May be used to implementation more detailed showing of comparison results
	 */
	public static String getLCS(String str1, String str2) {
		int[][] L = fillTracebackMatr(str1, str2);
		return getLCS(str1, str2, L);
	}
	
	private static String getLCS(String str1, String str2, int[][] L) {
		StringBuilder lcs = new StringBuilder();
		int indexStr1 = str1.length() - 1;
		int indexStr2 = str2.length() - 1;
		while (indexStr1 >= 0 && indexStr2 >= 0) {
			char c1 = str1.charAt(indexStr1);
			char c2 = str2.charAt(indexStr2);
			if (c1 == c2) {
				lcs.append(c1);
				indexStr1--;
				indexStr2--;
			} else if (L[indexStr1][indexStr2 + 1] > L[indexStr1 + 1][indexStr2]) {
				indexStr1--;
			} else {
				indexStr2--;
			}
		}
		return lcs.reverse().toString();
	}
	
	/*
	 *  May be used for optimization of comparison
	 */
	public static int longestBeginCommonSubsequenceSize(
			List<Task> current, List<Task> target) {
		int result = 0;
		for (int i = 0; (i < current.size() && i < target.size()); i++) {
			if (Task.equals(current.get(i), target.get(i))) {
				result++;
			} else {
				break;
			}
		}
		return result;
	}
	
	/*
	 *  May be used for optimization of comparison
	 */
	public static int longestEndCommonSubsequenceSize(
			List<Task> current, List<Task> target) {
		int result = 0;
		int currentLastIndex = current.size() - 1;
		int targetLastIndex = target.size() - 1;
		for (; (currentLastIndex >= 0 || targetLastIndex >= 0); currentLastIndex--, targetLastIndex--) {
			if (Task.equals(current.get(currentLastIndex), target.get(targetLastIndex))) {
				result++;
			} else {
				break;
			}
		}
		return result;
	}
	
}
