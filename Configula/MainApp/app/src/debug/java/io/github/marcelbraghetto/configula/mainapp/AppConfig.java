package io.github.marcelbraghetto.configula.mainapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Sample app config for debug builds.
 */
public class AppConfig {
    private static final String KEY_SERVER_URL = "server_url";
    private static final String KEY_ANALYTICS_ENABLED = "analytics_enabled";

    private static final String SERVER_URL_DEFAULT = "http://localhost";
    private static boolean ANALYTICS_ENABLED_DEFAULT = false;

    private AppConfig() { }

    private static SharedPreferences getSharedPreferences() {
        return MainApp.getContext().getSharedPreferences("configula", Context.MODE_PRIVATE);
    }

    /**
     * Accept and store the server url.
     *
     * @param serverUrl value to save.
     */
    public static void setServerUrl(@NonNull String serverUrl) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_SERVER_URL, serverUrl);
        editor.commit();
    }

    /**
     * Accept and store the analytics enabled flag.
     *
     * @param analyticsEnabled value to save.
     */
    public static void setAnalyticsEnabled(boolean analyticsEnabled) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_ANALYTICS_ENABLED, analyticsEnabled);
        editor.commit();
    }

    /**
     * Retrieve the analytics enabled setting as it was saved
     * by Configula, or a default if not set.
     *
     * @return analytics enabled flag that was configured or default.
     */
    public static boolean isAnalyticsEnabled() {
        return getSharedPreferences().getBoolean(KEY_ANALYTICS_ENABLED, ANALYTICS_ENABLED_DEFAULT);
    }

    /**
     * Retrieve the server url as it was saved
     * by Configula, or a default if not set.
     *
     * @return server url that was configured or default.
     */
    @NonNull
    public static String getServerUrl() {
        return getSharedPreferences().getString(KEY_SERVER_URL, SERVER_URL_DEFAULT);
    }
}