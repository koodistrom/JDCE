package fi.tamk.jdce;

public interface PlatformResolver {

    public Float getPedalSpeed();

    public boolean isAndroid();

    public void scan();

    public boolean isConnected();
    public boolean isDeviceAdded();
    public String getNewDeviceName();
    public void connectTo(int index);
    public boolean isScanning();
    public void setConnected(boolean connected);
    public boolean getConnected();

}