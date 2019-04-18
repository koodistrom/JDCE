package com.mygdx.game;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;
import no.nordicsemi.android.thingylib.Thingy;
import no.nordicsemi.android.thingylib.ThingyListenerHelper;
import no.nordicsemi.android.thingylib.ThingySdkManager;
import no.nordicsemi.android.thingylib.utils.ThingyUtils;

public class AndroidLauncher extends AndroidApplication implements ThingySdkManager.ServiceConnectionListener {

    ThingySdkManager thingySdkManager;
    ThingyService.ThingyBinder thingyBinder;
    private JDCEThingyListener thingyListener;

    private static final int SCAN_DURATION = 15000;
    boolean mIsScanning = false;
    private Handler mProgressHandler = new Handler();
    private BluetoothDevice mDevice;
    private AndroidResolver androidResolver;
    private JDCEGame game;
    boolean permissions;
    boolean bluetoothOn;
    public static final int REQUEST_ENABLE_BT = 1020;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
	    androidResolver = new AndroidResolver(this);
        JDCEGame.setPlatformResolver(androidResolver);
		super.onCreate(savedInstanceState);
        System.out.println("edes Android launcher toimii");
        thingySdkManager = ThingySdkManager.getInstance();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock=true;
		game = new JDCEGame();
		initialize(game, config);

		thingyListener = new JDCEThingyListener(thingySdkManager, androidResolver);
        thingyBinder = (ThingyService.ThingyBinder) thingySdkManager.getThingyBinder();



	}

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBleEnabled()) {
            enableBle();
        }
        System.out.println("onStart kutsuttu ");
        thingySdkManager.bindService(this, ThingyService.class);

        ThingyListenerHelper.registerThingyListener(this, thingyListener);
        registerReceiver(mBleStateChangedReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        thingySdkManager.unbindService(this);
        ThingyListenerHelper.unregisterThingyListener(this, thingyListener);
        unregisterReceiver(mBleStateChangedReceiver);
    }

    @Override
    public void onServiceConnected() {
        //Use this binder to access you own API methods declared in the binder inside ThingyService
        System.out.println("service yhdistetty");

        thingyBinder = (ThingyService.ThingyBinder) thingySdkManager.getThingyBinder();
        //prepareForScanning(true);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    final Runnable mBleScannerTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();

        }
    };

    private boolean checkIfRequiredPermissionsGranted() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                System.out.println("ei lupaa: ACCESS_COARSE_LOCATION");
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean isLocationEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int locationMode = Settings.Secure.LOCATION_MODE_OFF;
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (final Settings.SettingNotFoundException e) {
                // do nothing
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        return true;
    }



    public void prepareForScanning(final boolean nfcInitiated) {
        System.out.println("preparoidaan");
        System.out.println("luvat: "+ checkIfRequiredPermissionsGranted());

        if (checkIfRequiredPermissionsGranted()) {
            if (isLocationEnabled()) {
                permissions = true;
                if (thingyBinder != null) {
                    thingyBinder.setScanningState(true);
                    if (nfcInitiated) {
                        System.out.println("nfc-juttu käytössä ja skannaa");
                    } else {
                        System.out.println("skannaa");
                    }
                    startScan();
                }
            } else {
                permissions = false;
                System.out.println("sijaintilupapuuttuu");


            }
        }
    }

    private void startScan() {
        System.out.println("skannaus aloitettu");
        if (mIsScanning) {
            return;
        }
        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        final ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0).setUseHardwareBatchingIfSupported(false).setUseHardwareFilteringIfSupported(false).build();
        final List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(ThingyUtils.THINGY_BASE_UUID)).build());
        scanner.startScan(filters, settings, mScanCallback);
        mIsScanning = true;

        //Handler to stop scan after the duration time out
        mProgressHandler.postDelayed(mBleScannerTimeoutRunnable, SCAN_DURATION);
    }

    /**
     * Stop scan on rotation or on app closing.
     * In case the stopScan is called inside onDestroy we have to check if the app is finishing as the mIsScanning flag becomes false on rotation
     */
    private void stopScan() {
        if (thingyBinder != null) {
            thingyBinder.setScanningState(false);
        }

        if (mIsScanning) {
            System.out.println("skannaus loppu");
            androidResolver.isScanning = false;
            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(mScanCallback);
            mProgressHandler.removeCallbacks(mBleScannerTimeoutRunnable);
            mIsScanning = false;
        }
    }

    private void stopScanOnRotation() {
        if (!isFinishing() && mIsScanning) {
            if (thingyBinder != null) {
                thingyBinder.setScanningState(true);
            }
            System.out.println("skannaus loppu rotation homma");
            mProgressHandler.removeCallbacks(mBleScannerTimeoutRunnable);
            final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.stopScan(mScanCallback);
            mIsScanning = false;
        }
    }

    private String mAddress;
    private ScanCallback mScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(final int callbackType, @NonNull final ScanResult result) {
            // do nothing
            final BluetoothDevice device = result.getDevice();
            androidResolver.addDevice(device);

            System.out.println("Laite löytyi: "+device);
            if(device.getName().equals("JDCE12")){
                mDevice = device;
                System.out.println("laite: "+mDevice.getBluetoothClass());
                stopScan();
                connect();
            }


            if (mAddress != null && mAddress.equals(device.getAddress())) {

                stopScan();
                connect(device);
                mAddress = null;
                return;
            }

            if (device.equals(mDevice)) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        stopScan();
                        connect();

                    }
                });
            }
        }

        @Override
        public void onBatchScanResults(@NonNull final List<ScanResult> results) {
        }

        @Override
        public void onScanFailed(final int errorCode) {
            // should never be called
        }
    };

    private void connect() {
        thingySdkManager.connectToThingy(this, mDevice, ThingyService.class);
        final Thingy thingy = new Thingy(mDevice);
        System.out.println(mDevice);
        thingySdkManager.setSelectedDevice(mDevice);
        System.out.println(mDevice.getType());
        System.out.println("nyt pitäis olla yhteys");
        System.out.println("tyyppi: " + mDevice.getType());
        androidResolver.setConnected(true);
    }

    public void connect(final BluetoothDevice device) {
        thingySdkManager.connectToThingy(this, device, ThingyService.class);
        final Thingy thingy = new Thingy(device);
        thingySdkManager.setSelectedDevice(device);
        System.out.println("nyt pitäis olla yhteys");
        System.out.println("tyyppi: "+device.getType());
        androidResolver.setConnected(true);
    }

    final BroadcastReceiver mBleStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        //teksti yhdistäsaatana
                        enableBle();
                        break;
                }
            }
        }
    };

    /**
     * Checks whether the Bluetooth adapter is enabled.
     */
    private boolean isBleEnabled() {
        final BluetoothManager bm = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        final BluetoothAdapter ba = bm.getAdapter();
        return ba != null && ba.isEnabled();
    }

    /**
     * Tries to start Bluetooth adapter.
     */
    private void enableBle() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

}
