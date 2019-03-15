package net.rheinsieg.wifisignalanalyzer.location;

import net.rheinsieg.wifisignalanalyzer.MainActivity;



public class LocationPrefsHelper {

        final public static String KEY_LOCATION_NAME = "location_key";

        public static void setName(String value) {
            MainActivity.locationpreferences.edit().putString(KEY_LOCATION_NAME, value ).commit();
        }
        public static String getName() {
            return MainActivity.locationpreferences.getString(KEY_LOCATION_NAME,"unknown|unknown|unknown");
        }


    }