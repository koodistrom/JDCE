package com.mygdx.game;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.wahoofitness.common.datatypes.TimePeriod;
import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.capabilities.Accelerometer;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.CrankRevs;
import com.wahoofitness.connector.conn.characteristics.CrankWheelRevsHelper;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.listeners.discovery.DiscoveryListener;

import java.util.Collection;

public class ConnectingService extends Service {

    private HardwareConnector mHardwareConnector ;
    CrankRevs.Listener mCrankRevsListener = new CrankRevs.Listener() {

        @Override
        public void onCrankRevsData(CrankRevs.Data data) {

        }
    };

    Accelerometer.Listener mAccelerometerListener = new Accelerometer.Listener() {
        @Override
        public void onAccelerometerData(Accelerometer.Data data) {

        }
    };

    static SensorConnection mSensorConnection;
    static Accelerometer accelerometer;

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
        System.out.println("ConnectingService on Create");
        mHardwareConnector = new HardwareConnector ( this , mHardwareConnectorListener );
        mHardwareConnector.startDiscovery(new DiscoveryListener() {
            @Override
            public void onDeviceDiscovered(ConnectionParams connectionParams) {
                System.out.print("device discovered: ");
                System.out.println(connectionParams.getProductType());
                mSensorConnection = mHardwareConnector.requestSensorConnection(connectionParams, new SensorConnection.Listener() {
                    @Override
                    public void onNewCapabilityDetected(SensorConnection sensorConnection, Capability.CapabilityType capabilityType) {
                        System.out.print("capability found: ");
                        System.out.println(capabilityType);
                        if ( capabilityType == Capability.CapabilityType. CrankRevs ) {
                            System.out.println("Crank rev capability found");
                            CrankRevs crankRevs = ( CrankRevs ) sensorConnection . getCurrentCapability ( Capability.CapabilityType. CrankRevs );
                            crankRevs.setCrankRevsAutoZeroTime(TimePeriod.fromSeconds(1));
                            crankRevs . addListener ( mCrankRevsListener );


                        }

                        if ( capabilityType == Capability.CapabilityType. Accelerometer ) {
                            System.out.println("Accelerometer capability found");
                            accelerometer = (Accelerometer)sensorConnection.getCurrentCapability(Capability.CapabilityType.Accelerometer);
                            accelerometer.addListener(mAccelerometerListener);

                        }
                    }

                    @Override
                    public void onSensorConnectionStateChanged(SensorConnection sensorConnection, HardwareConnectorEnums.SensorConnectionState sensorConnectionState) {

                    }

                    @Override
                    public void onSensorConnectionError(SensorConnection sensorConnection, HardwareConnectorEnums.SensorConnectionError sensorConnectionError) {

                    }
                });
                 System.out.print("capabilities: ");
                 Collection<Capability.CapabilityType> capabilities = mSensorConnection.getExpectedCapabilities();
                 System.out.println(capabilities.size());
                 System.out.println(mSensorConnection.getExpectedCapabilities());


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

    public static Double getCrankRevsData ()
    {
        if ( mSensorConnection != null ) {
            CrankRevs crankRevs = ( CrankRevs ) mSensorConnection . getCurrentCapability ( Capability.CapabilityType. CrankRevs );
            if ( crankRevs != null ) {
                return crankRevs . getCrankRevsData ().getCrankSpeed().asRevolutionsPerSecond();
            } else {
// The sensor connection does not currently support the crank revs capability
                return null;
            }
        } else {
// Sensor not connected
            return null;

        }
    }

    public static Float[] getAccelerometerData () {
        Float[] xYZ = new Float[3];
        if ( mSensorConnection != null ) {

            if ( accelerometer != null ) {
                xYZ[0] =  accelerometer . getAccelerometerData().getAccelerationX(false);
                xYZ[1] =  accelerometer . getAccelerometerData().getAccelerationY(false);
                xYZ[2] =  accelerometer . getAccelerometerData().getAccelerationZ(false);
                return xYZ;
            } else {
// The sensor connection does not currently support the crank revs capability
                return null;
            }
        } else {
// Sensor not connected
            return null;
        }
    }
}

