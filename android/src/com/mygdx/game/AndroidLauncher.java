package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import no.nordicsemi.android.thingylib.ThingyListener;
import no.nordicsemi.android.thingylib.ThingyListenerHelper;
import no.nordicsemi.android.thingylib.ThingySdkManager;

public class AndroidLauncher extends AndroidApplication implements ThingySdkManager.ServiceConnectionListener {
    ConnectingService connectingService;
    ThingySdkManager thingySdkManager;
    ThingyService.ThingyBinder thingyBinder;
    private JDCEThingyListener thingyListener;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
        JDCEGame.setPlatformResolver(new AndroidResolver());
		super.onCreate(savedInstanceState);
        System.out.println("edes Android launcher toimii");
        thingySdkManager = ThingySdkManager.getInstance();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock=true;
		initialize(new JDCEGame(), config);

		thingyListener = new JDCEThingyListener(thingySdkManager);

		connectingService= new ConnectingService();
        Intent intent = new Intent(this,ConnectingService.class);
        startService(intent);

	}

    @Override
    protected void onStart() {
        super.onStart();
        thingySdkManager.bindService(this, ThingyService.class);

        ThingyListenerHelper.registerThingyListener(this, thingyListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        thingySdkManager.unbindService(this);
        ThingyListenerHelper.unregisterThingyListener(this, thingyListener);
    }

    @Override
    public void onServiceConnected() {
        //Use this binder to access you own API methods declared in the binder inside ThingyService
        thingyBinder = (ThingyService.ThingyBinder) thingySdkManager.getThingyBinder();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
