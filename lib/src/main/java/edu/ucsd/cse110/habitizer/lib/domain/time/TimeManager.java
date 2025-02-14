package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public abstract class TimeManager {
	public abstract HabitizerTime getCurrentTimeNanoseconds();
	public double getCurrentTimeSeconds() {
		return getCurrentTimeNanoseconds().time() / (double) HabitizerTime.secondsToNanoseconds;
	}
}
