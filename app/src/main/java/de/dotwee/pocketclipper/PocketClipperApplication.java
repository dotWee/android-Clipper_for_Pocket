package de.dotwee.pocketclipper;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;

import timber.log.Timber;

//import androidx.browser.customtabs.CustomTabsIntent;

public class PocketClipperApplication extends Application {

    private static Context applicationContext;
    public static ClipboardManager clipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();

        PocketClipperApplication.applicationContext = getApplicationContext();
        PocketClipperApplication.clipboardManager =
                (ClipboardManager) applicationContext.getSystemService(CLIPBOARD_SERVICE);
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
