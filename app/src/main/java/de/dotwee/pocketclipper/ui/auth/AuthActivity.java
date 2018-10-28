package de.dotwee.pocketclipper.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.FragmentManager;
import de.dotwee.pocketclipper.BuildConfig;
import de.dotwee.pocketclipper.R;
import de.dotwee.pocketclipper.api.PocketApi;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.helper.KeyStoreHelper;
import de.dotwee.pocketclipper.ui.auth.access.AccessTokenAuthFragment;
import de.dotwee.pocketclipper.ui.auth.request.RequestTokenAuthFragment;
import de.dotwee.pocketclipper.ui.auth.start.StartAuthFragment;
import timber.log.Timber;

public class AuthActivity extends AppCompatActivity implements RequestTokenAuthFragment.Callback, AccessTokenAuthFragment.Callback, StartAuthFragment.Callback {
    private static final String LOG_TAG = AuthActivity.class.getSimpleName();

    FragmentManager fragmentManager;
    StartAuthFragment startAuthFragment;
    PocketService.RequestTokenResponse requestTokenResponse;


    SharedPreferences sharedPreferences;
    PocketApi pocketApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(LOG_TAG);

        setContentView(R.layout.activity_auth);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        pocketApi = new PocketApi();

        startAuthFragment = StartAuthFragment.newInstance(this);
        fragmentManager.beginTransaction()
                .replace(R.id.container, RequestTokenAuthFragment.newInstance(this))
                .commitNow();
    }

    @Override
    public void onNext(PocketService.RequestTokenResponse requestTokenResponse) {
        this.requestTokenResponse = requestTokenResponse;

        Toast.makeText(this, "requestToken=" + this.requestTokenResponse.toString(), Toast.LENGTH_SHORT).show();
        KeyStoreHelper.writeRequestToken(PreferenceManager.getDefaultSharedPreferences(this), requestTokenResponse.code);

        Uri uri = pocketApi.getAuthorizationUri(requestTokenResponse);
        Timber.i("onContinueWithAuthorization: uri=%s", uri.toString());
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, uri);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        if (intent != null) {
            String intentAction = intent.getAction();

            if (intentAction != null && intentAction.equals(Intent.ACTION_VIEW)) {
                String dataString = intent.getDataString();

                Timber.i("onResume: action=%s data=%s", intentAction, dataString);
                if (BuildConfig.PocketApiRedirectUri.equalsIgnoreCase(dataString)) {
                    // Show next fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, AccessTokenAuthFragment.newInstance(this, requestTokenResponse))
                            .commitNow();
                }
            }
        }

        // TODO else: show errors...
    }

    @Override
    public void onNext(PocketService.AccessTokenResponse accessTokenResponse) {
        Toast.makeText(this, "accessTokenResponse=" + accessTokenResponse.toString(), Toast.LENGTH_SHORT).show();

        KeyStoreHelper.writeAccessToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()), accessTokenResponse.accessToken);
        KeyStoreHelper.writeUsername(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()), accessTokenResponse.username);
    }

    @Override
    public void onClickStart() {
        fragmentManager.beginTransaction()
                .replace(R.id.container, RequestTokenAuthFragment.newInstance(this))
                .commitNow();
    }
}
