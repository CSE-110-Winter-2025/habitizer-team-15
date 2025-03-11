package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public abstract class TimeManager {
	/**
	 * Get the current time (in nanoseconds).
	 * @return
	 */
	public abstract HabitizerTime getCurrentTime();
}