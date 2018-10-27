package de.dotwee.pocketclipper.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public final class PocketService {
    public static final String API_URL = "https://getpocket.com";

    public static final String PATH_OAUTH = "/auth/authorize";

    public static final String PARAMETER_CONSUMER_KEY = "consumer_key";
    public static final String PARAMETER_REDIRECT_URI_KEY = "redirect_uri";
    public static final String PARAMETER_REQUEST_TOKEN = "request_token";
    public static final String PARAMETER_REQUEST_CODE = "code";
    public static final String PARAMETER_REDIRECT_URI = "redirect_uri";

    public static final String HEADER_X_ACCEPT_JSON = "X-Accept: application/json";


    public interface Pocket {

        @FormUrlEncoded
        @Headers(HEADER_X_ACCEPT_JSON)
        @POST("/v3/oauth/request")
        Observable<RequestTokenResponse> getRequestToken(
                @Field(PARAMETER_CONSUMER_KEY) String consumerKey,
                @Field(PARAMETER_REDIRECT_URI_KEY) String redirectUri);

        @FormUrlEncoded
        @Headers(HEADER_X_ACCEPT_JSON)
        @POST("/v3/oauth/authorize")
        Observable<AccessTokenResponse> getAccessToken(
                @Field(PARAMETER_CONSUMER_KEY) String consumerKey,
                @Field(PARAMETER_REQUEST_CODE) String code);

    }

    public static class RequestTokenResponse {
        public final String code;

        public RequestTokenResponse(String code) {
            this.code = code;
        }
    }

    public static class AccessTokenResponse {
        public final String accessToken;
        public final String username;

        public AccessTokenResponse(String accessToken, String username) {
            this.accessToken = accessToken;
            this.username = username;
        }
    }
}
