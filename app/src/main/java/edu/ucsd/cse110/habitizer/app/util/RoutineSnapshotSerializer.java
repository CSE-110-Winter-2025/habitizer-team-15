package edu.ucsd.cse110.habitizer.app.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class RoutineSnapshotSerializer {
    public static Routine.RoutineSnapshot fromJson(String string) {
        Routine.RoutineSnapshot routineSnapshot = new Routine.RoutineSnapshot();
        try {
            JSONObject root = new JSONObject(string);
            routineSnapshot.routineId = root.getInt("routineId");
            routineSnapshot.timeTrackerTime = root.getLong("timeTrackerTime");
            routineSnapshot.timeTrackerLastCheckoff = root.getLong("timeTrackerLastCheckoff");
            JSONArray recordedTaskTimes = root.getJSONArray("recordedTaskTimes");
            for (int i = 0; i < recordedTaskTimes.length(); i++) {
                if (recordedTaskTimes.isNull(i))
                    routineSnapshot.recordedTaskTimes.add(null);
                else
                    routineSnapshot.recordedTaskTimes.add(new HabitizerTime(recordedTaskTimes.getLong(i)));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return routineSnapshot;
    }
    public static String toJson(Routine.RoutineSnapshot snapshot) {
        JSONObject root = new JSONObject();
        JSONArray recordedTaskTimes = new JSONArray();
        try {
            root.put("routineId", snapshot.routineId);
            root.put("timeTrackerTime", snapshot.timeTrackerTime);
            for (HabitizerTime recordedTaskTime : snapshot.recordedTaskTimes) {
                if (recordedTaskTime == null)
                    recordedTaskTimes.put(null);
                else
                    recordedTaskTimes.put(recordedTaskTime.time());
            }
            root.put("recordedTaskTimes", recordedTaskTimes);
            root.put("timeTrackerLastCheckoff", snapshot.timeTrackerLastCheckoff);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return root.toString();
    }
}
