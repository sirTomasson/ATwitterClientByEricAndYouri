package ap.tomassen.online.ruigetweets.activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.TwitterApi;

import ap.tomassen.online.ruigetweets.fragment.LoginFragment;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.class.getSimpleName();

    private TwitterModel model = TwitterModel.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment_container, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //TODO: finish building AsyncTask. but leave commented out for testing other components


        new RequestToken();
    }

    private class RequestToken extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OAuth10aService authService = model.getAuthService();

            String url = null;
            try {
                OAuth1RequestToken reqToken = authService.getRequestToken();
                url = authService.getAuthorizationUrl(reqToken);
                Log.i(TAG, "doInBackground: url = " + url);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
        }
    }
}
