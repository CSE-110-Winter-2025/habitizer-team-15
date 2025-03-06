package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataRoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(DataRoutineEntity entity);

    @Query("SELECT * FROM data_routines WHERE id = :id")
    DataRoutineEntity get(int id);

    @Query("SELECT * FROM data_routines")
    List<DataRoutineEntity> getAll();

    @Query("SELECT COUNT(*) FROM data_routines")
    int size();


}
