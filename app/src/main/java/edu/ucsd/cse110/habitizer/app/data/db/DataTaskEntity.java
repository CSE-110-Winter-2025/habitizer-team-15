package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;

public class DataTaskEntity {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    public Integer id = null;
    @ColumnInfo(name = "name")
    public String name;

    public DataTaskEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public DataTask toDataTask() {
        return new DataTask(name, id);
    }

    public static DataTaskEntity fromDataTask(DataTask d) {
        return new DataTaskEntity(d.id(), d.name());
    }
}
