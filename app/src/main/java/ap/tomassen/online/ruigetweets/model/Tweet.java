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

public class Tweet {
    private int id;
    private Date createdAt;
    private String text;
    private int reTweetCount;
    private int favoriteCount;
    private List<Entity> entities;
    private User user;

    public Tweet(JSONObject tweetObj) throws JSONException, ParseException {
        createdAt = parseDate(tweetObj.getString("created_at"));
        id = tweetObj.getInt("id");
        text = tweetObj.getString("text");
        reTweetCount = tweetObj.getInt("retweet_count");
        favoriteCount = tweetObj.getInt("favorite_count");

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

    public Tweet(int id, Date createdAt, String text, int reTweetCount, int favoriteCount, List<Entity> entities, User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.reTweetCount = reTweetCount;
        this.favoriteCount = favoriteCount;
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

    public int getReTweetCount() {
        return reTweetCount;
    }

    public void setReTweetCount(int reTweetCount) {
        this.reTweetCount = reTweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public Entity getEntity(int index) {
        return entities.get(index);
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    private Date parseDate(String date) throws ParseException {
        String twitterFormat =  "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        return sf.parse(date);
    }
}
