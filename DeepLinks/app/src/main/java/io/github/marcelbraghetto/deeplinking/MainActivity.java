package io.github.marcelbraghetto.deeplinking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.main_text);
        String action = getIntent().getAction();
        Uri data = getIntent().getData();

        // If we were started by an ACTION_VIEW, then grab the content
        // of the passed in data and display it to the user.
        if(Intent.ACTION_VIEW.equals(action) && data != null) {
            textView.setText(data.getLastPathSegment());
        } else {
            // Otherwise it was a regular launch, just show a default message.
            textView.setText(getString(R.string.default_launch_message));
        }
    }
}
