package io.github.marcelbraghetto.configula.mainapp;

import android.support.annotation.NonNull;

/**
 * Created by Marcel Braghetto on 12/09/15.
 *
 * Sample app config for release builds.
 */
public final class AppConfig {
    private AppConfig() { }

    /**
     * Always enable analytics for release builds.
     *
     * @return true always.
     */
    public static boolean isAnalyticsEnabled() {
        return true;
    }

    /**
     * Hard code the server url so it's
     * constant for release builds.
     *
     * @return server url.
     */
    @NonNull
    public static String getServerUrl() {
        return "https://www.google.com";
    }
}
