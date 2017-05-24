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
    private ArrayList<Status> statuses;
    private ArrayList<User> users;
    private OAuth10aService authService;

    private TwitterModel() {
        authService = new ServiceBuilder()
                .apiSecret(TwitterApi.API_SECRET)
                .apiKey(TwitterApi.API_KEY)
                .callback(TwitterApi.CALL_BACK_URL)
                .build(TwitterApi.getInstance());
    }

    public static TwitterModel getInstance() {
        if (twitterModel == null) {
            twitterModel = new TwitterModel();
        }
        return twitterModel;
    }

    public void add(Status status) {
        statuses.add(status);
    }

    public void add(int index, Status status) {
        statuses.add(index, status);
    }

    public Status get(int index) {
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

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(JSONObject tweetsObj) throws JSONException, ParseException {
        statuses = new ArrayList<Status>();
        users = new ArrayList<User>();

        JSONArray tweetsArray = tweetsObj.getJSONArray("statuses");

        for (int i = 0; i < tweetsArray.length(); i++) {
            JSONObject tweetObj = tweetsArray.getJSONObject(i);
            statuses.add(new Status(tweetObj));
        }

        for (int i = 0; i < tweetsArray.length(); i++) {
            users.add(new User(tweetsArray.getJSONObject(i).getJSONObject("user")));
        }
    }

    public OAuth10aService getAuthService() {
        return authService;
    }
}
