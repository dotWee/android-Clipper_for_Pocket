package de.dotwee.pocketclipper.ui.auth.request;

import de.dotwee.pocketclipper.api.PocketApi;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.ui.auth.AbstractAuthViewModel;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RequestTokenAuthViewModel extends AbstractAuthViewModel {

    private Disposable requestTokenResponseDisposable;
    private PocketService.RequestTokenResponse requestTokenResponse;
    private PocketApi pocketApi;

    public RequestTokenAuthViewModel() {
        this.pocketApi = new PocketApi();
        title = "Step 1: Request access token and authorize";
    }

    public PocketService.RequestTokenResponse getRequestTokenResponse() {
        return requestTokenResponse;
    }

    public void onStart() {
        requestTokenResponseDisposable = pocketApi.getRequestTokenObservable()
                .subscribe(new Consumer<PocketService.RequestTokenResponse>() {
                    // onNext
                    @Override
                    public void accept(PocketService.RequestTokenResponse requestTokenResponse) throws Exception {
                        RequestTokenAuthViewModel.this.requestTokenResponse = requestTokenResponse;
                        callback.onDisplaySuccess();
                    }
                }, new Consumer<Throwable>() {
                    // onError
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onDisplayError(throwable);
                    }
                }, new Action() {
                    // onComplete
                    @Override
                    public void run() throws Exception {
                        callback.onDisplayCompleted();
                    }
                }, new Consumer<Disposable>() {
                    // onSub
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        callback.onDisplayStartup();
                    }
                });
    }
}
