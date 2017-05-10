package ap.tomassen.online.ruigetweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric on 8-5-2017.
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


    public Mention(int[] indices, int userId, String name, String screenName) {
        super(indices);
        this.userId = userId;
        this.name = name;
        this.screenName = screenName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
