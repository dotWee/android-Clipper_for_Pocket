package de.dotwee.pocketclipper.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.dotwee.pocketclipper.R;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.ui.auth.RequestTokenAuthFragment;
import timber.log.Timber;

public class AuthActivity extends AppCompatActivity implements RequestTokenAuthFragment.Callback {
    private static final String LOG_TAG = AuthActivity.class.getSimpleName();

    RequestTokenAuthFragment requestTokenAuthFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(LOG_TAG);

        setContentView(R.layout.activity_auth);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestTokenAuthFragment = RequestTokenAuthFragment.newInstance(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, requestTokenAuthFragment)
                    .commitNow();
        }
    }

    @Override
    public void onNext(PocketService.RequestTokenResponse requestTokenResponse) {
        Toast.makeText(this, "requestToken=" + requestTokenResponse.toString(), Toast.LENGTH_SHORT).show();
    }
}
