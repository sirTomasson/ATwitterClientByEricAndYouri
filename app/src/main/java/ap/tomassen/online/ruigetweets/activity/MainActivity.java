package ap.tomassen.online.ruigetweets.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
import java.text.ParseException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.exception.ProfileException;
import ap.tomassen.online.ruigetweets.fragment.MenuFragment;
import ap.tomassen.online.ruigetweets.fragment.ProfileFragment;
import ap.tomassen.online.ruigetweets.fragment.TimelineFragment;
import ap.tomassen.online.ruigetweets.fragment.UpdateStatusFragment;
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.TwitterModel;


public class MainActivity extends AppCompatActivity
        implements MenuFragment.CallBackListener,
        UpdateStatusFragment.CallbackListener {



    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PROFILE_ID = "profile_id";
    public static final String SHIT_BROKE = "sh!t_broke";

    private ListView mLvTwitterFeed;

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
//        secret.equals(SHIT_BROKE) && token.equals(SHIT_BROKE)

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

    private void buildListView() {
        TimelineFragment fragment = new TimelineFragment();

        FragmentTransaction transaction = manager.beginTransaction();
        transaction
                .add(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();

//        mLvTwitterFeed = (ListView) findViewById(R.id.lv_twitter_feed);
//        mLvTwitterFeed.setAdapter(new StatusListAdapter(
//                this,
//                R.layout.list_item,
//                model.getStatuses()
//        ));
//        final Intent profileIntent = new Intent(this, ProfileActivity.class);
//
//        mLvTwitterFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                Object tweetObj = adapterView.getItemAtPosition(position);
//                if (tweetObj instanceof Tweet) {
//                    Tweet status = (Tweet) tweetObj;
//                    profileIntent.putExtra(MainActivity.PROFILE_ID, status.getUser().getId());
//
//                    startActivity(profileIntent);
//                }
//            }
//        });
    }

    @Override
    public void createNewTweet() {
        UpdateStatusFragment fragment = new UpdateStatusFragment();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction
                .add(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
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


            String url = "https://api.twitter.com/1.1/statuses/update.json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            request.addBodyParameter("status", statusUpdate);



            authService.signRequest(api.getAccessToken(), request);

            Response response = request.send();

            String res = null;

            if (response.isSuccessful()) {
                try {
                    res = response.getBody();
                    Log.i(TAG, "doInBackground: response successful" +
                            "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    private class CreateFavoritesTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {

            OAuth10aService authService = model.getAuthService();

            int statusId = integers[0];

            String url = "https://api.twitter.com/1.1/favorites/create.json";

            OAuthRequest request = new OAuthRequest(Verb.POST,
                    url,
                    authService
            );

            request.addBodyParameter("id", statusId + "");
            return null;
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
