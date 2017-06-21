package ap.tomassen.online.ruigetweets.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;


import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.AuthorizationFragment;


import ap.tomassen.online.ruigetweets.fragment.LoginFragment;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentCallbackListener, AuthorizationFragment.AuthorizationCallbackListener {
    private final static String TAG = LoginActivity.class.getSimpleName();
    public static final String USER_TOKEN = "user_token";
    public static final String USER_SECRET = "user_secret";


    private TwitterModel model = TwitterModel.getInstance();

    private OAuth1RequestToken reqToken;

    private String authorizationUrl;

    private AuthorizationFragment authorizationFragment;
    private LoginFragment loginFragment;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new RequestTokenTask().execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(this, MainActivity.class);

        loginFragment = new LoginFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.fl_fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onLoginClick() {
        authorizationFragment = new AuthorizationFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.fl_fragment_container, authorizationFragment)
                .addToBackStack(null)
                .commit();

        authorizationFragment.setAuthorizationUrl(authorizationUrl);
    }

    @Override
    public void AuthorizationListener(String verifier) {
        new AccessTokenTask().execute(verifier);
    }

    private class RequestTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();

            String url = null;

            try {
                reqToken = authService.getRequestToken();
                url = authService.getAuthorizationUrl(reqToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return url;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            authorizationUrl = url;
        }
    }


    private class AccessTokenTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            OAuth10aService authService = model.getAuthService();

            String verifier = strings[0];

            try {


                OAuth1AccessToken accessToken = authService
                        .getAccessToken(reqToken, verifier);


                String token = accessToken.getToken();
                String secret = accessToken.getTokenSecret();

                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext()).edit()
                        .putString(USER_TOKEN, token)
                        .putString(USER_SECRET, secret)
                        .apply();

                MyTwitterApi.getInstance().setAccessToken(accessToken);

                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
