package com.mygdx.game;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.listeners.discovery.DiscoveryListener;

public class ConnectingService extends Service {

    private HardwareConnector mHardwareConnector ;
    private final HardwareConnector.Listener mHardwareConnectorListener = new HardwareConnector.Listener (){
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
        mHardwareConnector.startDiscovery(new DiscoveryListener() {
            @Override
            public void onDeviceDiscovered(ConnectionParams connectionParams) {

            }

            @Override
            public void onDiscoveredDeviceLost(ConnectionParams connectionParams) {

            }

            @Override
            public void onDiscoveredDeviceRssiChanged(ConnectionParams connectionParams, int i) {

            }
        });
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
