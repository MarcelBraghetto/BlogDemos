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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;
import io.github.marcelbraghetto.kaleidoscope.IKaleidoscopeInterface;
import io.github.marcelbraghetto.kaleidoscope.local.events.DrawLineEvent;

public class MainActivity extends AppCompatActivity {
    private boolean mIsRemoteKaleidoscopeInterfaceBound;
    private IKaleidoscopeInterface mRemoteKaleidoscopeInterface;

    private InputDrawingCanvas mInputDrawingCanvas;
    private OutputDrawingCanvas mOutputDrawingCanvas;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_clear) {
            mOutputDrawingCanvas.clear();
            mInputDrawingCanvas.clear();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputDrawingCanvas = (InputDrawingCanvas) findViewById(R.id.input_drawing_canvas);
        mInputDrawingCanvas.setDelegate(new InputDrawingCanvas.Delegate() {
            @Override
            public void onLineDrawn(int x1, int y1, int x2, int y2) {
                if(mIsRemoteKaleidoscopeInterfaceBound) {
                    try {
                        mRemoteKaleidoscopeInterface.drawLine(x1, y1, x2, y2);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mOutputDrawingCanvas = (OutputDrawingCanvas) findViewById(R.id.output_drawing_canvas);

        Intent remoteServiceIntent = createRemoteServiceIntent();
        if(remoteServiceIntent != null) {
            bindService(remoteServiceIntent, mRemoteServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mOutputDrawingCanvas.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mOutputDrawingCanvas.pause();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull DrawLineEvent event) {
        if(mOutputDrawingCanvas != null) {
            mOutputDrawingCanvas.drawLine(event.x1, event.y1, event.x2, event.y2);
        }
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
}
