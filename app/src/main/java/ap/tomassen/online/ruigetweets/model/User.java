package ap.tomassen.online.ruigetweets.model;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A twitter user that belongs to a tweet
 */
public class User {
    private long id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String profileImageUrl;
    private String profileUrl;
    private String profileBackgroundUrl;
    private String followersCount;
    private String favoritesCount;


    /**
     * creates a user object based on Json from the twitter api
     * @param userObj JSON from the twitter api
     * @throws JSONException when unexpected json is entered
     */
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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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
