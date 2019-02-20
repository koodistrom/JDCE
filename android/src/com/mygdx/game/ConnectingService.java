package com.mygdx.game;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.conn.connections.SensorConnection;

public class ConnectingService extends Service {

    private HardwareConnector mHardwareConnector ;
    private final HardwareConnector . Listener mHardwareConnectorListener = new HardwareConnector . Listener (){
        @Override
        public void onHardwareConnectorStateChanged(HardwareConnectorTypes.NetworkType networkType, HardwareConnectorEnums.HardwareConnectorState hardwareConnectorState) {

        }

        @Override
        public void onFirmwareUpdateRequired(SensorConnection sensorConnection, String s, String s1) {

        }
    };

    @Override
    public void onCreate () {
        super . onCreate ();
        mHardwareConnector = new HardwareConnector ( this , mHardwareConnectorListener );
    }

    @Override
    public void onDestroy () {
        super . onDestroy ();
        mHardwareConnector . shutdown ();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
