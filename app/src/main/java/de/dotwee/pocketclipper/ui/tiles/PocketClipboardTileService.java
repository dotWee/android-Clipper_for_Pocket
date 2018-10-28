package de.dotwee.pocketclipper.ui.tiles;

import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import de.dotwee.pocketclipper.api.PocketApi;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.helper.ClipboardHelper;
import de.dotwee.pocketclipper.helper.KeyStoreHelper;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class PocketClipboardTileService extends TileService implements Consumer<PocketService.AddLinkResponse> {
    private static final String LOG_TAG = PocketClipboardTileService.class.getSimpleName();

    private Consumer<Throwable> onErrorConsumer;
    private Consumer<PocketService.AddLinkResponse> onSuccessConsumer;
    private PocketApi pocketApi;

    public PocketClipboardTileService() {
        super();

        onErrorConsumer = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Timber.i("%s: error=%s", LOG_TAG, throwable.getMessage());
                Timber.e(throwable);

                Toast.makeText(getApplicationContext(), "Error! Message: " + throwable.getLocalizedMessage(), Toast.LENGTH_LONG);
            }
        };
        onSuccessConsumer = new Consumer<PocketService.AddLinkResponse>() {
            @Override
            public void accept(PocketService.AddLinkResponse addLinkResponse) throws Exception {
                Toast.makeText(getApplicationContext(), "Link saved to Pocket. Status: " + addLinkResponse.status, Toast.LENGTH_SHORT);
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        pocketApi = new PocketApi();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        String accessToken = KeyStoreHelper.getAccessToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        String[] urls = ClipboardHelper.getUrlsFromClipboard();
        for (String url :
                urls) {
            pocketApi.getAddLinkObservable(accessToken, url)
                    .subscribe(onSuccessConsumer, onErrorConsumer);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void accept(PocketService.AddLinkResponse addLinkResponse) throws Exception {

    }
}
