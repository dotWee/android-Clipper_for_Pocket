package de.dotwee.pocketclipper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import de.dotwee.pocketclipper.api.PocketApi;
import de.dotwee.pocketclipper.api.PocketService;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Disposable requestTokenCodeObservable;
    PocketService.RequestTokenResponse requestTokenResponse;
    Consumer<Throwable> onRequestTokenCodeError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable);

            textViewTokenRequestStatus.setText("Error: Message=" + throwable.getMessage());
            progressBarTokenRequest.setVisibility(View.GONE);
            imageViewTokenRequestStatus.setVisibility(View.VISIBLE);
            imageViewTokenRequestStatus.setImageResource(R.drawable.ic_status_negative);

            buttonAuthorize.setEnabled(true);
        }
    };

    Disposable accessTokenCodeObservable;
    PocketService.AccessTokenResponse accessTokenResponse;
    TableRow tableRowAccess;
    ProgressBar progressBarTokenAccess;
    Consumer<PocketService.RequestTokenResponse> onRequestTokenSuccess = new Consumer<PocketService.RequestTokenResponse>() {
        @Override
        public void accept(PocketService.RequestTokenResponse requestTokenResponse) throws Exception {
            MainActivity.this.requestTokenResponse = requestTokenResponse;

            textViewTokenRequestStatus.setText("Success: Code=" + requestTokenResponse.code);
            progressBarTokenRequest.setVisibility(View.GONE);
            imageViewTokenRequestStatus.setVisibility(View.VISIBLE);
            imageViewTokenRequestStatus.setImageResource(R.drawable.ic_status_positive);

            onContinueWithAuthorization();
        }
    };
    PocketApi pocketApi;
    TableLayout tableLayoutStatus;
    Button buttonAuthorize;
    ProgressBar progressBarTokenRequest;
    ImageView imageViewTokenRequestStatus;
    TextView textViewTokenRequestStatus;


    TableRow tableRowAuthorize;
    ProgressBar progressBarTokenAuthorize;
    ImageView imageViewTokenAuthorizeStatus;
    TextView textViewTokenAuthorizeStatus;
    ImageView imageViewTokenAccessStatus;
    TextView textViewTokenAccessStatus;
    Consumer<PocketService.AccessTokenResponse> onAccessTokenSuccess = new Consumer<PocketService.AccessTokenResponse>() {
        @Override
        public void accept(PocketService.AccessTokenResponse accessTokenResponse) throws Exception {
            MainActivity.this.accessTokenResponse = accessTokenResponse;
            tableRowAccess.setVisibility(View.VISIBLE);

            textViewTokenAccessStatus.setText("Success: access_token=" + accessTokenResponse.accessToken + " username=" + accessTokenResponse.username);
            progressBarTokenAccess.setVisibility(View.GONE);
            imageViewTokenAccessStatus.setVisibility(View.VISIBLE);
            imageViewTokenAccessStatus.setImageResource(R.drawable.ic_status_positive);

            onContinueWithAuthorization();
        }
    };
    Consumer<Throwable> onAccessTokenError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            Timber.e(throwable);

            textViewTokenAccessStatus.setText("Error: Message=" + throwable.getMessage());
            progressBarTokenAccess.setVisibility(View.GONE);
            imageViewTokenAccessStatus.setVisibility(View.VISIBLE);
            imageViewTokenAccessStatus.setImageResource(R.drawable.ic_status_negative);

            buttonAuthorize.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Timber.tag(LOG_TAG);

        pocketApi = new PocketApi();

        tableLayoutStatus = findViewById(R.id.tableLayoutStatus);

        buttonAuthorize = findViewById(R.id.buttonAuthorize);
        buttonAuthorize.setOnClickListener(this);

        progressBarTokenRequest = findViewById(R.id.progressBarTokenRequest);
        imageViewTokenRequestStatus = findViewById(R.id.imageViewTokenRequestStatus);
        textViewTokenRequestStatus = findViewById(R.id.textViewTokenRequestStatus);

        tableRowAuthorize = findViewById(R.id.tableRowAuthorize);
        progressBarTokenAuthorize = findViewById(R.id.progressBarTokenAuthorize);
        imageViewTokenAuthorizeStatus = findViewById(R.id.imageViewTokenAuthorizeStatus);
        textViewTokenAuthorizeStatus = findViewById(R.id.textViewTokenAuthorizeStatus);

        tableRowAccess = findViewById(R.id.tableRowAccess);
        progressBarTokenAccess = findViewById(R.id.progressBarTokenAccess);
        imageViewTokenAccessStatus = findViewById(R.id.imageViewTokenAccessStatus);
        textViewTokenAccessStatus = findViewById(R.id.textViewTokenAccessStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void onSendUrlIntent(String url) {
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // set toolbar color and/or setting custom actions before invoking build()
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
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
                if (PARAMETER_REDIRECT_URI_VALUE.equalsIgnoreCase(dataString)) {
                    pocketAuthorizer.onRedirected();
                }
            }
        }
    }
    */

    private void onStartAuthorization() {
        progressBarTokenRequest.setVisibility(View.VISIBLE);
        imageViewTokenRequestStatus.setVisibility(View.GONE);
        textViewTokenRequestStatus.setText("Starting request...");

        requestTokenCodeObservable = pocketApi.getRequestTokenObservable()
                .subscribe(onRequestTokenSuccess, onRequestTokenCodeError);
    }

    private void onContinueWithAuthorization() {
        tableRowAuthorize.setVisibility(View.VISIBLE);
        progressBarTokenAuthorize.setVisibility(View.VISIBLE);
        imageViewTokenAuthorizeStatus.setVisibility(View.GONE);

        Uri uri = pocketApi.getAuthorizationUri(requestTokenResponse);
        Timber.i("onContinueWithAuthorization: uri=%s", uri.toString());
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, uri);
    }

    private void onFinishAccessToken() {
        tableRowAccess.setVisibility(View.VISIBLE);
        progressBarTokenAccess.setVisibility(View.VISIBLE);
        imageViewTokenAccessStatus.setVisibility(View.GONE);
        textViewTokenRequestStatus.setText("Starting request...");

        accessTokenCodeObservable = pocketApi.getAccessTokenObservable(this.requestTokenResponse)
                .subscribe(onAccessTokenSuccess, onAccessTokenError);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonAuthorize:
                    this.onStartAuthorization();
                    buttonAuthorize.setEnabled(false);
                    return;
            }
        }
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
                    tableRowAuthorize.setVisibility(View.VISIBLE);
                    progressBarTokenAuthorize.setVisibility(View.GONE);
                    textViewTokenAuthorizeStatus.setText("Successfully authorized");
                    imageViewTokenAuthorizeStatus.setVisibility(View.VISIBLE);
                    imageViewTokenAuthorizeStatus.setImageResource(R.drawable.ic_status_positive);

                    onFinishAccessToken();
                    return;
                }
            }
        }

        // TODO else: show errors...
    }
}
