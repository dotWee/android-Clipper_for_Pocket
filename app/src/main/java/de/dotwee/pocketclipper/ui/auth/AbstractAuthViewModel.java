package de.dotwee.pocketclipper.ui.auth;

import androidx.lifecycle.ViewModel;

public abstract class AbstractAuthViewModel extends ViewModel {

    protected Callback callback;
    protected boolean showProgressBar = false;
    protected String title;
    protected String message;

    protected boolean progressBarVisibility = false;
    protected boolean buttonStartEnabled = true;
    protected boolean buttonNextVisibility = false;

    protected Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    protected abstract void onStart();

    public interface Callback {

        void onDisplayStartup();

        void onDisplaySuccess();

        void onDisplayError(Throwable throwable);

        void onDisplayCompleted();
    }
}
