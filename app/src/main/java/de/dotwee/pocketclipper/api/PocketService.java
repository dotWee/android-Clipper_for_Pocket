package de.dotwee.pocketclipper.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public final class PocketService {
    public static final String API_URL = "https://getpocket.com";

    public static final String PARAMETER_CONSUMER_KEY = "consumer_key";
    public static final String PARAMETER_REDIRECT_URI_KEY = "redirect_uri";

    public static final String HEADER_X_ACCEPT_JSON = "X-Accept: application/json";


    public interface Pocket {

        @FormUrlEncoded
        @Headers(HEADER_X_ACCEPT_JSON)
        @POST("/v3/oauth/request")
        Observable<RequestTokenCode> getRequestToken(
                @Field(PARAMETER_CONSUMER_KEY) String consumerKey,
                @Field(PARAMETER_REDIRECT_URI_KEY) String redirectUri);

    }

    public static class RequestTokenCode {
        public final String code;

        public RequestTokenCode(String code) {
            this.code = code;
        }
    }
}
