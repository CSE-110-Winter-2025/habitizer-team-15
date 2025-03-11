package edu.ucsd.cse110.habitizer.app.data.db;

import android.util.JsonWriter;

import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;

/**
 * For some reason, Android Room does NOT support lists!
 * Unfortunately, we have to resort to such means as JSON
 * to serialize List<DataTask>.
 */
public class DataTaskListConverter {
    @TypeConverter
    public String toString(List<DataTask> list) {
        JSONArray jsonArray = new JSONArray();
        for (DataTask task : list) {
            JSONObject value = new JSONObject();
            try {
                value.put("id", task.id());
                value.put("name", task.name());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            jsonArray.put(value);
        }
        return jsonArray.toString();
    }
    @TypeConverter
    public List<DataTask> toDataTaskList(String d) {
        List<DataTask> dataTaskList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(d);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DataTask newTask = new DataTask(
                    jsonObject.getString("name"),
                    jsonObject.getInt("id"));
                dataTaskList.add(newTask);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return dataTaskList;
    }
}
