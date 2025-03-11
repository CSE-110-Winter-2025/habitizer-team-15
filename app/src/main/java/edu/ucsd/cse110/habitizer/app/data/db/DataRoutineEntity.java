package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.DataTask;

@Entity(tableName = "data_routines")
public class DataRoutineEntity {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;

    @TypeConverters(DataTaskListConverter.class)
    @ColumnInfo(name = "data_tasks")
    public List<DataTask> dataTasks;

    @ColumnInfo(name = "total_time")
    public long totalTime;

    public DataRoutineEntity(Integer id, String name, List<DataTask> dataTasks, long totalTime) {
        this.id = id;
        this.name = name;
        this.dataTasks = dataTasks;
        this.totalTime = totalTime;
    }


    public static DataRoutineEntity fromDataRoutine(@NonNull DataRoutine dataRoutine) {
        return new DataRoutineEntity(
            dataRoutine.id(),
            dataRoutine.name(),
            dataRoutine.dataTasks(),
            dataRoutine.totalTime()
        );
    }

    public @NonNull DataRoutine toDataRoutine() {
        return new DataRoutine(name, dataTasks, id, totalTime);
    }
}
