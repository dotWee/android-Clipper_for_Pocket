package de.dotwee.pocketclipper.helper;

import android.content.ClipData;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import de.dotwee.pocketclipper.PocketClipperApplication;
import timber.log.Timber;

public class ClipboardHelper {
    private static final String LOG_TAG = ClipboardHelper.class.getSimpleName();

    public static String[] getUrlsFromClipboard() {
        ClipData clipData = PocketClipperApplication
                .clipboardManager
                .getPrimaryClip();

        if (clipData == null) {
            return new String[0];
        }

        String text = clipData.getItemAt(0).getText().toString();

        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();

            Timber.i("%s: URL extracted %s", LOG_TAG, url);
            links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }
}
