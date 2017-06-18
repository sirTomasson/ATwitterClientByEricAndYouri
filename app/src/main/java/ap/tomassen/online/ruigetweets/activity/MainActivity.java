package ap.tomassen.online.ruigetweets.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.exception.ProfileException;
import ap.tomassen.online.ruigetweets.fragment.ErrorDialogFragment;
import ap.tomassen.online.ruigetweets.fragment.MenuFragment;
import ap.tomassen.online.ruigetweets.fragment.ProfileFragment;
import ap.tomassen.online.ruigetweets.fragment.SearchFragment;
import ap.tomassen.online.ruigetweets.fragment.TimelineFragment;
import ap.tomassen.online.ruigetweets.fragment.UpdateStatusFragment;
import ap.tomassen.online.ruigetweets.model.Error;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.TwitterModel;


public class MainActivity extends AppCompatActivity
        implements MenuFragment.CallBackListener,
        UpdateStatusFragment.CallbackListener, SearchFragment.SearchFragmentCallbackListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PROFILE_ID = "profile_id";
    public static final String SHIT_BROKE = "sh!t_broke";
    public static final String TAG_DIALOG_FRAGMENT = "dialog_tag_fragment";
    public static final String ERROR_CODE = "ERROR_CODE";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

    private TwitterModel model = TwitterModel.getInstance();
    private MyTwitterApi api = MyTwitterApi.getInstance();


    private FragmentManager manager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(LoginActivity.USER_TOKEN, SHIT_BROKE);

        String secret = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(LoginActivity.USER_SECRET, SHIT_BROKE);

        if (secret.equals(SHIT_BROKE) && token.equals(SHIT_BROKE)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            OAuth1AccessToken accessToken = new OAuth1AccessToken(token, secret);

            new UserProfileRequestTask().execute(accessToken);
            new UserTimelineTask().execute(accessToken);

            api.setAccessToken(accessToken);
        }
    }
    public void createErrorDialog(String json) {
        ArrayList<Error> errors = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            errors = model.getErrorMessagesFromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle b = new Bundle();

        if (errors != null) {
            Error error = errors.get(0);
            b.putString(ERROR_CODE, error.getCode() + "");
            b.putString(ERROR_MESSAGE, error.getMessage());
        }

        dialogFragment.setArguments(b);
        dialogFragment.show(getFragmentManager(), TAG_DIALOG_FRAGMENT);
    }


    /*=======================================CALLBACKS============================================*/

    @Override
    public void createNewTweet() {
        Fragment fragment = manager.findFragmentById(R.id.fl_container);

        if (fragment != null && fragment instanceof UpdateStatusFragment) {
            Log.d(TAG, "createNewTweet: ");
            manager.popBackStack();
        } else {
            fragment = new UpdateStatusFragment();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction
                    .add(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void showSearchTweet() {

        Fragment fragment = manager.findFragmentById(R.id.fl_container);

        if (fragment != null && fragment instanceof SearchFragment) {
            manager.popBackStack();
        } else {

            fragment = new SearchFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction
                    .add(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void showProfile() {
        ProfileFragment fragment = new ProfileFragment();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showTimeLine() {
        TimelineFragment fragment = new TimelineFragment();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void sendTweet(String tweet) {
        new UpdateStatusTask().execute(tweet);
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

    @Override
    public void searchTweet(String searchText) {
        new SearchTweetTask().execute(searchText);
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

    /*==============================ASYNC TASKS===================================================*/

    private class UserProfileRequestTask extends AsyncTask<OAuth1AccessToken, Void, Profile> {

        @Override
        protected Profile doInBackground(OAuth1AccessToken... accessTokens) {
            OAuth10aService authService = model.getAuthService();
            Profile profile = null;

            try {

                OAuthRequest request = new OAuthRequest(Verb.GET,
                        "https://api.twitter.com/1.1/account/verify_credentials.json",
                        authService);

                authService.signRequest(accessTokens[0], request);

                Response response = request.send();

                if (response.isSuccessful()) {
                    String res = response.getBody();

                    JSONObject userObj = new JSONObject(res);
                    profile = Profile.getInstance(userObj, accessTokens[0]);

                } else {
                    createErrorDialog(response.getMessage());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return profile;
        }
    }

    private class UserTimelineTask extends AsyncTask<OAuth1AccessToken, JSONArray, JSONArray> {

        @Override
        protected JSONArray doInBackground(OAuth1AccessToken... oAuth1AccessTokens) {
            OAuth10aService authService = model.getAuthService();


            OAuthRequest request = new OAuthRequest(Verb.GET,
                    "https://api.twitter.com/1.1/statuses/home_timeline.json",
                    authService);

            JSONArray tweetsObject = null;

            try {
                authService.signRequest(Profile.getInstance().getAccessToken(), request);

                Response response = request.send();

                if (response.isSuccessful()) {
                    String res = response.getBody();

                    tweetsObject = new JSONArray(res);
                    model.setStatuses(tweetsObject);

                }


            } catch (ProfileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return tweetsObject;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            showTimeLine();
        }
    }

    private class UpdateStatusTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... Strings) {

            String statusUpdate = Strings[0];

            OAuth10aService authService = model.getAuthService();


            String url = "https://api.twitter.com/1.1/status/update.json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            request.addBodyParameter("status", statusUpdate);


            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            String res = null;

            try {
                if (response.isSuccessful()) {
                    try {
                        res = response.getBody();
                        Log.i(TAG, "doInBackground: response successful" +
                                "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    createErrorDialog(response.getBody());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String aString) {
            super.onPostExecute(aString);

            if (aString != null) {
                showToastMessage(aString);
            }
        }
    }

    private class SearchTweetTask extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... Strings) {
            OAuth10aService authService = model.getAuthService();
            String searchText = Strings[0];
            String url = "https://api.twitter.com/1.1/search/tweets.json?";
            JSONObject searchObject = null;

            OAuthRequest request = new OAuthRequest(Verb.GET,
                    url + "q=" + searchText,
                    authService);

            try {
                authService.signRequest(api.getAccessToken(), request);
                Response response = request.send();
                if (response.isSuccessful()) {
                    String res = response.getBody();
                    searchObject = new JSONObject(res);
                    Log.i(TAG, "searchTweet: response successful" +
                            response.getBody());
//                    model.setStatuses(searchObject);
                } else {
                    Log.i(TAG, "doInBackground: response failed" +
                            response.getBody());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    /*============================================================================================*/

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("errors.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
