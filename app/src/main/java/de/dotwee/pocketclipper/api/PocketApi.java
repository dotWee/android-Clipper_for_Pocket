package de.dotwee.pocketclipper.api;

import android.net.Uri;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import de.dotwee.pocketclipper.BuildConfig;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PocketApi {
    private Retrofit retrofit;
    private PocketService.Pocket pocket;
    private Scheduler schedulerObserveOn;

    public PocketApi() {
        this.schedulerObserveOn = AndroidSchedulers.mainThread();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(PocketService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new ObserveOnMainCallAdapterFactory(schedulerObserveOn))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.pocket = retrofit.create(PocketService.Pocket.class);
    }

    public Observable<PocketService.RequestTokenCode> getRequestTokenObservable() {
        return this.pocket.getRequestToken(
                BuildConfig.PocketApiCustomerKey,
                BuildConfig.PocketApiRedirectUri
        );
    }

    public Uri getAuthorizationUri(PocketService.RequestTokenCode requestTokenCode) {
        /*
        return Uri.parse(PocketService.API_URL)
                .buildUpon()
                .appendPath(PocketService.PATH_OAUTH)
                .appendQueryParameter(PocketService.PARAMETER_REQUEST_TOKEN, requestTokenCode.code)
                .appendQueryParameter(PocketService.PARAMETER_REDIRECT_URI, BuildConfig.PocketApiRedirectUri)
                .build();
        */

        String url = PocketService.API_URL + PocketService.PATH_OAUTH + "?" + PocketService.PARAMETER_REQUEST_TOKEN + "=" + requestTokenCode.code + "&" + PocketService.PARAMETER_REDIRECT_URI + "=" + BuildConfig.PocketApiRedirectUri;
        return Uri.parse(url);
    }

    static final class ObserveOnMainCallAdapterFactory extends CallAdapter.Factory {
        final Scheduler scheduler;

        ObserveOnMainCallAdapterFactory(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public @Nullable
        CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            if (getRawType(returnType) != Observable.class) {
                return null; // Ignore non-Observable types.
            }

            // Look up the next call adapter which would otherwise be used if this one was not present.
            //noinspection unchecked returnType checked above to be Observable.
            final CallAdapter<Object, Observable<?>> delegate = (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this, returnType, annotations);

            return new CallAdapter<Object, Object>() {
                @Override
                public Object adapt(Call<Object> call) {
                    // Delegate to get the normal Observable...
                    Observable<?> o = delegate.adapt(call);

                    // ...and change it to send notifications to the observer on the specified scheduler.
                    return o.subscribeOn(Schedulers.io()).observeOn(scheduler);
                }

                @Override
                public Type responseType() {
                    return delegate.responseType();
                }
            };
        }
    }
}
