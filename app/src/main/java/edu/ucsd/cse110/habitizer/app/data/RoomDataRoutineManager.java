package edu.ucsd.cse110.habitizer.app.data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.data.db.DataRoutineDao;
import edu.ucsd.cse110.habitizer.app.data.db.DataRoutineEntity;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.IDataRoutineManager;

public class RoomDataRoutineManager implements IDataRoutineManager {

    private DataRoutineDao dao;

    public RoomDataRoutineManager(DataRoutineDao dao) {
        this.dao = dao;
    }

    @Override
    public void clearDataRoutines() {

    }

    @Override
    public void addDataRoutine(DataRoutine dataRoutine) {
        dao.insert(DataRoutineEntity.fromDataRoutine(dataRoutine));
    }

    @Override
    public void addDataRoutine(int i, DataRoutine dataRoutine) {

    }

    @Override
    public void setDataRoutine(int i, DataRoutine dataRoutine) {
        addDataRoutine(dataRoutine);
    }

    @Override
    public List<DataRoutine> getDataRoutines() {
        return dao.getAll()
                .stream()
                .map(DataRoutineEntity::toDataRoutine)
                .collect(Collectors.toList());
    }
}
