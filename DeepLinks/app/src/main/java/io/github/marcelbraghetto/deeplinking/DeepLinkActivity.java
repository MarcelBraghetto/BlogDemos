package io.github.marcelbraghetto.deeplinking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Marcel Braghetto on 6/09/15.
 *
 * This activity simply receives the intent data
 * and restarts the application main activity with
 * the given data then finishes itself.
 */
public class DeepLinkActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Generate a new intent to launch our main activity
        // with the given action and data, and tell it to
        // clear and restart a fresh instance.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(getIntent().getAction());
        intent.setData(getIntent().getData());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finish this activity as well.
        finish();
    }
}
