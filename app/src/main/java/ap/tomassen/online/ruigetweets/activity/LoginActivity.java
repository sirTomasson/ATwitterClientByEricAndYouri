package ap.tomassen.online.ruigetweets.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.AuthorizationFragment;


import ap.tomassen.online.ruigetweets.fragment.LoginFragment;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentCallbackListener, AuthorizationFragment.AuthorizationCallbackListener {
    private final static String TAG = LoginActivity.class.getSimpleName();


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

            Log.i(TAG, "doInBackground: verifier " + verifier);
            Log.i(TAG, "doInBackground: request token " + reqToken.toString());


            try {

                OAuth1AccessToken accessToken = authService
                        .getAccessToken(reqToken, verifier);

                if (accessToken == null) throw new AssertionError("accessToken cannot be null");

                OAuthRequest request = new OAuthRequest(Verb.GET,
                        "https://api.twitter.com/1.1/account/verify_credentials.json",
                        authService);

                model.setAccessToken(accessToken);

                authService.signRequest(accessToken, request);

                Response response = request.send();

                if (response.isSuccessful()) {
                    String res = response.getBody();

                    JSONObject userObj = new JSONObject(res);
                    Profile.getInstance(userObj);

                    startActivity(intent);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
