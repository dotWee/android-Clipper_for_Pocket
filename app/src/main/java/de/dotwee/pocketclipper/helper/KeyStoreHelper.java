package de.dotwee.pocketclipper.helper;

import android.content.SharedPreferences;

public class KeyStoreHelper {

    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_REQUEST_TOKEN = "request_token";
    private static final String PREF_USERNAME = "username";

    public static void writeUsername(SharedPreferences sharedPreferences, String username) {
        writeToSharedPreferences(sharedPreferences, PREF_USERNAME, username);
    }

    public static String getAccessToken(SharedPreferences sharedPreferences) {
        return getFromSharedPreferences(sharedPreferences, PREF_ACCESS_TOKEN);
    }

    public static String getUsername(SharedPreferences sharedPreferences) {
        return getFromSharedPreferences(sharedPreferences, PREF_USERNAME);
    }

    public static String getRequestToken(SharedPreferences sharedPreferences) {
        return getFromSharedPreferences(sharedPreferences, PREF_REQUEST_TOKEN);
    }

    private static String getFromSharedPreferences(SharedPreferences sharedPreferences, String key) {
        return sharedPreferences.getString(key, null);
    }

    private static void writeToSharedPreferences(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void writeRequestToken(SharedPreferences sharedPreferences, String requestToken) {
        writeToSharedPreferences(sharedPreferences, PREF_REQUEST_TOKEN, requestToken);
    }

    public static void writeAccessToken(SharedPreferences sharedPreferences, String accessToken) {
        writeToSharedPreferences(sharedPreferences, PREF_ACCESS_TOKEN, accessToken);
    }
}
