package de.dotwee.pocketclipper;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

//import androidx.browser.customtabs.CustomTabsIntent;

public class PocketClipperApplication extends Application {

    //public static CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
    // TODO set toolbar color and/or setting custom actions
    //.build();
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        PocketClipperApplication.applicationContext = getApplicationContext();
        Timber.plant(new Timber.DebugTree());

        /*
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Timber.plant(new CrashReportingTree());
        }
        */
    }
}
