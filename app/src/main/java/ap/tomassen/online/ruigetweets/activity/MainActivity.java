package ap.tomassen.online.ruigetweets.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import ap.tomassen.online.ruigetweets.R;
import ap.tomassen.online.ruigetweets.model.Tweet;
import ap.tomassen.online.ruigetweets.model.TwitterModel;
import ap.tomassen.online.ruigetweets.model.User;
import ap.tomassen.online.ruigetweets.view.TweetListAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String PROFILE_ID = "profile_id";
    private ListView mLvTwitterFeed;

    private TwitterModel twitterModel = TwitterModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String filename = "output.json";

        try {
            String JsonString = readAssetIntoString(filename);
            JSONObject jsonObject = new JSONObject(JsonString);
            buildArray(jsonObject);
            User u = twitterModel.get(0).getUser();
            System.out.println(u.getScreenName());
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
                twitterModel.getTweets()
        ));
        final Intent profileIntent = new Intent(this, ProfileActivity.class);

        mLvTwitterFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (view instanceof ImageView) {
                    Log.i(TAG, "onItemClick: view is image view");
                }

                Object tweetObj = adapterView.getItemAtPosition(position);
                if (tweetObj instanceof Tweet) {
                    Tweet tweet = (Tweet) tweetObj;
                    Log.i(TAG, "onItemClick: " + tweet.getUser().getId());
                    profileIntent.putExtra(PROFILE_ID, tweet.getUser().getId());

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
        twitterModel.setTweets(jsonObject);
    }
}
