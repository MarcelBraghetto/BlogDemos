package io.github.marcelbraghetto.kaleidoscope.local;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import io.github.marcelbraghetto.kaleidoscope.IKaleidoscopeInterface;
import io.github.marcelbraghetto.kaleidoscope.local.events.DrawLineEvent;

public class MainActivity extends AppCompatActivity {
    private boolean mIsRemoteKaleidoscopeInterfaceBound;
    private IKaleidoscopeInterface mRemoteKaleidoscopeInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent remoteServiceIntent = createRemoteServiceIntent();
        if(remoteServiceIntent != null) {
            bindService(remoteServiceIntent, mRemoteServiceConnection, Context.BIND_AUTO_CREATE);
        }

        Button testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDrawLineCommand();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull DrawLineEvent event) {
        Toast.makeText(this, "Local Main Activity received DrawLineEvent!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mIsRemoteKaleidoscopeInterfaceBound) {
            unbindService(mRemoteServiceConnection);
        }
    }

    private final ServiceConnection mRemoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteKaleidoscopeInterface = IKaleidoscopeInterface.Stub.asInterface(service);
            mIsRemoteKaleidoscopeInterfaceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteKaleidoscopeInterface = null;
            mIsRemoteKaleidoscopeInterfaceBound = false;
        }
    };

    private Intent createRemoteServiceIntent() {
        // Create a basic intent using the intent filter action of
        // the remote service as per its manifest configuration.
        Intent intent = new Intent("RemoteKaleidoscopeService");

        // Attempt to resolve the service through the Android OS
        ResolveInfo info = getPackageManager().resolveService(intent, Context.BIND_AUTO_CREATE);

        // If the service failed to resolve it could mean that the
        // remote app is not installed or something wasn't configured
        // correctly, so we can't really start/bind the service...
        if(info == null) {
            return null;
        }

        // Otherwise, grab the resolved package name and service name and
        // assign them to the intent and bingo we have an explicit service intent!
        intent.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
        return intent;
    }

    private void sendDrawLineCommand() {
        if(mIsRemoteKaleidoscopeInterfaceBound) {
            try {
                mRemoteKaleidoscopeInterface.drawLine(1, 1, 2, 2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
