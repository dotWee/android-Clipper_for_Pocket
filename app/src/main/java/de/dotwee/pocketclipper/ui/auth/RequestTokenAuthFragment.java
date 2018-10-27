package de.dotwee.pocketclipper.ui.auth;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import de.dotwee.pocketclipper.api.PocketService;
import timber.log.Timber;

public class RequestTokenAuthFragment extends AbstractAuthFragment<RequestTokenAuthViewModel> {

    private RequestTokenAuthViewModel viewModel;
    private Callback callback;

    public RequestTokenAuthFragment() {
    }

    public static RequestTokenAuthFragment newInstance() {
        return new RequestTokenAuthFragment();
    }

    public static RequestTokenAuthFragment newInstance(Callback callback) {
        RequestTokenAuthFragment fragment = new RequestTokenAuthFragment();
        fragment.setCallback(callback);
        return fragment;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.i("onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("onActivityCreated");

        viewModel = ViewModelProviders.of(this).get(RequestTokenAuthViewModel.class);
        viewModel.setCallback(this);
        setViewModel(viewModel);
    }

    @Override
    public void onDisplaySuccess() {
        super.onDisplaySuccess();

        PocketService.RequestTokenResponse requestTokenResponse = viewModel.requestTokenResponse;
        Timber.i("onDisplaySuccess: tokenCode=%s", requestTokenResponse.toString());
        if (callback != null) {
            callback.onNext(requestTokenResponse);
        }
    }

    public interface Callback {

        void onNext(PocketService.RequestTokenResponse requestTokenResponse);
    }
}
