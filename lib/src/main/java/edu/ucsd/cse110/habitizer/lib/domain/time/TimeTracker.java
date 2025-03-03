package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

/**
 * Keeps track of time for running Routines.
 */
public class TimeTracker {
	private final TimeManager timeManager;
	private HabitizerTime timeManagerStartTime;
	private HabitizerTime trackerLastCheckoff;

	private HabitizerTime trackerEndTime;

	private final MutableSubject<Boolean> isStarted;



	private final MutableSubject<Boolean> isPaused;
	private HabitizerTime pauseTime;
	private HabitizerTime pauseDiffTime;

	public TimeTracker(TimeManager timeManager) {
		this.isStarted = new PlainMutableSubject<>(false);
		this.isPaused = new PlainMutableSubject<>(false);
		this.pauseTime = HabitizerTime.zero;
		this.pauseDiffTime = HabitizerTime.zero;
		this.timeManager = timeManager;
	}

	public HabitizerTime getElapsedTime() {
		if (trackerEndTime != null)
			return trackerEndTime;
		if (Boolean.TRUE.equals(isPaused.getValue()))
			return pauseTime;
		if (timeManagerStartTime == null)
			return HabitizerTime.zero;

		return timeManager.getCurrentTime()
				.add(this.pauseDiffTime)
				.subtract(this.timeManagerStartTime);
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
		this.timeManagerStartTime = this.timeManager.getCurrentTime();
		this.trackerLastCheckoff = HabitizerTime.zero;
	}

	public void stop() {
		this.trackerEndTime = getElapsedTime();
		this.isStarted.setValue(false);
	}

	public boolean switchPause() {
		HabitizerTime currTime = timeManager.getCurrentTime();

		isPaused.setValue(Boolean.FALSE.equals(isPaused.getValue()));

		// If it becomes paused: we stop at currTime.
		// Else it becomes unpaused: diffTime now closes the gap between time during pause
		boolean paused = Boolean.TRUE.equals(isPaused.getValue());
		if (paused)
			pauseTime = currTime.add(pauseDiffTime);
		else
			pauseDiffTime = pauseTime.subtract(currTime);
		return paused;
	}

	public MutableSubject<Boolean> getIsStartedSubject() {
		return isStarted;
	}

	public MutableSubject<Boolean> getIsPausedSubject() {
		return isPaused;
	}
}
