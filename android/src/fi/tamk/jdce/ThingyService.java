package fi.tamk.jdce;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import androidx.annotation.Nullable;
import no.nordicsemi.android.thingylib.BaseThingyService;
import no.nordicsemi.android.thingylib.ThingyConnection;

/**
 * Thingy service
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class ThingyService extends BaseThingyService {

    @Nullable
    @Override
    public BaseThingyBinder onBind(Intent intent) {
        return new ThingyBinder();
    }

    /**
     * The type Thingy binder.
     */
    public class ThingyBinder extends BaseThingyBinder {
        private boolean mIsScanning = false;

        /**
         * Sets scanning state.
         *
         * @param isScanning the is scanning
         */
        public void setScanningState(final boolean isScanning) {
            mIsScanning = isScanning;
        }

        /**
         * Is scanning state boolean.
         *
         * @return the boolean
         */
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
