package ap.tomassen.online.ruigetweets.model;

import com.github.scribejava.core.model.OAuth1AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import ap.tomassen.online.ruigetweets.exception.ProfileException;

/**
 * Created by Eric on 25-5-2017.
 */

public class Profile extends User {
    private OAuth1AccessToken accessToken = null;

    private static Profile instance = null;

    public static Profile getInstance(JSONObject userObj)
            throws JSONException {
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

    public OAuth1AccessToken getAccessToken() {
        return accessToken;
    }

    public static boolean isSet() {
        if (instance == null) {
            return false;
        } else {
            return true;
        }
    }

}
