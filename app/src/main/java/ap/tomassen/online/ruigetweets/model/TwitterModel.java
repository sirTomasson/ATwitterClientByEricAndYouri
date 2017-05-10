package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by youri on 9-5-2017.
 */

public class TwitterModel {
    private static TwitterModel twitterModel = null;
    private ArrayList<Tweet> tweets;

    private TwitterModel() {

    }

    public static TwitterModel getInstance() {
        if (twitterModel == null) {
            twitterModel = new TwitterModel();
        }
        return twitterModel;
    }

    public void add(Tweet tweet) {
        tweets.add(tweet);
    }

    public void add(int index, Tweet tweet) {
        tweets.add(index, tweet);
    }

    public Tweet get(int index) {
        return tweets.get(index);
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(JSONObject tweetsObj) throws JSONException, ParseException {
        tweets = new ArrayList<Tweet>();

        JSONArray tweetsArray = tweetsObj.getJSONArray("statuses");

        for (int i = 0; i < tweetsArray.length(); i++) {
            JSONObject tweetObj = tweetsArray.getJSONObject(i);
            tweets.add(new Tweet(tweetObj));
        }
    }
}
