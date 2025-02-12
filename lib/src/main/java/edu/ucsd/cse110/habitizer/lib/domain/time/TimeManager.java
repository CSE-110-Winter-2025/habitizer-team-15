package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public abstract class TimeManager {
	public static final int secondsToNanoseconds = 1000000000;
	public abstract HabitizerTime getCurrentTimeNanoseconds();
	public double getCurrentTimeSeconds() {
		return getCurrentTimeNanoseconds().time() / (double)secondsToNanoseconds;
	}
}
