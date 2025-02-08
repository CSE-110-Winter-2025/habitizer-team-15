package edu.ucsd.cse110.habitizer.lib.domain;

import edu.ucsd.cse110.habitizer.lib.data.TaskTime;

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
