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
    private String location;
    private String description;
    private String profileImageUrl;
    private String profileUrl;
    private String profileBackgroundUrl;
    private String followersCount;
    private String favoritesCount;


    public User(JSONObject userObj) throws JSONException {
        id = userObj.getInt("id");
        name = userObj.getString("name");
        screenName = userObj.getString("screen_name");
        location = userObj.getString("location");
        description = userObj.getString("description");
        profileImageUrl = userObj.getString("profile_image_url");
        profileUrl = userObj.getString("url");
        profileBackgroundUrl = userObj.getString("profile_background_image_url");
        followersCount = userObj.getString("followers_count");
        favoritesCount = userObj.getString("favourites_count");
    }

    public User(int id, String name, String screenName, String description, String profileImageUrl, String profileUrl, String profileBackgroundUrl, String followersCount, String retweets, String location) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.profileUrl = profileUrl;
        this.profileBackgroundUrl = profileBackgroundUrl;
        this.followersCount = followersCount;
        this.favoritesCount = retweets;
        this.location = location;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getFavoritesCount() {
        return favoritesCount;
    }

    public String getProfileBackgroundUrl() {
        return profileBackgroundUrl;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
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

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}
