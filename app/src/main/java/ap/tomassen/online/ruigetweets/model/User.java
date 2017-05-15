package ap.tomassen.online.ruigetweets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 8-5-2017.
 */

public class User {
    private int id;
    private String name;
    private String screenName;
    private String description;
    private String profileImageUrl;
    private String profileUrl;
    private String profileBackgroundUrl;
    private String followers;
    private String retweets;
    private String location;

    public User(JSONObject userObj) throws JSONException {
        id = userObj.getInt("id");
        name = userObj.getString("name");
        screenName = userObj.getString("screen_name");
        description = userObj.getString("description");
        profileImageUrl = userObj.getString("profile_image_url");
        profileUrl = userObj.getString("url");
        profileBackgroundUrl = userObj.getString("profile_background_image_url");
        followers = userObj.getString("followers_count");
        retweets = userObj.getString("favourites_count");
        location = userObj.getString("location");
    }

    public User(int id, String name, String screenName, String description, String profileImageUrl, String profileUrl, String profileBackgroundUrl, String followers, String retweets, String location) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.profileUrl = profileUrl;
        this.profileBackgroundUrl = profileBackgroundUrl;
        this.followers = followers;
        this.retweets = retweets;
        this.location = location;
    }

    public String getProfileBackgroundUrl() {
        return profileBackgroundUrl;
    }
    public String getFollowers() {
        return followers;
    }
    public String getRetweets() {
        return retweets;
    }
    public String getLocation() {
        return location;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
