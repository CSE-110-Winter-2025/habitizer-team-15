package edu.ucsd.cse110.habitizer.lib.domain;

import edu.ucsd.cse110.habitizer.lib.data.TaskTime;
import kotlin.NotImplementedError;

/**
 * Add javadoc
 */
public class TimeTracker {
	private ITimeManager timeManager;
	private TaskTime lastCheckoff;
	private TaskTime startTime;

	public TimeTracker(ITimeManager timeManager) {
		this.timeManager = timeManager;
	}

	public TaskTime getElapsedTime() {
		return timeManager.getCurrentTime().subtract(this.startTime);
	}

	public TaskTime getCheckoffTime() {
		return timeManager.getCurrentTime().subtract(this.lastCheckoff);
	}

	public void start() {
		this.startTime = this.timeManager.getCurrentTime();
		this.lastCheckoff = this.startTime;
	}

	public void stop() {
		throw new NotImplementedError();
	}
}
