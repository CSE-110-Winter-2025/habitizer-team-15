package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

/**
 * Add javadoc
 */
public class TimeTracker {
	private final TimeManager timeManager;
	private HabitizerTime timeManagerStartTime;
	private HabitizerTime trackerLastCheckoff;

	private HabitizerTime trackerEndTime;

	private boolean isStarted;

	public TimeTracker(TimeManager timeManager) {
		this.timeManager = timeManager;
	}

	public HabitizerTime getElapsedTime() {
		if (trackerEndTime != null)
			return trackerEndTime;
		if (timeManagerStartTime == null)
			return HabitizerTime.zero;
		return timeManager.getCurrentTimeNanoseconds().subtract(this.timeManagerStartTime);
	}

	private void checkoff() {
		this.trackerLastCheckoff = getElapsedTime();
	}

	public HabitizerTime getCheckoffTime() {
		return getElapsedTime().subtract(this.trackerLastCheckoff);
	}

	public HabitizerTime getCheckoffTimeAndCheckoff() {
		var time = getCheckoffTime();
		checkoff();
		return time;
	}

	public void start() {
		this.isStarted = true;
		this.timeManagerStartTime = this.timeManager.getCurrentTimeNanoseconds();
		this.trackerLastCheckoff = HabitizerTime.zero;
	}

	public void stop() {
		this.trackerEndTime = getElapsedTime();
		this.isStarted = false;
	}
}
