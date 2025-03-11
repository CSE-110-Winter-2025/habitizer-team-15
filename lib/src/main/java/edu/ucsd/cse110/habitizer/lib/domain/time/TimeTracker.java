package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

/**
 * Add javadoc
 */
public class TimeTracker {
	private final PausableWrapperTimeManager pausableTimeManager;
	private HabitizerTime timeManagerStartTime;
	private HabitizerTime trackerLastCheckoff;

	private HabitizerTime trackerEndTime;

	private MutableSubject<Boolean> isStarted;

	public TimeTracker(TimeManager timeManager) {
		this.isStarted = new PlainMutableSubject<>();
		this.isStarted.setValue(false);
		this.pausableTimeManager = new PausableWrapperTimeManager(timeManager);
	}

	public HabitizerTime getElapsedTime() {
		if (trackerEndTime != null)
			return trackerEndTime;
		if (timeManagerStartTime == null)
			return HabitizerTime.zero;
		return pausableTimeManager.getCurrentTime().subtract(this.timeManagerStartTime);
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
		this.isStarted.setValue(true);
		this.timeManagerStartTime = this.pausableTimeManager.getCurrentTime();
		this.trackerLastCheckoff = HabitizerTime.zero;
	}

	public void stop() {
		this.trackerEndTime = getElapsedTime();
		this.isStarted.setValue(false);
	}

	public boolean switchPause() {
		return this.pausableTimeManager.switchPause();
	}

	public boolean isPaused() {
		return this.pausableTimeManager.isPaused();
	}

	public MutableSubject<Boolean> getIsStartedSubject() {
		return isStarted;
	}
}
