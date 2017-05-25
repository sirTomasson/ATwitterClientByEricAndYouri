package ap.tomassen.online.ruigetweets.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import ap.tomassen.online.ruigetweets.R;
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

        Log.i(TAG, "onCreate: token " + token);
        Log.i(TAG, "onCreate: secret " + secret);

        if (secret.equals(SHIT_BROKE) && token.equals(SHIT_BROKE)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            OAuth1AccessToken accessToken = new OAuth1AccessToken(token, secret);
            new UserProfileRequest().execute(accessToken);
        }


        String filename = "output.json";

        try {
            String JsonString = readAssetIntoString(filename);
            JSONObject jsonObject = new JSONObject(JsonString);
            buildArray(jsonObject);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    profileIntent.putExtra(PROFILE_ID, status.getUser().getId());

                    startActivity(profileIntent);
                }
            }
        });
    }


    /**
     * Reads an asset file and returns a string with the full contents.
     *
     * @param filename The filename of the file to read.
     * @return The contents of the file.
     * @throws IOException If file could not be found or not read.
     */
    private String readAssetIntoString(String filename) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            InputStream is = getAssets().open(filename, AssetManager.ACCESS_BUFFER);
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private void buildArray(JSONObject jsonObject) throws JSONException, ParseException {
        twitterModel.setStatuses(jsonObject);
    }

    private class UserProfileRequest extends AsyncTask<OAuth1AccessToken, Object, Profile> {

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
}
