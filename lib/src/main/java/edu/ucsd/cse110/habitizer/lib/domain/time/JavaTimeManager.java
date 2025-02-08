package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class JavaTimeManager extends ITimeManager {

    @Override
    public HabitizerTime getCurrentTimeNanoseconds() {
        return new HabitizerTime(System.nanoTime());
    }

}
