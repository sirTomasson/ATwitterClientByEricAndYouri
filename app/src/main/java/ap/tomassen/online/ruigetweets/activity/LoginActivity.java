package ap.tomassen.online.ruigetweets.activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.fragment.TwitterFragment;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;


import ap.tomassen.online.ruigetweets.fragment.LoginFragment;
import ap.tomassen.online.ruigetweets.model.TwitterModel;

/**
 * Created by youri on 17-5-2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentCallbackListener {
    private final static String TAG = LoginActivity.class.getSimpleName();


    private TwitterModel model = TwitterModel.getInstance();

    private TwitterFragment twitterFragment;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = new LoginFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.fl_fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();

        new RequestToken().execute();

    }

    @Override
    public void onItemSelected() {
        

    }

    private class RequestToken extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OAuth10aService authService = new ServiceBuilder()
                    .apiKey(MyTwitterApi.API_KEY)
                    .apiSecret(MyTwitterApi.API_SECRET)
                    .callback(MyTwitterApi.CALL_BACK_URL)
                    .build(MyTwitterApi.getInstance());

            String url = null;
            try {
                OAuth1RequestToken reqToken = authService.getRequestToken();
                url = authService.getAuthorizationUrl(reqToken);
                Log.i(TAG, "doInBackground: url = " + url);


//                twitterFragment.setURL(url);

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
        }
    }
}
