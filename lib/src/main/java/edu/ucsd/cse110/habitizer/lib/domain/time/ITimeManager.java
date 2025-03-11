package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public interface ITimeManager {
	/**
	 * Get the current time (in nanoseconds).
	 * @return
	 */
	public abstract HabitizerTime getCurrentTime();
}