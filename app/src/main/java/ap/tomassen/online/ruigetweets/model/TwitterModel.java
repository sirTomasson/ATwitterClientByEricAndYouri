package ap.tomassen.online.ruigetweets.model;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth10aService;

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
    private ArrayList<Tweet> statuses;
    private ArrayList<User> users;
    private OAuth10aService authService;

    private TwitterModel() {
        authService = new ServiceBuilder()
                .apiSecret(MyTwitterApi.API_SECRET)
                .apiKey(MyTwitterApi.API_KEY)
                .callback(MyTwitterApi.CALL_BACK_URL)
                .build(MyTwitterApi.getInstance());
    }

    public static TwitterModel getInstance() {
        if (twitterModel == null) {
            twitterModel = new TwitterModel();
        }
        return twitterModel;
    }

    public void add(Tweet tweet) {
        statuses.add(tweet);
    }

    public void add(int index, Tweet tweet) {
        statuses.add(index, tweet);
    }

    public Tweet get(int index) {
        return statuses.get(index);
    }


    public User getUser(int id) {
        for (User u : users) {

            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public List<Tweet> getStatuses() {
        return statuses;
    }

    public void setStatuses(JSONArray tweetsArray) throws JSONException, ParseException {
        statuses = new ArrayList<Tweet>();
        users = new ArrayList<User>();


        for (int i = 0; i < tweetsArray.length(); i++) {
            JSONObject tweetObj = tweetsArray.getJSONObject(i);
            statuses.add(new Tweet(tweetObj));
        }

        for (int i = 0; i < tweetsArray.length(); i++) {
            users.add(new User(tweetsArray.getJSONObject(i).getJSONObject("user")));
        }
    }

    public OAuth10aService getAuthService() {
        return authService;
    }
}
