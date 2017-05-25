package ap.tomassen.online.ruigetweets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 25-5-2017.
 */

public class Profile extends User {

    public Profile(JSONObject userObj) throws JSONException {
        super(userObj);
    }
    public Profile(int id, String name, String screenName, String description, String profileImageUrl, String profileUrl, String profileBackgroundUrl, String followersCount, String retweets, String location) {
        super(id, name, screenName, description, profileImageUrl, profileUrl, profileBackgroundUrl, followersCount, retweets, location);
    }

}
