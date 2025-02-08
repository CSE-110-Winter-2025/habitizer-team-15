package edu.ucsd.cse110.habitizer.lib.domain;

/**
 *
 */
public interface ITimeManager {
	/**
	 * Gets current time, depending on format of generic type
	 * @return T current time
	 */
	public TaskTime getCurrentTime();
}
