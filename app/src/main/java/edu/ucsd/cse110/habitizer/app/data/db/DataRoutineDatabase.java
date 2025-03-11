package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DataRoutineEntity.class}, version = 1)
public abstract class DataRoutineDatabase extends RoomDatabase {
    public abstract DataRoutineDao dataRoutineDao();
}
