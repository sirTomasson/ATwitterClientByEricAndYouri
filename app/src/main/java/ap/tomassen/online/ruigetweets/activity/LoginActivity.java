package ap.tomassen.online.ruigetweets.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterApi;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.println(new RequestTokens());
    }

    private class RequestTokens extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            final OAuth10aService authService = new ServiceBuilder()
                    .apiKey("sUWAnBCiS9ssPIlNDWeby1ol4")
                    .apiSecret("YMpi7BcHd57TWMNIfS9A1lymuulnm0lXKjlpeqgsGmoj2nUZrZ")
                    .callback("http://www.erickerkhoven.nl")
                    .build(TwitterApi.getInstance());

            String url = null;
            try {
                OAuth1RequestToken reqToken = authService.getRequestToken();
                url = authService.getAuthorizationUrl(reqToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return url;

        }

    }
}
