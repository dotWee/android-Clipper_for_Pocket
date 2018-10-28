package de.dotwee.pocketclipper.ui.auth.access;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.ui.auth.AbstractAuthFragment;
import timber.log.Timber;

public class AccessTokenAuthFragment extends AbstractAuthFragment<AccessTokenAuthViewModel> {

    private AccessTokenAuthViewModel viewModel;
    private PocketService.RequestTokenResponse requestTokenResponse;
    private Callback callback;

    public AccessTokenAuthFragment() {
    }

    @NonNull
    public static AccessTokenAuthFragment newInstance(@NonNull Callback callback, @NonNull PocketService.RequestTokenResponse requestTokenResponse) {
        AccessTokenAuthFragment fragment = new AccessTokenAuthFragment();
        fragment.setCallback(callback);
        fragment.setRequestTokenResponse(requestTokenResponse);
        return fragment;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setRequestTokenResponse(@NonNull PocketService.RequestTokenResponse requestTokenResponse) {
        this.requestTokenResponse = requestTokenResponse;
    }

    @Override
    public void setViewModel(AccessTokenAuthViewModel viewModel) {
        super.setViewModel(viewModel);

        viewModel.setRequestTokenResponse(requestTokenResponse);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.i("onActivityCreated");

        viewModel = ViewModelProviders.of(this).get(AccessTokenAuthViewModel.class);
        viewModel.setCallback(this);
        setViewModel(viewModel);
    }

    @Override
    public void onDisplaySuccess() {
        super.onDisplaySuccess();

        PocketService.AccessTokenResponse accessTokenResponse = viewModel.getAccessTokenResponse();
        Timber.i("onDisplaySuccess: accessTokenResponse=%s", accessTokenResponse.toString());
        callback.onNext(accessTokenResponse);
    }

    public interface Callback {

        void onNext(PocketService.AccessTokenResponse accessTokenResponse);
    }
}
