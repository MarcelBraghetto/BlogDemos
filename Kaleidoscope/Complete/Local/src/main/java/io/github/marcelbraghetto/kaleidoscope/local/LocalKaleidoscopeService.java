package io.github.marcelbraghetto.kaleidoscope.local;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import de.greenrobot.event.EventBus;
import io.github.marcelbraghetto.kaleidoscope.IKaleidoscopeInterface;
import io.github.marcelbraghetto.kaleidoscope.local.events.DrawLineEvent;

/**
 * Created by Marcel Braghetto on 1/11/15.
 *
 * Service for the local application process that
 * uses our AIDL contract when something binds to it.
 */
public class LocalKaleidoscopeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mKaleidescopeInterface.asBinder();
    }

    /**
     * This is the secret sauce, our service will create an implementation of
     * the AIDL contract 'stub' which can then be returned as the 'binder'
     * when the 'onBind' method is invoked on the service.
     *
     * When the consumer of the service is connected and invokes methods via
     * AIDL, this is where they will be executed.
     */
    private final IKaleidoscopeInterface mKaleidescopeInterface = new IKaleidoscopeInterface.Stub() {
        @Override
        public void drawLine(int x1, int y1, int x2, int y2) throws RemoteException {
            EventBus.getDefault().post(new DrawLineEvent(x1, y1, x2, y2));
        }
    };
}
