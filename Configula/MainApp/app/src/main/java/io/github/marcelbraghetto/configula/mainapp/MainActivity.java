package io.github.marcelbraghetto.configula.mainapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.main_text);

        String serverUrl = AppConfig.getServerUrl();
        boolean isAnalyticsEnabled = AppConfig.isAnalyticsEnabled();

        StringBuilder sb = new StringBuilder("Server Url: ")
                .append(serverUrl)
                .append("\n")
                .append("Analytics enabled: ")
                .append(isAnalyticsEnabled);

        textView.setText(sb.toString());
    }
}
