package edu.ucsd.cse110.habitizer.lib.domain.time;

import java.util.Calendar;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class DebugJavaTimeManager extends TimeManager {

    @Override
    public HabitizerTime getCurrentTimeNanoseconds() {
        return new HabitizerTime((long) (Calendar.getInstance().getTimeInMillis() / 1000 * 60
                * HabitizerTime.secondsToNanoseconds));
    }
}
