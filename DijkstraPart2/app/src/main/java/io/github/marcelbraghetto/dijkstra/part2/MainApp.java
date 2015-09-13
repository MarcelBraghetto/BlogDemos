package io.github.marcelbraghetto.dijkstra.part2;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Simple application implementation.
 */
public class MainApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    @NonNull
    public static Context getContext() {
        return sContext;
    }
}
