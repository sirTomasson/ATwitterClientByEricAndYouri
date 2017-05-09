package ap.tomassen.online.ruigetweets.model;

import java.util.ArrayList;

import ap.tomassen.online.ruigetweets.Tweet;

/**
 * Created by youri on 9-5-2017.
 */

public class TwitterModel {
    private static TwitterModel twitterModel = null;
    private ArrayList<Tweet> tweets;

    private TwitterModel() {
        tweets = new ArrayList<Tweet>();
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
}
