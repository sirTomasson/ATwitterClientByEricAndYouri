package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eric on 8-5-2017.
 */

public class Status {
    private Date createdAt;
    private int id;
    private String text;
    private List<Entity> entities;
    private int retweetCount;
    private int favoritesCount;
    private User user;

    public Status(JSONObject tweetObj) throws JSONException, ParseException {
        createdAt = parseDate(tweetObj.getString("created_at"));
        id = tweetObj.getInt("id");
        text = tweetObj.getString("text");
        retweetCount = tweetObj.getInt("retweet_count");
        favoritesCount = tweetObj.getInt("favorite_count");

        entities = new ArrayList<Entity>();

        JSONObject tweetEntities = tweetObj.getJSONObject("entities");

        JSONArray hashTagsArr = tweetEntities.getJSONArray("hashtags");
        for (int i = 0; i < hashTagsArr.length(); i++) {
            if (!hashTagsArr.isNull(i)) {
                entities.add(new HashTag(hashTagsArr.getJSONObject(i)));
            }
        }

        JSONArray userMentionsArr = tweetEntities.getJSONArray("user_mentions");
        for (int i = 0; i < userMentionsArr.length(); i++) {
            if (!userMentionsArr.isNull(i)) {
                entities.add(new Mention(userMentionsArr.getJSONObject(i)));
            }
        }

        JSONArray urlsArr = tweetEntities.getJSONArray("urls");
        for (int i = 0; i < urlsArr.length(); i++) {
            if (!urlsArr.isNull(i)) {
                entities.add(new Link(urlsArr.getJSONObject(i)));
            }
        }

        JSONObject userObj = tweetObj.getJSONObject("user");
        user = new User(userObj);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public User getUser() {
        return user;
    }

    public Status(int id, Date createdAt, String text, int retweetCount, int favoritesCount, List<Entity> entities, User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.retweetCount = retweetCount;
        this.favoritesCount = favoritesCount;
        this.entities = entities;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public Entity getEntity(int index) {
        return entities.get(index);
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    private Date parseDate(String date) throws ParseException {
        String twitterFormat =  "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        return sf.parse(date);
    }
}
