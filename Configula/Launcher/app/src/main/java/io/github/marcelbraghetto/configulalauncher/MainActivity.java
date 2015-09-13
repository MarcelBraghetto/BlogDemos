package io.github.marcelbraghetto.configulalauncher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CheckBox mAnalyticsCheckbox;
    private Spinner mServerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnalyticsCheckbox = (CheckBox) findViewById(R.id.analytics_checkbox);
        mServerSpinner = (Spinner) findViewById(R.id.server_spinner);

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverUrl = getResources().getStringArray(R.array.server_urls)[mServerSpinner.getSelectedItemPosition()];
                boolean analyticsEnabled = mAnalyticsCheckbox.isChecked();

                // Construct the data URI that will be in the launch intent.
                Uri dataUri = new Uri.Builder()
                        .scheme("configula")
                        .authority("io.github.marcelbraghetto.configula.mainapp")
                        .appendQueryParameter("serverUrl", serverUrl)
                        .appendQueryParameter("analyticsEnabled", analyticsEnabled ? "true" : "false")
                        .build();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(dataUri);

                // See if there is something that can handle our intent.
                // In particular, if our main app is not installed OR
                // it is installed as a RELEASE build, then there will
                // be no activity that can launch the intent.
                if (intent.resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_intent_activity), Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });
    }
}
