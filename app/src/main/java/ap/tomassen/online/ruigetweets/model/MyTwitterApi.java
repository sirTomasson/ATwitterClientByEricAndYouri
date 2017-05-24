package ap.tomassen.online.ruigetweets.model;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

/**
 * Created by youri on 17-5-2017.
 */

public class MyTwitterApi extends DefaultApi10a {

    private static MyTwitterApi api = null;

    public static final String CALL_BACK_URL = "http://www.erickerkhoven.nl";
    public static final String API_KEY = "sUWAnBCiS9ssPIlNDWeby1ol4";
    public static final String API_SECRET = "YMpi7BcHd57TWMNIfS9A1lymuulnm0lXKjlpeqgsGmoj2nUZrZ";

    private final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
    private final String AUTHORIZATION_TOKEN_URL = "https://api.twitter.com/oauth/authorize";
    private final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";



    private MyTwitterApi() {

    }

    public static MyTwitterApi getInstance() {
        if (api == null) {
            api = new MyTwitterApi();
        }
        return api;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_URL;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return AUTHORIZATION_TOKEN_URL + "?oauth_token=" + requestToken.getToken();
    }
}
