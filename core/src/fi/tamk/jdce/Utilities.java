package fi.tamk.jdce;

/**
 * Contains utilities for the main code.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class Utilities {
    /**
     * Turns seconds received as float in to a string.
     *
     * @param s seconds to be converted.
     * @return returns the converted String.
     */
    public static String secondsToString(float s) {
        int minutes = (int) s / 60;
        float seconds = s - (minutes * 60);

        return String.format("%02d:%02.2f", minutes, seconds);
    }
}
