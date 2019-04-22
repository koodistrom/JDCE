package fi.tamk.jdce;

/**
 * The interface Platform resolver. Implemented by desktop resolver and android resolver.
 *
 * Idea of the interface was to help getting input from android specific parts of the project to the core code that are needed for the Bluetooth sensor to work.
 * should be refactored so that this isn't used anymore but time ran out.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public interface PlatformResolver {

    /**
     * Gets pedal speed.
     *
     * @return the pedal speed
     */
    public Float getPedalSpeed();

    /**
     * Is android boolean.
     *
     * @return the boolean
     */
    public boolean isAndroid();

    /**
     * Scan.
     */
    public void scan();

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected();

    /**
     * Is device added boolean.
     *
     * @return the boolean
     */
    public boolean isDeviceAdded();

    /**
     * Gets new device name.
     *
     * @return the new device name
     */
    public String getNewDeviceName();

    /**
     * Connect to.
     *
     * @param index the index
     */
    public void connectTo(int index);

    /**
     * Is scanning boolean.
     *
     * @return the boolean
     */
    public boolean isScanning();

    /**
     * Sets connected.
     *
     * @param connected the connected
     */
    public void setConnected(boolean connected);

    /**
     * Gets connected.
     *
     * @return the connected
     */
    public boolean getConnected();

}
