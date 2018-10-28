package de.dotwee.pocketclipper.ui.auth.request;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.ui.auth.AbstractAuthFragment;
import timber.log.Timber;

public class RequestTokenAuthFragment extends AbstractAuthFragment<RequestTokenAuthViewModel> {

    private RequestTokenAuthViewModel viewModel;
    private Callback callback;

    public RequestTokenAuthFragment() {
    }

    public static RequestTokenAuthFragment newInstance(Callback callback) {
        RequestTokenAuthFragment fragment = new RequestTokenAuthFragment();
        fragment.setCallback(callback);
        return fragment;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
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

        PocketService.RequestTokenResponse requestTokenResponse = viewModel.getRequestTokenResponse();
        callback.onNext(requestTokenResponse);
    }

    public interface Callback {

        void onNext(PocketService.RequestTokenResponse requestTokenResponse);
    }
}
