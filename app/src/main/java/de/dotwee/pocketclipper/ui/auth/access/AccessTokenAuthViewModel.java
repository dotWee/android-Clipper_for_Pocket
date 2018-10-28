package de.dotwee.pocketclipper.ui.auth.access;

import de.dotwee.pocketclipper.api.PocketApi;
import de.dotwee.pocketclipper.api.PocketService;
import de.dotwee.pocketclipper.ui.auth.AbstractAuthViewModel;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class AccessTokenAuthViewModel extends AbstractAuthViewModel {

    private Disposable accessTokenResponseDisposable;
    private PocketService.RequestTokenResponse requestTokenResponse;
    private PocketService.AccessTokenResponse accessTokenResponse;
    private PocketApi pocketApi;

    public AccessTokenAuthViewModel() {
        this.pocketApi = new PocketApi();
        title = "Step 2: Request device specific token";
    }

    public void setRequestTokenResponse(PocketService.RequestTokenResponse requestTokenResponse) {
        this.requestTokenResponse = requestTokenResponse;
    }

    public PocketService.AccessTokenResponse getAccessTokenResponse() {
        return accessTokenResponse;
    }

    public void onStart() {


        accessTokenResponseDisposable = pocketApi.getAccessTokenObservable(requestTokenResponse)
                .subscribe(new Consumer<PocketService.AccessTokenResponse>() {
                    @Override
                    public void accept(PocketService.AccessTokenResponse accessTokenResponse) throws Exception {
                        AccessTokenAuthViewModel.this.accessTokenResponse = accessTokenResponse;
                        callback.onDisplaySuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onDisplayError(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        callback.onDisplayCompleted();
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        callback.onDisplayStartup();
                    }
                });
    }
}
