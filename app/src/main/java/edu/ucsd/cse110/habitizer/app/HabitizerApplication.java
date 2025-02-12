package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class HabitizerApplication extends Application {
    private InMemoryDataSource inMemoryDataSource;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
