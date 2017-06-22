package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Mention entity from a tweet
 */
public class Mention extends Entity{
    private int userId;
    private String name;
    private String screenName;

    public Mention(JSONObject mentionObject) throws JSONException {
        super();

        userId = mentionObject.getInt("id");
        name = mentionObject.getString("name");
        screenName = mentionObject.getString("screen_name");

        JSONArray indicesArr = mentionObject.getJSONArray("indices");
        int indices[] = {indicesArr.getInt(0), indicesArr.getInt(1) };
        setIndices(indices);
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }
}
