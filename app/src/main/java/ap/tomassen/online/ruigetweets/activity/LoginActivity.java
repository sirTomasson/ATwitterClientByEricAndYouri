package ap.tomassen.online.ruigetweets.activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.AuthorizationFragment;


import ap.tomassen.online.ruigetweets.fragment.LoginFragment;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentCallbackListener {
    private final static String TAG = LoginActivity.class.getSimpleName();


    private TwitterModel model = TwitterModel.getInstance();

    private String authorizationUrl;

    private AuthorizationFragment authorizationFragment;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new RequestTokenTask().execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    private class RequestTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();

            String url = null;

            try {
                OAuth1RequestToken reqToken = authService.getRequestToken();
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
}
