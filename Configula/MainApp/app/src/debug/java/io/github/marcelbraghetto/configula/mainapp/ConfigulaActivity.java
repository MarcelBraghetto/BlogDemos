package io.github.marcelbraghetto.configula.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by Marcel Braghetto on 6/09/15.
 *
 * This activity receives the Configula intent data
 * and restarts the application main activity with
 * the given data then finishes itself.
 *
 * Note that this activity is not even included in
 * the source path for release builds.
 */
public class ConfigulaActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab the data from the intent used to launch this activity.
        Uri uri = Uri.parse(getIntent().getDataString());

        // Attempt to set app config server url to the 'serverUrl' data parameter.
        String serverUrl = uri.getQueryParameter("serverUrl");
        if(!TextUtils.isEmpty(serverUrl)) {
            AppConfig.setServerUrl(serverUrl);
        }

        // Attempt to set app config analytics enabled to the 'analyticsEnabled' data parameter.
        String analyticsEnabled = uri.getQueryParameter("analyticsEnabled");
        if (!TextUtils.isEmpty(analyticsEnabled)) {
            AppConfig.setAnalyticsEnabled(Boolean.valueOf(analyticsEnabled));
        }

        // Generate a new intent to relaunch our main activity fresh.
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finish this activity as well.
        finish();
    }
}