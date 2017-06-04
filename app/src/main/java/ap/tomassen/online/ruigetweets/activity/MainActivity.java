package ap.tomassen.online.ruigetweets.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.scribejava.apis.TwitterApi;
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
import ap.tomassen.online.ruigetweets.model.MyTwitterApi;
import ap.tomassen.online.ruigetweets.model.Profile;
import ap.tomassen.online.ruigetweets.model.Status;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.view.TweetListAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PROFILE_ID = "profile_id";
    public static final String SHIT_BROKE = "sh!t_broke";

    private ListView mLvTwitterFeed;

    private TwitterModel twitterModel = TwitterModel.getInstance();

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

            MyTwitterApi api = MyTwitterApi.getInstance();
            api.setAccessToken(accessToken);
        }
    }

    private class UserProfileRequestTask extends AsyncTask<OAuth1AccessToken, Void, Profile> {

        @Override
        protected Profile doInBackground(OAuth1AccessToken... accessTokens) {
            OAuth10aService authService = twitterModel.getAuthService();
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
            OAuth10aService authService = twitterModel.getAuthService();


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
                }

            } catch (ProfileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return tweetsObject;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            try {
                twitterModel.setStatuses(jsonArray);
                buildListView();
//                startActivity(timelineIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildListView() {
        if (twitterModel.getStatuses() == null) throw new AssertionError("Statuses cannot be null");

        mLvTwitterFeed = (ListView) findViewById(R.id.lv_twitter_feed);
        mLvTwitterFeed.setAdapter(new TweetListAdapter(
                this,
                R.layout.list_item,
                twitterModel.getStatuses()
        ));
        final Intent profileIntent = new Intent(this, ProfileActivity.class);

        mLvTwitterFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Object tweetObj = adapterView.getItemAtPosition(position);
                if (tweetObj instanceof Status) {
                    Status status = (Status) tweetObj;
                    profileIntent.putExtra(MainActivity.PROFILE_ID, status.getUser().getId());

                    startActivity(profileIntent);
                }
            }
        });
    }
}
