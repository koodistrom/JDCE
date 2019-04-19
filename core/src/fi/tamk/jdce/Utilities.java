package fi.tamk.jdce;

public class Utilities {
    public static String secondsToString(float s) {
        int minutes = (int) s / 60;
        float seconds = s - (minutes * 60);

        return String.format("%02d:%02.2f", minutes, seconds);
    }
}
