package de.dotwee.pocketclipper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.dotwee.pocketclipper.api.PocketApi;
import de.dotwee.pocketclipper.api.PocketService;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Consumer<PocketService.RequestTokenCode> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    Disposable requestTokenCodeObservable;
    PocketApi pocketApi;

    TableLayout tableLayoutStatus;
    Button buttonAuthorize;

    ProgressBar progressBarTokenRequest;
    ImageView imageViewTokenRequestStatus;
    TextView textViewTokenRequestStatus;

    Consumer<PocketService.RequestTokenCode> onRequestTokenSuccess;
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
                .subscribe(this, onRequestTokenCodeError);
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
    public void accept(PocketService.RequestTokenCode requestTokenCode) throws Exception {
        textViewTokenRequestStatus.setText("Success: Code=" + requestTokenCode.code);
        progressBarTokenRequest.setVisibility(View.GONE);
        imageViewTokenRequestStatus.setVisibility(View.VISIBLE);
        imageViewTokenRequestStatus.setImageResource(R.drawable.ic_status_positive);
    }
}
