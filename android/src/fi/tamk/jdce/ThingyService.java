package fi.tamk.jdce;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import androidx.annotation.Nullable;
import no.nordicsemi.android.thingylib.BaseThingyService;
import no.nordicsemi.android.thingylib.ThingyConnection;

public class ThingyService extends BaseThingyService {

    @Nullable
    @Override
    public BaseThingyBinder onBind(Intent intent) {
        return new ThingyBinder();
    }

    public class ThingyBinder extends BaseThingyBinder {
        private boolean mIsScanning = false;

        public void setScanningState(final boolean isScanning) {
            mIsScanning = isScanning;
        }

        public boolean isScanningState() {
            return mIsScanning;
        }


        @Override
        public ThingyConnection getThingyConnection(BluetoothDevice device) {
            return mThingyConnections.get(device);
        }

        //You can write your own functionality here

    }
}
