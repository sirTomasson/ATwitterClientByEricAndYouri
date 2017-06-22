package ap.tomassen.online.ruigetweets.model;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

public class MyTwitterApi extends DefaultApi10a {

    private static MyTwitterApi api = null;

    private OAuth1AccessToken accessToken = null;

    public static final String CALL_BACK_URL = "http://www.erickerkhoven.nl";
    static final String API_KEY = "sUWAnBCiS9ssPIlNDWeby1ol4";
    static final String API_SECRET = "YMpi7BcHd57TWMNIfS9A1lymuulnm0lXKjlpeqgsGmoj2nUZrZ";


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
        return "https://api.twitter.com/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.twitter.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return "https://api.twitter.com/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    public void setAccessToken(OAuth1AccessToken accessToken) {
        if (this.accessToken == null) {
            this.accessToken = accessToken;
        }
    }

    public OAuth1AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * checks if the access token is set
     * @return true if the accessToken is set, otherwise false
     */
    public boolean isAccessTokenSet() {
        return accessToken != null;
    }
}
