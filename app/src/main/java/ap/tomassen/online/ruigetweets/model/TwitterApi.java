package ap.tomassen.online.ruigetweets.model;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * Created by youri on 17-5-2017.
 */

public class TwitterApi extends DefaultApi10a {

    private static TwitterApi api = null;

    private String requestToken;
    private String authorizeToken;
    private String accessToken;


    private TwitterApi() {
        requestToken = "https://api.twitter.com/oauth2/token";
        authorizeToken = "https://api.twitter.com/oauth/request_token";
        accessToken = "https://api.twitter.com/oauth/access_token";
    }

    public static TwitterApi getInstance() {
        if (api == null) {
            api = new TwitterApi();
        }
        return api;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return null;
    }
}
