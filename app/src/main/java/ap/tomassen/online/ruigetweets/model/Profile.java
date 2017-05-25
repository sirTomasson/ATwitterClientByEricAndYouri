package ap.tomassen.online.ruigetweets.model;

import org.json.JSONException;
import org.json.JSONObject;

import ap.tomassen.online.ruigetweets.exception.ProfileException;

/**
 * Created by Eric on 25-5-2017.
 */

public class Profile extends User {

    private static Profile instance = null;

    public static Profile getInstance(JSONObject userObj) throws JSONException {
        if (instance == null) {
            instance = new Profile(userObj);
        }
        return instance;
    }

    public static Profile getInstance() throws ProfileException {
        if (instance == null) {
            throw new ProfileException("Profile not set use JSONObject constructor");
        } else {
            return instance;
        }
    }

    private Profile(JSONObject userObj) throws JSONException {
        super(userObj);
    }
    private Profile(int id, String name, String screenName, String description, String profileImageUrl, String profileUrl, String profileBackgroundUrl, String followersCount, String retweets, String location) {
        super(id, name, screenName, description, profileImageUrl, profileUrl, profileBackgroundUrl, followersCount, retweets, location);
    }

}
