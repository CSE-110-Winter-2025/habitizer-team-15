package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

/**
 * Tracks time for a Routine session.
 */
public class TimeTracker {
	private final PausableWrapperITimeManager pausableTimeManager;
	private HabitizerTime timeManagerStartTime;

	/**
	 * This start time difference is mainly used when restoring a Routine session.
 	 */
	private HabitizerTime trackerStartTimeDiff = HabitizerTime.zero;
	private HabitizerTime trackerLastCheckoff = HabitizerTime.zero;
	private HabitizerTime trackerLastCheckoffInit = HabitizerTime.zero;

	private HabitizerTime trackerEndTime;

	private MutableSubject<Boolean> isStarted;

	public TimeTracker(ITimeManager ITimeManager) {
		this.isStarted = new PlainMutableSubject<>();
		this.isStarted.setValue(false);
		this.pausableTimeManager = new PausableWrapperITimeManager(ITimeManager);
	}

	public HabitizerTime getElapsedTime() {
		if (trackerEndTime != null)
			return trackerEndTime;
		if (timeManagerStartTime == null)
			return HabitizerTime.zero;
		return pausableTimeManager.getCurrentTime()
				.subtract(this.timeManagerStartTime)
				.add(trackerStartTimeDiff);
	}

	public void checkoff() {
		this.trackerLastCheckoff = getElapsedTime();
	}

	public void setTrackerLastCheckoffInit(HabitizerTime t) {
		this.trackerLastCheckoffInit = t;
	}

	public HabitizerTime getCheckoffTime() {
		return getElapsedTime().subtract(this.trackerLastCheckoff);
	}

	public void start() {
		this.isStarted.setValue(true);
		this.timeManagerStartTime = this.pausableTimeManager.getCurrentTime();
		this.trackerLastCheckoff = trackerLastCheckoffInit;
	}

	public void stop() {
		this.trackerEndTime = getElapsedTime();
		this.isStarted.setValue(false);
	}

	public void addStartTime(HabitizerTime time) {
		this.trackerStartTimeDiff = trackerStartTimeDiff.add(time);
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
