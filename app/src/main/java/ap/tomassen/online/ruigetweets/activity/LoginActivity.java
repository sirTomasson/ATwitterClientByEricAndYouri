package ap.tomassen.online.ruigetweets.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;


import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.AuthorizationFragment;


import ap.tomassen.online.ruigetweets.fragment.LoginFragment;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.TwitterModel;



public class LoginActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentCallbackListener, AuthorizationFragment.CallbackListener {

    public static final String USER_TOKEN = "user_token";
    public static final String USER_SECRET = "user_secret";


    private TwitterModel model = TwitterModel.getInstance();

    private OAuth1RequestToken reqToken;

    private String authorizationUrl;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new RequestTokenTask().execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(this, MainActivity.class);

        LoginFragment loginFragment = new LoginFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.fl_fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();

    }

    /**
     * Replaces the current fragment with a WebView so the user can authorize this app
     */
    @Override
    public void onLoginClick() {
        AuthorizationFragment authorizationFragment = new AuthorizationFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle b = new Bundle();
        b.putString(AuthorizationFragment.AUTHORIZATION_URL_KEY, authorizationUrl);
        authorizationFragment.setArguments(b);

        transaction
                .replace(R.id.fl_fragment_container, authorizationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * The CallbackListener method that starts an access token async task
     * @param verifier that is given when the user authorizes this app to use his twitter account
     */
    @Override
    public void authorize(String verifier) {
        new AccessTokenTask().execute(verifier);
    }

    /**
     * Retrieves a request token, then with the request token it retrieves a authorization url were
     * the user can sign in.
     */
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
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            authorizationUrl = url;
        }
    }

    /**
     * retrieves a access token by sending a verifier. After the acces token is retrieved, it is then
     * saved in the user preferences.
     */
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
